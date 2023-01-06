package com.qwlabs.q.cdi;

import com.qwlabs.q.conditions.AndQCondition;
import com.qwlabs.q.conditions.OrQCondition;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
class TypedQFormatterTest {

    private QFormatContext andContext = QFormatContext.builder()
            .conditionType(AndQCondition.class)
            .dialect("a")
            .build();
    private QFormatContext orContext = QFormatContext.builder()
            .conditionType(OrQCondition.class)
            .dialect("a")
            .build();

    @Test
    void test_dispatchable() {
        var formatter = new AFormatter();
        assertTrue(formatter.dispatchable(andContext));
        assertFalse(formatter.dispatchable(orContext));
    }

    @Test
    void should_format() {
        var formatter = new AFormatter();
        String value = formatter.format(AndQCondition.builder().build());
        assertThat(value, is("from AFormatter"));
    }

    private static class AFormatter extends TypedQFormatter<AndQCondition> {

        @Override
        protected String dialect() {
            return "a";
        }

        @Override
        protected String doFormat(AndQCondition condition) {
            return "from AFormatter";
        }
    }
}