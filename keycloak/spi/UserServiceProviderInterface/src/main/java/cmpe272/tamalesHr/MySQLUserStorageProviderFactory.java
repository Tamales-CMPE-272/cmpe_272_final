package cmpe272.tamalesHr;

import org.keycloak.component.ComponentModel;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.RealmModel;
import org.keycloak.storage.UserStorageProviderFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MySQLUserStorageProviderFactory implements UserStorageProviderFactory<MySQLUserStorageProvider> {

    public static final String PROVIDER_ID = "mysql-user_store";
    private static final Logger log = LoggerFactory.getLogger(MySQLUserStorageProviderFactory.class);
    @Override
    public MySQLUserStorageProvider create(KeycloakSession session, ComponentModel model) {
        TamalesKeycloakSession tamalesKeycloakSession = new TamalesKeycloakSessionImpl();
        tamalesKeycloakSession.create(session);
        TamalesComponentModel tamalesComponentModel = new TamalesComponentModelImpl();
        tamalesComponentModel.create(model);
        return new MySQLUserStorageProvider(tamalesKeycloakSession, tamalesComponentModel);
    }

    @Override
    public void onCreate(KeycloakSession session, RealmModel realm, ComponentModel model) {
        log.info("<<DEBUG>> onCreate");
        UserStorageProviderFactory.super.onCreate(session, realm, model);
    }

    @Override
    public String getId() {
        return PROVIDER_ID;
    }

    @Override
    public void close() {}

    @Override
    public String getHelpText() {
        return "Custom user storage provider using raw JDBC.";
    }
}