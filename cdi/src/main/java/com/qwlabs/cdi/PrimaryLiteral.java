package com.qwlabs.cdi;

import jakarta.enterprise.util.AnnotationLiteral;

public final class PrimaryLiteral extends AnnotationLiteral<Primary> implements Primary {
    public static final PrimaryLiteral INSTANCE = new PrimaryLiteral();

    public PrimaryLiteral() {
    }
}
