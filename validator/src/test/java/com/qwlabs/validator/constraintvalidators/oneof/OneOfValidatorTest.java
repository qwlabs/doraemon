package com.qwlabs.validator.constraintvalidators.oneof;

import com.qwlabs.validator.constraints.OneOf;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OneOfValidatorTest {
    private OneOfValidator validator;

    @BeforeEach
    void setUp() {
        this.validator = new OneOfValidator();
    }

    @Test
    void should_return_true_when_null() {
        assertTrue(this.validator.isValid(null, null));
    }

    @Test
    void should_return_false_when_not_in(@Mock OneOf oneOf) {
        String[] oneOfValue = new String[]{"a", "B"};
        when(oneOf.value()).thenReturn(oneOfValue);
        when(oneOf.ignoreCase()).thenReturn(false);
        when(oneOf.ignoreWhitespace()).thenReturn(false);

        this.validator.initialize(oneOf);
        assertFalse(this.validator.isValid("A", null));
    }

    @Test
    void should_return_false_when_in(@Mock OneOf oneOf) {
        String[] oneOfValue = new String[]{"a", "B"};
        when(oneOf.value()).thenReturn(oneOfValue);
        when(oneOf.ignoreCase()).thenReturn(false);
        when(oneOf.ignoreWhitespace()).thenReturn(false);

        this.validator.initialize(oneOf);
        assertTrue(this.validator.isValid("B", null));
    }


    @Test
    void should_return_false_when_not_in_and_ignore_case(@Mock OneOf oneOf) {
        String[] oneOfValue = new String[]{"a", "B"};
        when(oneOf.value()).thenReturn(oneOfValue);
        when(oneOf.ignoreCase()).thenReturn(true);
        when(oneOf.ignoreWhitespace()).thenReturn(false);

        this.validator.initialize(oneOf);
        assertFalse(this.validator.isValid("D", null));
    }

    @Test
    void should_return_true_when_not_in_and_ignore_case(@Mock OneOf oneOf) {
        String[] oneOfValue = new String[]{"a", "B"};
        when(oneOf.value()).thenReturn(oneOfValue);
        when(oneOf.ignoreCase()).thenReturn(true);
        when(oneOf.ignoreWhitespace()).thenReturn(false);

        this.validator.initialize(oneOf);
        assertTrue(this.validator.isValid("b", null));
    }

    @Test
    void should_return_true_when_allow_empty(@Mock OneOf oneOf) {
        String[] oneOfValue = new String[]{"a", "B"};
        when(oneOf.value()).thenReturn(oneOfValue);
        when(oneOf.allowEmpty()).thenReturn(true);
        when(oneOf.ignoreCase()).thenReturn(true);
        when(oneOf.ignoreWhitespace()).thenReturn(false);

        this.validator.initialize(oneOf);
        assertTrue(this.validator.isValid("", null));
    }

    @Test
    void should_return_false_when_not_in_ignore_whitespace(@Mock OneOf oneOf) {
        String[] oneOfValue = new String[]{"a", "B"};
        when(oneOf.value()).thenReturn(oneOfValue);
        when(oneOf.ignoreCase()).thenReturn(false);
        when(oneOf.ignoreWhitespace()).thenReturn(true);

        this.validator.initialize(oneOf);
        assertFalse(this.validator.isValid("D  ", null));
    }

    @Test
    void should_return_true_when_not_in_ignore_whitespace(@Mock OneOf oneOf) {
        String[] oneOfValue = new String[]{"a", "B"};
        when(oneOf.value()).thenReturn(oneOfValue);
        when(oneOf.ignoreCase()).thenReturn(false);
        when(oneOf.ignoreWhitespace()).thenReturn(true);

        this.validator.initialize(oneOf);
        assertTrue(this.validator.isValid("B  ", null));
    }
}