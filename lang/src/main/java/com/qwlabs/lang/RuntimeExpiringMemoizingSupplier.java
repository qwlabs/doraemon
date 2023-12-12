package com.qwlabs.lang;

import com.google.common.base.Supplier;

public class RuntimeExpiringMemoizingSupplier<T> implements Supplier<T> {
    private final Supplier<WithExpiring<T>> delegate;
    private T value;
    private volatile long expirationNanos;

    public RuntimeExpiringMemoizingSupplier(Supplier<WithExpiring<T>> delegate) {
        this.delegate = delegate;
    }

    @Override
    public T get() {
        if (expirationNanos == 0) {
            synchronized (this) {
                if (expirationNanos == 0) {
                    realLoad();
                }
            }
        }
        long now = System.nanoTime();
        if (now < expirationNanos) {
            return this.value;
        }
        synchronized (this) {
            realLoad();
        }
        return value;
    }

    private void realLoad() {
        try {
            var withExpiring = delegate.get();
            this.value = withExpiring.getData();
            this.expirationNanos = System.nanoTime() + withExpiring.getExpiresInNanos();
        } catch (Exception e) {
            this.value = null;
            this.expirationNanos = 0;
        }
    }
}
