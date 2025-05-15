package cmpe272.tamalesHr;

import org.keycloak.models.KeycloakSession;

public class TamalesKeycloakSessionImpl implements TamalesKeycloakSession{
    private KeycloakSession session;

    @Override
    public TamalesKeycloakSession create(KeycloakSession session) {
        this.session = session;
        return this;
    }

    @Override
    public KeycloakSession keycloakSession() {
        return this.session;
    }
}
