package com.qwlabs.lang;

import lombok.extern.slf4j.Slf4j;

import javax.validation.constraints.NotNull;
import java.util.concurrent.atomic.LongAdder;
import java.util.function.Consumer;

@Slf4j
public class LimitedRunner<C> {
    private final String name;
    private final LongAdder runs;
    private final int limited;
    private final Consumer<C> consumer;

    public LimitedRunner(@NotNull String name, @NotNull Consumer<C> consumer) {
        this(name, 1, consumer);
    }

    public LimitedRunner(@NotNull String name, int limited, @NotNull Consumer<C> consumer) {
        this.name = name;
        this.limited = limited;
        this.runs = new LongAdder();
        this.consumer = consumer;
    }

    public void run(C c) {
        if (this.runs.sum() >= limited) {
            LOGGER.info("{} is runs is out of limited, ignore current run method call.", name);
            return;
        }
        this.runs.increment();
        try {
            consumer.accept(c);
        } finally {
            this.runs.decrement();
        }
    }
}
