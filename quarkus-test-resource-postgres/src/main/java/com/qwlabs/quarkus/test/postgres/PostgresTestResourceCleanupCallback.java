package com.qwlabs.quarkus.test.postgres;

import com.qwlabs.lang.C2;
import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.callback.QuarkusTestBeforeEachCallback;
import io.quarkus.test.junit.callback.QuarkusTestMethodContext;
import lombok.extern.slf4j.Slf4j;
import org.junit.platform.commons.support.AnnotationSupport;

import java.util.List;
import java.util.Objects;

@Slf4j
public class PostgresTestResourceCleanupCallback implements QuarkusTestBeforeEachCallback {
    @Override
    public void beforeEach(QuarkusTestMethodContext context) {
        List<QuarkusTestResource> resources = AnnotationSupport.findRepeatableAnnotations(
                context.getTestInstance().getClass(), QuarkusTestResource.class);
        boolean isPostgresTestResource = C2.stream(resources)
                .map(QuarkusTestResource::value)
                .anyMatch(clazz -> Objects.equals(PostgresTestResource.class, clazz));
        if (!isPostgresTestResource) {
            LOGGER.warn("class is not PostgresTestResource, ignore cleanup.");
            return;
        }
        PostgresTestResource.cleanup();
    }
}
