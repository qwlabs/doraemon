package com.qwlabs.lang;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;

import java.time.Duration;
import java.util.Objects;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;

@Slf4j
@Builder
@AllArgsConstructor
public class QueueWorker<C, E> {
    private static final int DEFAULT_MAX_SPIN_COUNT = 10;
    private final String name;
    private final Consumer<C> beforeFunction;
    private final Consumer<C> afterFunction;
    private final BiConsumer<C, E> beforeEachFunction;
    private final BiConsumer<C, E> afterEachFunction;
    private final Function<C, E> pollFunction;
    private final FailureConsumer<C, E> failureFunction;
    private final BiConsumer<C, E> workFunction;
    private final BiFunction<C, E, Boolean> continueFunction;
    private final Duration spinDuration;
    private final Integer maxSpin;
    private StopWatch stopWatch;

    public void execute() {
        execute(null);
    }

    public void execute(C context) {
        Objects.requireNonNull(name, "name can not be null.");
        Objects.requireNonNull(pollFunction, "PollFunction can not be null.");
        Objects.requireNonNull(failureFunction, "FailureFunction can not be null.");
        Objects.requireNonNull(workFunction, "WorkFunction can not be null.");
        stopWatch = new StopWatch(name);
        try {
            executeBefore(context);
            execute(context, 1);
        } finally {
            executeAfter(context);
            LOGGER.info(stopWatch.prettyPrint());
        }
    }

    private void executeAfter(C context) {
        try {
            stopWatch.start("after");
            F2.ifPresent(afterFunction, () -> afterFunction.accept(context));
        } finally {
            stopWatch.stop();
        }
    }

    private void executeBefore(C context) {
        try {
            stopWatch.start("before");
            F2.ifPresent(beforeFunction, () -> beforeFunction.accept(context));
        } finally {
            stopWatch.stop();
        }
    }

    private void executeBeforeEach(C context, E element) {
        F2.ifPresent(beforeEachFunction, () -> beforeEachFunction.accept(context, element));
    }

    private void executeAfterEach(C context, E element) {
        F2.ifPresent(afterEachFunction, () -> afterEachFunction.accept(context, element));
    }

    private void execute(C context, int spinCount) {
        if (getMaxSpin() <= spinCount) {
            LOGGER.warn("out of max spin count.");
            return;
        }
        E element;
        try {
            stopWatch.start("poll");
            element = pollFunction.apply(context);
        } finally {
            stopWatch.stop();
        }
        if (!isContinue(context, element)) {
            LOGGER.info("not need continue.");
            return;
        }
        try {
            stopWatch.start("work");
            executeBeforeEach(context, element);
            workFunction.accept(context, element);
        } catch (Exception e) {
            failureFunction.accept(context, element, e);
        } finally {
            executeAfterEach(context, element);
            stopWatch.stop();
        }
        if (Objects.isNull(spinDuration)) {
            LOGGER.warn("Do not need spin.");
            return;
        }
        this.spin();
        this.execute(context, spinCount + 1);
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

    private boolean isContinue(C context, E element) {
        return Objects.nonNull(element)
                && Optional.ofNullable(continueFunction)
                .map(f -> f.apply(context, element))
                .orElse(true);
    }

    public int getMaxSpin() {
        return Optional.ofNullable(maxSpin).orElse(DEFAULT_MAX_SPIN_COUNT);
    }

    @FunctionalInterface
    public interface FailureConsumer<C, E> {
        void accept(C context, E element, Exception e);
    }
}