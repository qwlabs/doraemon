package com.qwlabs.cdi;

import jakarta.enterprise.event.Event;

import java.util.concurrent.CompletionStage;

public class Events {

    public static <T> CompletionStage<T> fireBoth(Event<T> event, T instance) {
        event.fire(instance);
        return event.fireAsync(instance);
    }
}
