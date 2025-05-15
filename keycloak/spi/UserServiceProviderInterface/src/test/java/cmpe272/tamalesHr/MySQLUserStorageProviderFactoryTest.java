package cmpe272.tamalesHr;

import cmpe272.tamalesHr.MySQLUserStorageProvider;
import cmpe272.tamalesHr.MySQLUserStorageProviderFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.keycloak.component.ComponentModel;
import org.keycloak.models.KeycloakSession;

import static com.google.common.truth.Truth.assertThat;

class MySQLUserStorageProviderFactoryTest {

    private MySQLUserStorageProviderFactory factory;

    @BeforeEach
    void setup() {
        factory = new MySQLUserStorageProviderFactory();
    }

    @Test
    void testGetId() {
        assertThat(factory.getId()).isEqualTo("mysql-user_store");
    }

    @Test
    void testGetHelpText() {
        String helpText = factory.getHelpText();
        assertThat(helpText).contains("Custom user storage provider using raw JDBC");
    }

    @Test
    void testCreateReturnsProvider() {
        KeycloakSession session = null;  // Simplified, can be null for unit test
        ComponentModel model = ComponentModel.class.cast(null); // null or a dummy object if needed

        MySQLUserStorageProvider provider = factory.create(session, model);

        assertThat(provider).isNotNull();
        assertThat(provider).isInstanceOf(MySQLUserStorageProvider.class);
    }

    @Test
    void testOnCreateLogs() {
        // This will just trigger log, no effect but can be called
        factory.onCreate(null, null, null);

        // No exception = success (real logging would need a log-capture test library like LogCaptor)
        assertThat(true).isTrue(); // Placeholder assertion
    }

    @Test
    void testCloseDoesNotFail() {
        factory.close();

        // Should not throw any exception
        assertThat(true).isTrue(); // Placeholder assertion
    }
}
