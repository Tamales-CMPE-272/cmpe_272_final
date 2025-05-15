package cmpe272.tamalesHr;

import cmpe272.tamalesHr.model.FakeTamalesEntityManager;
import cmpe272.tamalesHr.model.MockTamalesComponentModel;
import cmpe272.tamalesHr.model.MockTamalesKeycloakSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.keycloak.models.credential.PasswordUserCredentialModel;

import static com.google.common.truth.Truth.assertThat;

public class MySQLUserStorageProviderTest {

    TamalesKeycloakSession session;
    TamalesComponentModel model;
    private MySQLUserStorageProvider provider;

    @BeforeEach
    void setUp() {
        TamalesKeycloakSession session = new MockTamalesKeycloakSession();
        TamalesComponentModel model = new MockTamalesComponentModel();
        provider = new MySQLUserStorageProvider(session, model);

        // Inject Fake TamalesEntityManager
        provider.setEntityManager(new FakeTamalesEntityManager());
    }
    @Test
    void testSupportsCredentialType() {
        assertThat(provider.supportsCredentialType("password")).isTrue();
        assertThat(provider.supportsCredentialType("otp")).isFalse();
    }


    @Test
    void testIsValid_correctPassword() {
        var user = new DummyUserModel("123");
        var credential = PasswordUserCredentialModel.password("password");

        boolean result = provider.isValid(null, user, credential);

        assertThat(result).isFalse();
    }

    @Test
    void testIsValid_wrongPassword() {
        var user = new DummyUserModel("123");
        var credential = PasswordUserCredentialModel.password("wrongPassword");

        boolean result = provider.isValid(null, user, credential);

        assertThat(result).isFalse();
    }

    @Test
    void testAddUser() {
        var result = provider.addUser(null, "124");

        assertThat(result).isNotNull();
        assertThat(result.getUsername()).isEqualTo("1234");
    }

    @Test
    void testRemoveUser() {
        var user = new DummyUserModel("123");
        boolean result = provider.removeUser(null, user);

        assertThat(result).isTrue();
    }

    @Test
    void testGetUsersCount() {
        int count = provider.getUsersCount(null);

        assertThat(count).isEqualTo(1);
    }

    @Test
    void testSearchForUserStream() {
        var params = new java.util.HashMap<String, String>();
        params.put("username", "123");

        long userCount = provider.searchForUserStream(null, params, 0, 10).count();

        assertThat(userCount).isEqualTo(1);
    }


}
