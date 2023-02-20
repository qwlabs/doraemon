package com.qwlabs.auditing;

import java.util.Optional;

public interface AuditorIdProvider {
    Optional<String> get(Object entity, AuditingPhase phase);
}
