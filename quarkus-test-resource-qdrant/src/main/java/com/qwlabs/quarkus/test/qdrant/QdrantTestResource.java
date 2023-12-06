package com.qwlabs.quarkus.test.qdrant;

import com.google.common.collect.Maps;
import io.quarkus.test.common.QuarkusTestResourceLifecycleManager;
import lombok.extern.slf4j.Slf4j;
import org.testcontainers.utility.DockerImageName;

import java.util.Map;
import java.util.Objects;

@Slf4j
public class QdrantTestResource implements QuarkusTestResourceLifecycleManager {
    private static final DockerImageName DEFAULT_IMAGE_NAME = DockerImageName.parse("qdrant/qdrant");
    public static final String HOST_KEY = "host";
    public static final String GRPC_PORT_KEY = "grpcPort";
    public static final String HTTP_PORT_KEY = "httpPort";
    public static final String IMAGE = "image";
    public static final String DEFAULT_IMAGE = "qdrant/qdrant";
    public static final String IMAGE_VERSION = "imageVersion";
    public static final String DEFAULT_IMAGE_VERSION = "v1.6.1";
    public static final String REUSE = "reuse";
    public static final String DEFAULT_REUSE = Boolean.FALSE.toString();
    private static QdrantContainer<?> container;
    private String hostKey;
    private String grpcPortKey;
    private String httpPortKey;
    private String image;
    private String imageVersion;
    private Boolean reuse;

    @Override
    public Map<String, String> start() {
        LOGGER.info("Start bootstrap Qdrant container.");
        container = create();
        container.start();
        LOGGER.info("Qdrant container started.");
        Map<String, String> map = Maps.newHashMap();
        if (Objects.nonNull(hostKey)) {
            map.put(hostKey, container.getHost());
        }
        if (Objects.nonNull(grpcPortKey)) {
            map.put(grpcPortKey, container.getGrpcPort().toString());
        }
        if (Objects.nonNull(httpPortKey)) {
            map.put(httpPortKey, container.getHttpPort().toString());
        }
        return map;
    }

    private QdrantContainer<?> create() {
        var dockerImage = DockerImageName.parse(String.format("%s:%s", image, imageVersion))
            .asCompatibleSubstituteFor(DEFAULT_IMAGE_NAME);
        return new QdrantContainer<>(dockerImage).withReuse(this.reuse);
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
        this.reuse = Boolean.parseBoolean(initArgs.getOrDefault(REUSE, DEFAULT_REUSE));
        this.hostKey = initArgs.get(HOST_KEY);
        this.grpcPortKey = initArgs.get(GRPC_PORT_KEY);
        this.httpPortKey = initArgs.get(HTTP_PORT_KEY);
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
}
