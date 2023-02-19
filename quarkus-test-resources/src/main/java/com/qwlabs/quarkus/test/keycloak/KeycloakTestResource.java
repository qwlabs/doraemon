package com.qwlabs.quarkus.test.keycloak;

import com.google.common.base.Suppliers;
import io.quarkus.test.common.QuarkusTestResourceLifecycleManager;
import io.restassured.RestAssured;
import io.restassured.specification.RequestSpecification;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.representations.AccessTokenResponse;
import org.keycloak.representations.idm.ClientRepresentation;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.RealmRepresentation;
import org.keycloak.representations.idm.UserRepresentation;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Supplier;
import java.util.stream.Stream;

@Slf4j
public class KeycloakTestResource implements QuarkusTestResourceLifecycleManager {
    public static final String TEST_SERVICE_CLIENT = "test-service";
    public static final String IMAGE = "imageVersion";
    public static final String DEFAULT_IMAGE = "quay.io/keycloak/keycloak";
    public static final String IMAGE_VERSION = "imageVersion";
    public static final String DEFAULT_IMAGE_VERSION = "20.0.3-0";

    public static final String DEFAULT_SECRET = "defaultSecret";
    public static final String DEFAULT_DEFAULT_SECRET = "secret";

    public static final String CLEANUP_ENABLED = "cleanupEnabled";

    public static final String DEFAULT_CLEANUP_ENABLED = Boolean.TRUE.toString();

    public static final String STARTUP_TIMEOUT = "startupTimeout";

    public static final String DEFAULT_STARTUP_TIMEOUT = Duration.ofMinutes(10).toString();

    private static KeycloakContainer container;
    private static boolean cleanupEnabled;
    private static String defaultSecret;
    private static Supplier<Keycloak> adminClient = Suppliers.memoize(KeycloakTestResource::createAdminClient);

    private String image;
    private String imageVersion;
    private Duration startupTimeout;

    public static void cleanup() {
        if (!cleanupEnabled) {
            LOGGER.info("Keycloak container config cleanup disabled, ignore it");
            return;
        }
        if (Objects.isNull(container)) {
            LOGGER.warn("Keycloak container is null, can not cleanup, ignore it");
            return;
        }
        LOGGER.info("Start clean up keycloak container.");
        adminClient.get().realms().findAll()
                .forEach(realm -> deleteRealms(realm.getRealm()));
        LOGGER.info("Clean up keycloak container completed.");
    }

    @Override
    public Map<String, String> start() {
        LOGGER.info("Start bootstrap Keycloak container.");
        container = create();
        container.start();
        LOGGER.info("Keycloak container started.");
        return genConfig();
    }

    private static Keycloak createAdminClient() {
        return container.getKeycloakAdminClient();
    }

    @Override
    public void init(Map<String, String> initArgs) {
        this.image = initArgs.getOrDefault(IMAGE, DEFAULT_IMAGE);
        this.imageVersion = initArgs.getOrDefault(IMAGE_VERSION, DEFAULT_IMAGE_VERSION);
        this.startupTimeout = Duration.parse(initArgs.getOrDefault(STARTUP_TIMEOUT, DEFAULT_STARTUP_TIMEOUT));
        defaultSecret = initArgs.getOrDefault(DEFAULT_SECRET, DEFAULT_DEFAULT_SECRET);
        cleanupEnabled = Boolean.parseBoolean(initArgs.getOrDefault(CLEANUP_ENABLED, DEFAULT_CLEANUP_ENABLED));
    }

    @Override
    public void stop() {
        if (Objects.nonNull(container)) {
            container.stop();
        }
    }

    private KeycloakContainer create() {
        return new KeycloakContainer(String.format("%s:%s", image, imageVersion))
                .withDisabledCaching()
                .withStartupTimeout(startupTimeout);
    }

