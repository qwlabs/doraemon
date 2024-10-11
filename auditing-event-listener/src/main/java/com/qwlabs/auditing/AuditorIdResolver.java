package com.qwlabs.auditing;

import java.util.Optional;

public interface AuditorIdResolver {
    Optional<String> resolve(Object entity);
}
