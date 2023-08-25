package com.qwlabs.lang;

import org.junit.jupiter.api.Test;

import java.util.concurrent.TimeUnit;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;

class StopWatchTest {
    @Test
    void should_print() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start("task1");
        stopWatch.stop();
        var content = stopWatch.prettyPrint(TimeUnit.SECONDS);
        assertThat(content, containsString("SECONDS"));
        assertThat(content, containsString("task1"));
    }
}
