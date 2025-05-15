package cmpe272.tamalesHr;

import org.keycloak.models.KeycloakSession;

public interface TamalesKeycloakSession {
    TamalesKeycloakSession create(KeycloakSession session);
    KeycloakSession keycloakSession();
}
