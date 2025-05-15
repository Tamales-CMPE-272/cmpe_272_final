package cmpe272.tamalesHr;

import jakarta.ws.rs.core.MultivaluedHashMap;
import org.keycloak.models.*;
import org.keycloak.storage.StorageId;
import org.keycloak.credential.UserCredentialManager;
import org.keycloak.storage.UserStorageUtil;
import org.keycloak.storage.adapter.AbstractUserAdapter;
import org.keycloak.storage.federated.UserFederatedStorageProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;

public class EmployeePasswordModel extends AbstractUserAdapter {
    private static final Logger log = LoggerFactory.getLogger(EmployeePasswordModel.class);
    private String emp_no;
    private String firstName;
    private String lastName;
    private Role role;

    public EmployeePasswordModel(
            TamalesKeycloakSession session,
            RealmModel realm,
            TamalesComponentModel storageProviderModel,
            String emp_no,
            String lastName,
            String firstName,
            Role role
    ) {
        super(session.keycloakSession(), realm, storageProviderModel.componentModel());
        this.emp_no = emp_no;
        this.lastName = lastName;
        this.firstName = firstName;
        this.role = role;
    }


    @Override
    public String getUsername() {
        return emp_no;
    }

    @Override
    public String getFirstName() {
        return firstName;
    }

    @Override
    public String getLastName() {
        return lastName;
    }

    @Override
    public String getEmail() {
        return firstName+"_"+lastName+"@gmail.com";
    }

    @Override
    public SubjectCredentialManager credentialManager() {
        return new UserCredentialManager(session, realm, this);
    }

    @Override
    public String getFirstAttribute(String name) {
        List<String> list = getAttributes().getOrDefault(name, List.of());
        return list.isEmpty() ? null : list.get(0);
    }

    @Override
    public String toString() {
        return this.firstName + " " + this.lastName + " (" + this.emp_no + ")";
    }

    @Override
    public Map<String, List<String>> getAttributes() {
        MultivaluedHashMap<String, String> attributes = new MultivaluedHashMap<>();
        attributes.add(UserModel.USERNAME, getUsername());
        attributes.add(UserModel.EMAIL, getEmail());
        attributes.add(UserModel.FIRST_NAME, getFirstName());
        attributes.add(UserModel.LAST_NAME, getLastName());
        return attributes;
    }

    @Override
    public Stream<String> getAttributeStream(String name) {
        Map<String, List<String>> attributes = getAttributes();
        return (attributes.containsKey(name)) ? attributes.get(name).stream() : Stream.empty();
    }

    @Override
    protected Set<GroupModel> getGroupsInternal() {
        return Set.of();
    }

    @Override
    protected Set<RoleModel> getRoleMappingsInternal() {
        Set<RoleModel> roles = new HashSet<>();
        if (this.role == null) {
           return Set.of();
        }

        RoleModel roleModel = realm.getRole(this.role.getKey());
        if (roleModel != null) {
            roles.add(roleModel);
        } else {
            log.error("❌ Role '{}' not found in realm '{}'", this.role.getKey(), realm.getName());
        }

        // Add client role (assuming client ID is the same as role name, or hardcoded)
        ClientModel client = realm.getClientByClientId("tamalesHr-rest-api");
        if (client != null) {
            RoleModel clientRole = client.getRole(this.role.getKey());
            if (clientRole != null) {
                roles.add(clientRole);
            }
        }
        return roles;
    }

    @Override
    public Stream<String> getRequiredActionsStream() {
        return getFederatedStorage().getRequiredActionsStream(realm, this.getId());
    }

    @Override
    public void addRequiredAction(String action) {
        getFederatedStorage().addRequiredAction(realm, this.getId(), action);
    }

    @Override
    public void removeRequiredAction(String action) {
        getFederatedStorage().removeRequiredAction(realm, this.getId(), action);
    }

    @Override
    public void addRequiredAction(RequiredAction action) {
        getFederatedStorage().addRequiredAction(realm, this.getId(), action.name());
    }

    @Override
    public void removeRequiredAction(RequiredAction action) {
        getFederatedStorage().removeRequiredAction(realm, this.getId(), action.name());
    }


    @Override
    public void setAttribute(String name, List<String> values) {
        // intended, see commit message
    }

    @Override
    public void setFirstName(String firstName) {
        // intended, see commit message
    }

    @Override
    public void setLastName(String lastName) {
        // intended, see commit message
    }

    @Override
    public void setEmail(String email) {
        // intended, see commit message
    }

    @Override
    public void setEmailVerified(boolean verified) {
        // intended, see commit message
    }

    @Override
    public void setEnabled(boolean enabled) {
        // intended, see commit message
    }

    @Override
    public void setCreatedTimestamp(Long timestamp) {
        // intended, see commit message
    }

    @Override
    public void setSingleAttribute(String name, String value) {
        // intended, see commit message
    }

    @Override
    public void removeAttribute(String name) {
        // intended, see commit message
    }

    @Override
    public String getId() {
        return StorageId.keycloakId(storageProviderModel, emp_no);
    }

    UserFederatedStorageProvider getFederatedStorage() {
        return UserStorageUtil.userFederatedStorage(session);
    }
}