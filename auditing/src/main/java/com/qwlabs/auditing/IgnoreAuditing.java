package com.qwlabs.auditing;

import jakarta.persistence.Transient;

public interface IgnoreAuditing {
    @Transient
    boolean isIgnoreAuditing(PersistPhase phase);
}
