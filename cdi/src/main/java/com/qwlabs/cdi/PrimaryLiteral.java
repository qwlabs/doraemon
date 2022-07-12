package com.qwlabs.cdi;

import javax.enterprise.util.AnnotationLiteral;

public final class PrimaryLiteral extends AnnotationLiteral<Primary> implements Primary {
    public static final PrimaryLiteral INSTANCE = new PrimaryLiteral();

    public PrimaryLiteral() {
    }
}
