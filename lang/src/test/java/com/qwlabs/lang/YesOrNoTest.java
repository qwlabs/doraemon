package com.qwlabs.lang;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertSame;

public class YesOrNoTest {

    @Test
    void should_boolean_value() {
        assertSame(YesOrNo.YES.booleanValue(), true);
        assertSame(YesOrNo.NO.booleanValue(), false);
    }

    @Test
    void should_short_name() {
        assertSame(YesOrNo.YES.shortName(), "Y");
        assertSame(YesOrNo.NO.shortName(), "N");
    }

    @Test
    void should_of() {
        assertSame(YesOrNo.of(true), YesOrNo.YES);
        assertSame(YesOrNo.of(false), YesOrNo.NO);
        assertSame(YesOrNo.of(Boolean.TRUE), YesOrNo.YES);
        assertSame(YesOrNo.of(Boolean.FALSE), YesOrNo.NO);
    }

    @Test
    void should_logic_op() {
        assertSame(YesOrNo.YES.and(YesOrNo.YES), YesOrNo.YES);
        assertSame(YesOrNo.YES.and(YesOrNo.NO), YesOrNo.NO);
        assertSame(YesOrNo.NO.and(YesOrNo.NO), YesOrNo.NO);
        assertSame(YesOrNo.NO.and(YesOrNo.YES), YesOrNo.NO);


        assertSame(YesOrNo.YES.or(YesOrNo.YES), YesOrNo.YES);
        assertSame(YesOrNo.YES.or(YesOrNo.NO), YesOrNo.YES);
        assertSame(YesOrNo.NO.or(YesOrNo.NO), YesOrNo.NO);
        assertSame(YesOrNo.NO.or(YesOrNo.YES), YesOrNo.YES);
    }
}
