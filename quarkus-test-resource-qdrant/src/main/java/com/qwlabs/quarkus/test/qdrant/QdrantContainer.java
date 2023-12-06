package com.qwlabs.quarkus.test.qdrant;

import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.traits.LinkableContainer;
import org.testcontainers.containers.wait.strategy.LogMessageWaitStrategy;
import org.testcontainers.utility.DockerImageName;

import java.time.Duration;
import java.time.temporal.ChronoUnit;

public class QdrantContainer<SELF extends QdrantContainer<SELF>>
    extends GenericContainer<SELF>
    implements LinkableContainer {

    public static final String DEFAULT_TAG = "v1.6.1";
    private static final DockerImageName DEFAULT_IMAGE_NAME = DockerImageName.parse("qdrant/qdrant");
    public static final Integer HTTP_PORT = 6333;
    public static final Integer GRPC_PORT = 6334;

    @Deprecated
    public QdrantContainer() {
        this(DEFAULT_IMAGE_NAME.withTag(DEFAULT_TAG));
    }

    public QdrantContainer(final String dockerImageName) {
        this(DockerImageName.parse(dockerImageName));
    }

    public QdrantContainer(final DockerImageName dockerImageName) {
        super(dockerImageName);
        dockerImageName.assertCompatibleWith(DEFAULT_IMAGE_NAME);
        this.waitStrategy =
            new LogMessageWaitStrategy()
                .withRegEx(".*Qdrant gRPC listening on.*\\s")
                .withTimes(2)
                .withStartupTimeout(Duration.of(60, ChronoUnit.SECONDS));
        addExposedPort(HTTP_PORT);
        addExposedPort(GRPC_PORT);
    }

    public Integer getGrpcPort() {
        return GRPC_PORT;
    }

    public Integer getHttpPort() {
        return HTTP_PORT;
    }
}
