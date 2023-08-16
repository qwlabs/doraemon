package com.qwlabs.lang;


import java.text.NumberFormat;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;


public class StopWatch {

    private final String id;

    private final Map<String, Task> tasks = new HashMap<>(1);

    private long totalNanos = 0L;
    private int count = 0;

    private long startNanos;

    private Task lastTask;

    public StopWatch() {
        this("");
    }

    public StopWatch(String id) {
        this.id = id;
    }

    public String getId() {
        return this.id;
    }

    public void start() throws IllegalStateException {
        start("");
    }

    public void start(String taskName) throws IllegalStateException {
        if (this.lastTask != null) {
            throw new IllegalStateException("Can't start StopWatch: it's already running");
        }
        this.lastTask = new Task(taskName);
        this.startNanos = System.nanoTime();
    }

    public void stop() throws IllegalStateException {
        if (this.lastTask == null) {
            throw new IllegalStateException("Can't stop StopWatch: it's not running");
        }
        long nanos = System.nanoTime() - this.startNanos;
        tasks.computeIfAbsent(this.lastTask.name, (key) -> this.lastTask).append(nanos);
        this.totalNanos += nanos;
        this.count++;
        this.lastTask = null;
    }

    public boolean isRunning() {
        return (this.lastTask != null);
    }

    public String shortSummary() {
        return shortSummary(TimeUnit.NANOSECONDS);
    }

    public String shortSummary(TimeUnit timeUnit) {
        return "StopWatch '%s': running time = %d %s".formatted(getId(), of(totalNanos, timeUnit), timeUnit.name());
    }

    private long of(long totalNanos, TimeUnit timeUnit) {
        return timeUnit.convert(Duration.ofNanos(totalNanos));
    }

    public String prettyPrint() {
        return prettyPrint(TimeUnit.NANOSECONDS);
    }

    public String prettyPrint(TimeUnit timeUnit) {
        StringBuilder sb = new StringBuilder(shortSummary());
        sb.append('\n');
        sb.append("---------------------------------------------\n");
        sb.append(timeUnit.name()).append("         %     Task name\n");
        sb.append("---------------------------------------------\n");
        NumberFormat nf = NumberFormat.getNumberInstance();
        nf.setMinimumIntegerDigits(9);
        nf.setGroupingUsed(false);
        NumberFormat pf = NumberFormat.getPercentInstance();
        pf.setMinimumIntegerDigits(3);
        pf.setGroupingUsed(false);
        this.tasks.values().forEach(task -> {
            sb.append(nf.format(of(task.totalNanos, timeUnit))).append("  ");
            sb.append(pf.format((double) task.totalNanos / totalNanos)).append("  ");
            sb.append(task.name).append('\n');
        });
        return sb.toString();
    }

    @Override
    public String toString() {
        return prettyPrint();
    }

    public static final class Task {
        private final String name;
        private int count;
        private long totalNanos;

        Task(String name) {
            this.name = name;
            this.count = 0;
            this.totalNanos = 0L;
        }

        public void append(long nanos) {
            this.totalNanos += nanos;
            this.count++;
        }
    }
}
