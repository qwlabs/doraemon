package com.qwlabs.quarkus.test.databases;

import io.quarkus.test.common.QuarkusTestResourceLifecycleManager;
import lombok.extern.slf4j.Slf4j;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.ext.ScriptUtils;
import org.testcontainers.jdbc.JdbcDatabaseDelegate;

import java.util.Map;
import java.util.Objects;

@Slf4j
public class PostgresTestResource implements QuarkusTestResourceLifecycleManager {
    public static final String IMAGE = "image";
    public static final String DEFAULT_IMAGE = "postgres";
    public static final String IMAGE_VERSION = "imageVersion";
    public static final String DEFAULT_IMAGE_VERSION = "14.6-alpine";
    public static final String DATABASE_NAME = "databaseName";
    public static final String DEFAULT_DATABASE_NAME = "quarkus";
    public static final String USERNAME = "username";
    public static final String DEFAULT_USERNAME = "quarkus";
    public static final String PASSWORD = "password";
    public static final String DEFAULT_PASSWORD = "quarkus";
    public static final String CLEANUP_SQL_LOCATION = "cleanupSqlLocation";
    public static final String DEFAULT_CLEANUP_SQL_LOCATION = "db/cleanup.sql";
    public static final String CLEANUP_ENABLED = "cleanupEnabled";
    public static final String DEFAULT_CLEANUP_ENABLED = Boolean.TRUE.toString();

    private static PostgreSQLContainer<?> container;
    private static boolean cleanupEnabled;
    private static String cleanupSqlLocation;
    private String image;
    private String imageVersion;
    private String databaseName;
    private String username;
    private String password;

    @Override
    public Map<String, String> start() {
        LOGGER.info("Start bootstrap Postgres container.");
        container = create();
        container.start();
        LOGGER.info("Postgres container started.");
        return Map.of(
            "quarkus.datasource.jdbc.url", container.getJdbcUrl(),
            "quarkus.datasource.username", container.getUsername(),
            "quarkus.datasource.password", container.getPassword()
        );
    }

    private PostgreSQLContainer<?> create() {
        return new PostgreSQLContainer<>(String.format("%s:%s", image, imageVersion))
            .withDatabaseName(databaseName)
            .withUsername(username)
            .withPassword(password)
                .withExposedPorts();
    }

    @Override
    public void stop() {
        if (Objects.nonNull(container)) {
            container.stop();
        }
    }

    @Override
    public void init(Map<String, String> initArgs) {
        this.image = initArgs.getOrDefault(IMAGE, DEFAULT_IMAGE);
        this.imageVersion = initArgs.getOrDefault(IMAGE_VERSION, DEFAULT_IMAGE_VERSION);
        this.databaseName = initArgs.getOrDefault(DATABASE_NAME, DEFAULT_DATABASE_NAME);
        this.username = initArgs.getOrDefault(USERNAME, DEFAULT_USERNAME);
        this.password = initArgs.getOrDefault(PASSWORD, DEFAULT_PASSWORD);
        cleanupEnabled = Boolean.valueOf(initArgs.getOrDefault(CLEANUP_ENABLED, DEFAULT_CLEANUP_ENABLED));
        cleanupSqlLocation = initArgs.getOrDefault(CLEANUP_SQL_LOCATION, DEFAULT_CLEANUP_SQL_LOCATION);
    }

    @Override
    public void inject(Object testInstance) {
        LOGGER.error("inject {}", testInstance);
        QuarkusTestResourceLifecycleManager.super.inject(testInstance);
    }

    @Override
    public void inject(TestInjector testInjector) {
        LOGGER.error("inject {}", testInjector);
        QuarkusTestResourceLifecycleManager.super.inject(testInjector);
    }

    public static void cleanup() {
        cleanup(cleanupSqlLocation);
    }

    public static void cleanup(String cleanupSqlLocation) {
        if (!cleanupEnabled) {
            LOGGER.info("Postgres container config cleanup disabled, ignore it");
            return;
        }
        if (Objects.isNull(container)) {
            LOGGER.warn("Postgres container is null, can not cleanup, ignore it");
            return;
        }
        LOGGER.info("Start clean up postgres container.");
        JdbcDatabaseDelegate containerDelegate = new JdbcDatabaseDelegate(container, "");
        ScriptUtils.runInitScript(containerDelegate, cleanupSqlLocation);
        LOGGER.info("Clean up postgres container completed.");
    }
}
