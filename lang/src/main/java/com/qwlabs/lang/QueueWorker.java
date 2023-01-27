package com.qwlabs.lang;

import com.google.common.collect.Sets;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.time.Duration;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;

@Slf4j
@Builder
@AllArgsConstructor
public class QueueWorker<C, E> {
    private static final int DEFAULT_MAX_SPIN_COUNT = 10;
    private final String name;
    private final Consumer<C> onBefore;
    private final Consumer<C> onAfter;
    private final BiConsumer<C, E> onBeforeEach;
    private final BiConsumer<C, E> onAfterEach;
    private final Function<C, E> onPoll;
    private final FailedConsumer<C, E> onFailed;
    private final BiConsumer<C, E> onWork;
    private final ContinueWhenPredicate<C, E> continueWhen;
    private final Duration spinDuration;
    private final Integer maxRuns;
    private StopWatch stopWatch;

    public void execute() {
        execute(null);
    }

    public void execute(C context) {
        Objects.requireNonNull(name, "name can not be null.");
        Objects.requireNonNull(onPoll, "onPoll can not be null.");
        Objects.requireNonNull(onFailed, "onFailed can not be null.");
        Objects.requireNonNull(onWork, "onWork can not be null.");
        stopWatch = new StopWatch(name);
        var runs = 1;
        try {
            executeBefore(context);
            while (true) {
                if (!execute(context, runs)) {
                    break;
                }
                runs++;
            }
        } finally {
            executeAfter(context);
            LOGGER.info(stopWatch.prettyPrint());
        }
    }

    private void executeAfter(C context) {
        try {
            stopWatch.start("after");
            F2.ifPresent(onAfter, () -> onAfter.accept(context));
        } finally {
            stopWatch.stop();
        }
    }

    private void executeBefore(C context) {
        try {
            stopWatch.start("before");
            F2.ifPresent(onBefore, () -> onBefore.accept(context));
        } finally {
            stopWatch.stop();
        }
    }

    private void executeBeforeEach(C context, E element) {
        F2.ifPresent(onBeforeEach, () -> onBeforeEach.accept(context, element));
    }

    private void executeAfterEach(C context, E element) {
        F2.ifPresent(onAfterEach, () -> onAfterEach.accept(context, element));
    }

    private void executeOnFailed(C context, E element, Exception e) {
        try {
            onFailed.accept(context, element, e);
        } catch (Exception exception) {
            LOGGER.warn("on failed handler error.", exception);
        }
    }

    private boolean execute(C context, int runs) {
        if (isOutOfMaxRuns(runs)) {
            LOGGER.info("out of max runs.");
            return false;
        }
        E element;
        try {
            stopWatch.start("poll");
            element = onPoll.apply(context);
        } finally {
            stopWatch.stop();
        }
        if (!isContinue(context, element, null)) {
            LOGGER.info("not need continue.");
            return false;
        }
        try {
            stopWatch.start("work");
            executeBeforeEach(context, element);
            onWork.accept(context, element);
        } catch (Exception e) {
            executeOnFailed(context, element, e);
            if (!isContinue(context, element, e)) {
                LOGGER.info("not need continue.");
                return false;
            }
        } finally {
            executeAfterEach(context, element);
            stopWatch.stop();
        }
        if (Objects.isNull(spinDuration)) {
            this.spin();
        }
        return true;
    }

    private boolean isOutOfMaxRuns(int runs) {
        return Optional.ofNullable(maxRuns)
                .map(mr -> mr < runs)
                .orElse(false);
    }

    private void spin() {
        if (spinDuration == null || spinDuration.isZero()) {
            return;
        }
        try {
            stopWatch.start("spin");
            Thread.sleep(spinDuration.toMillis());
        } catch (InterruptedException e) {
            LOGGER.debug("Spin while wait next execute was interrupted", e);
        } finally {
            stopWatch.stop();
        }
    }

    private boolean isContinue(C context, E element, Exception e) {
        return Objects.nonNull(element)
                && Optional.ofNullable(continueWhen)
                .map(f -> f.test(context, element, e))
                .orElse(true);
    }

    @FunctionalInterface
    public interface ContinueWhenPredicate<C, E> {
        boolean test(C context, E element, @Nullable Exception e);
    }

    @FunctionalInterface
    public interface FailedConsumer<C, E> {
        void accept(C context, E element, Exception e);
    }
}