    private Map<String, String> genConfig() {
        return Map.of(
                "quarkus.oidc.auth-server-url", String.format("%srealms/%s", container.getAuthServerUrl(), KeycloakContainer.MASTER_REALM),
                "quarkus.tenant.oidc.auth-server-url", container.getAuthServerUrl(),
                "quarkus.keycloak.admin-client.server-url", container.getAuthServerUrl(),
                "quarkus.keycloak.admin-client.realm", KeycloakContainer.MASTER_REALM,
                "quarkus.keycloak.admin-client.username", container.getAdminUsername(),
                "quarkus.keycloak.admin-client.password", container.getAdminPassword(),
                "quarkus.keycloak.admin-client.grant-type", CredentialRepresentation.PASSWORD
        );
    }

    public static ClientRepresentation createServiceClient(String clientId, String clientSecret) {
        ClientRepresentation client = new ClientRepresentation();
        client.setClientId(clientId);
        client.setPublicClient(false);
        client.setSecret(clientSecret);
        client.setDirectAccessGrantsEnabled(true);
        client.setServiceAccountsEnabled(true);
        client.setEnabled(true);
        return client;
    }

    public static ClientRepresentation createServiceClient(String clientId) {
        return createServiceClient(clientId, defaultSecret);
    }

    public static ClientRepresentation createWebAppClient(String clientId, String clientSecret) {
        ClientRepresentation client = new ClientRepresentation();
        client.setClientId(clientId);
        client.setPublicClient(false);
        client.setSecret(clientSecret);
        client.setRedirectUris(List.of("*"));
        client.setEnabled(true);
        return client;
    }

    public static ClientRepresentation createWebAppClient(String clientId) {
        return createWebAppClient(clientId, defaultSecret);
    }

    public static UserRepresentation createUser(String username) {
        UserRepresentation user = new UserRepresentation();
        user.setUsername(username);
        user.setEnabled(true);
        user.setCredentials(new ArrayList<>());
        user.setRealmRoles(new ArrayList<>());
        user.setEmail(username + "@qwlabs.com");
        CredentialRepresentation credential = new CredentialRepresentation();
        credential.setType(CredentialRepresentation.PASSWORD);
        credential.setValue(username);
        credential.setTemporary(false);
        user.getCredentials().add(credential);
        return user;
    }

    public static String getAccessToken(String realm, String userName) {
        return getAccessToken(realm, TEST_SERVICE_CLIENT, userName);
    }

    public static String getAccessToken(String realm, String clientId, String userName) {
        return getAccessToken(realm, clientId, defaultSecret, userName);
    }

    public static String getAccessToken(String realm, String clientId, String clientSecret, String userName) {
        return createRequestSpec().param("grant_type", "password")
                .param("username", userName)
                .param("password", userName)
                .param("client_id", clientId)
                .param("client_secret", clientSecret)
                .when()
                .post(container.getAuthServerUrl() + "realms/" + realm + "/protocol/openid-connect/token")
                .as(AccessTokenResponse.class).getToken();
    }

    public static RealmRepresentation createRealm(String name) {
        RealmRepresentation realm = new RealmRepresentation();
        realm.setRealm(name);
        realm.setEnabled(true);
        realm.setUsers(new ArrayList<>());
        realm.setClients(new ArrayList<>());
        realm.setAccessTokenLifespan(3);
        realm.setSsoSessionMaxLifespan(3);
        realm.getClients().add(createServiceClient(TEST_SERVICE_CLIENT));
        return realm;
    }

    public static void createUser(String realm, UserRepresentation user) {
        adminClient.get().realm(realm)
                .users()
                .create(user);
    }

    public static void createClient(String realm, ClientRepresentation client) {
        adminClient.get().realm(realm)
                .clients()
                .create(client);
    }

    public static void createRealm(RealmRepresentation... realms) {
        Stream.of(realms)
                .forEach(realm -> adminClient.get().realms().create(realm));
    }

    public static void deleteRealms(String... realms) {
        Stream.of(realms)
                .filter(realm -> !KeycloakContainer.MASTER_REALM.equalsIgnoreCase(realm))
                .forEach(realm -> adminClient.get().realm(realm).remove());
    }

    private static String getAdminAccessToken() {
        return adminClient.get().tokenManager().getAccessToken().getToken();
    }

    private static RequestSpecification createRequestSpec() {
        return RestAssured.given();
    }
}
