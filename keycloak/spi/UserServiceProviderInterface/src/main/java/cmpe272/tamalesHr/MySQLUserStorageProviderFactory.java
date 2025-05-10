package cmpe272.tamalesHr;

import com.mysql.cj.jdbc.MysqlXADataSource;
import jakarta.enterprise.inject.spi.CDI;
import org.jboss.jandex.DotName;
import org.keycloak.component.ComponentModel;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.RealmModel;
import org.keycloak.storage.UserStorageProviderFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;

public class MySQLUserStorageProviderFactory implements UserStorageProviderFactory<MySQLUserStorageProvider> {

    public static final String PROVIDER_ID = "mysql-user_store";
    private static final Logger log = LoggerFactory.getLogger(MySQLUserStorageProviderFactory.class);
    @Override
    public MySQLUserStorageProvider create(KeycloakSession session, ComponentModel model) {
        return new MySQLUserStorageProvider(session, model);
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