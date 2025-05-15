package cmpe272.tamalesHr.model;

import cmpe272.tamalesHr.TamalesKeycloakSession;
import org.keycloak.models.KeycloakSession;

public class MockTamalesKeycloakSession implements TamalesKeycloakSession {
    @Override
    public TamalesKeycloakSession create(KeycloakSession session) {
        return null;
    }

    @Override
    public KeycloakSession keycloakSession() {
        return null;
    }
}
