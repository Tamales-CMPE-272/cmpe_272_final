package cmpe272.tamalesHr;

import org.keycloak.models.ClientModel;
import org.keycloak.models.GroupModel;
import org.keycloak.models.RoleModel;
import org.keycloak.models.SubjectCredentialManager;

import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

public class DummyUserModel implements org.keycloak.models.UserModel {
    private final String username;

    DummyUserModel(String username) {
        this.username = username;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public void setUsername(String s) {

    }

    @Override
    public Long getCreatedTimestamp() {
        return 0L;
    }

    @Override
    public void setCreatedTimestamp(Long aLong) {

    }

    @Override
    public boolean isEnabled() {
        return false;
    }

    @Override
    public void setEnabled(boolean b) {

    }

    @Override
    public void setSingleAttribute(String s, String s1) {

    }

    @Override
    public void setAttribute(String s, List<String> list) {

    }

    @Override
    public void removeAttribute(String s) {

    }

    @Override
    public String getFirstAttribute(String s) {
        return "";
    }

    @Override
    public Stream<String> getAttributeStream(String s) {
        return Stream.empty();
    }

    @Override
    public Map<String, List<String>> getAttributes() {
        return Map.of();
    }

    @Override
    public Stream<String> getRequiredActionsStream() {
        return Stream.empty();
    }

    @Override
    public void addRequiredAction(String s) {

    }

    @Override
    public void removeRequiredAction(String s) {

    }

    @Override
    public String getFirstName() {
        return "";
    }

    @Override
    public void setFirstName(String s) {

    }

    @Override
    public String getLastName() {
        return "";
    }

    @Override
    public void setLastName(String s) {

    }

    @Override
    public String getEmail() {
        return "";
    }

    @Override
    public void setEmail(String s) {

    }

    @Override
    public boolean isEmailVerified() {
        return false;
    }

    @Override
    public void setEmailVerified(boolean b) {

    }

    @Override
    public Stream<GroupModel> getGroupsStream() {
        return Stream.empty();
    }

    @Override
    public void joinGroup(GroupModel groupModel) {

    }

    @Override
    public void leaveGroup(GroupModel groupModel) {

    }

    @Override
    public boolean isMemberOf(GroupModel groupModel) {
        return false;
    }

    @Override
    public String getFederationLink() {
        return "";
    }

    @Override
    public void setFederationLink(String s) {

    }

    @Override
    public String getServiceAccountClientLink() {
        return "";
    }

    @Override
    public void setServiceAccountClientLink(String s) {

    }

    @Override
    public SubjectCredentialManager credentialManager() {
        return null;
    }

    // Implement minimal required methods returning dummy values
    @Override public String getId() { return username; }

    @Override
    public Stream<RoleModel> getRealmRoleMappingsStream() {
        return Stream.empty();
    }

    @Override
    public Stream<RoleModel> getClientRoleMappingsStream(ClientModel clientModel) {
        return Stream.empty();
    }

    @Override
    public boolean hasRole(RoleModel roleModel) {
        return false;
    }

    @Override
    public void grantRole(RoleModel roleModel) {

    }

    @Override
    public Stream<RoleModel> getRoleMappingsStream() {
        return Stream.empty();
    }

    @Override
    public void deleteRoleMapping(RoleModel roleModel) {

    }
    // Leave other methods throwing UnsupportedOperationException for brevity
}
