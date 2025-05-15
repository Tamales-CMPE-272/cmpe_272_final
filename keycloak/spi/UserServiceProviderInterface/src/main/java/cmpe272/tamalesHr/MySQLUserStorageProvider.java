package cmpe272.tamalesHr;


import jakarta.persistence.*;
import org.jetbrains.annotations.VisibleForTesting;
import org.keycloak.credential.CredentialInput;
import org.keycloak.credential.CredentialInputUpdater;
import org.keycloak.credential.CredentialInputValidator;
import org.keycloak.models.*;
import org.keycloak.models.credential.PasswordCredentialModel;

import org.keycloak.models.credential.PasswordUserCredentialModel;
import org.keycloak.storage.UserStorageProvider;

import org.keycloak.storage.user.UserLookupProvider;
import org.keycloak.storage.user.UserQueryProvider;
import org.keycloak.storage.user.UserRegistrationProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.*;
import java.util.stream.Stream;

public class MySQLUserStorageProvider implements UserStorageProvider,
        UserLookupProvider,
        UserQueryProvider,
        CredentialInputUpdater,
        CredentialInputValidator,
        UserRegistrationProvider {

    private final TamalesKeycloakSession session;
    private final TamalesComponentModel model;
    static final String KEY_USERNAME = "username";

    TamalesEntityManager entityManager;

    @VisibleForTesting
    public void setEntityManager(TamalesEntityManager entityManager) {
        this.entityManager = entityManager;
    }

    private static final Logger log = LoggerFactory.getLogger(MySQLUserStorageProvider.class);

    public MySQLUserStorageProvider(TamalesKeycloakSession session, TamalesComponentModel model) {
        this.session = session;
        this.model = model;
        this.entityManager = new TamalesEntityManagerImpl().create();
    }

    @Override
    public void close() {
        log.info("🔍close");
    }

    @Override
    public boolean supportsCredentialType(String credentialType) {
        log.info("🔍supportsCredentialType: credentialType{}", credentialType);
        return PasswordCredentialModel.TYPE.equals(credentialType);
    }

    @Override
    public boolean isConfiguredFor(RealmModel realm, UserModel user, String credentialType) {
        log.info("🔍isConfiguredFor: realm{} user{} credentialType{}", realm, user, credentialType);
        return supportsCredentialType(credentialType);
    }

    @Override
    public boolean isValid(RealmModel realm, UserModel user, CredentialInput input) {
        log.info("🔐 isValid: Validating credentials for user={} with type={}", user.getUsername(), input.getType());
        if (!supportsCredentialType(input.getType()) || !(input instanceof PasswordUserCredentialModel)) {
            log.error("❌ credential type not supported supportedCredentialType: {} input:{}",input.getType(), input );
            return false;
        }
        String rawPassword = input.getChallengeResponse();
        try {
            Object[] result = (Object[]) entityManager
                    .createNativeQuery("SELECT password_hash, salt FROM employee_passwords WHERE emp_no = :empNo")
                    .setParameter("empNo", Integer.parseInt(user.getUsername()))
                    .getSingleResult();

            String storedHash = (String) result[0];
            String salt = (String) result[1];

            String combined = salt + rawPassword;
            String computedHash = md5Hex(combined);

            boolean matches = storedHash.equalsIgnoreCase(computedHash);
            log.info("✅ Password match result for emp_no {}: {}", user.getUsername(), matches);

            return matches;
        } catch (Exception e) {
            log.error("❌ Error validating credentials for emp_no={}", user.getUsername(), e);
            return false;
        }
    }

    private String md5Hex(String input) throws Exception {
        MessageDigest md = MessageDigest.getInstance("MD5");
        byte[] digest = md.digest(input.getBytes(StandardCharsets.UTF_8));
        StringBuilder sb = new StringBuilder();
        for (byte b : digest) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }

    @Override
    public boolean updateCredential(RealmModel realm, UserModel user, CredentialInput input) {
        log.info("🔍disableCredentialType: realm{} user{} input{}", realm, user, input);
        return true;
    }

    @Override
    public void disableCredentialType(RealmModel realm, UserModel user, String credentialType) {
        log.info("🔍disableCredentialType: realm{} user{} credentialType{}", realm, user, credentialType);
    }

    @Override
    public Stream<String> getDisableableCredentialTypesStream(RealmModel realm, UserModel user) {
        log.info("🔍getDisableableCredentialTypesStream: realm{} user{}", realm, user);
        return Stream.empty();
    }

    @Override
    public UserModel getUserById(RealmModel realm, String id) {
        log.info("🔍getUserById, id: {}", id );
        String empNo = UUIDValidator.extractEmpNo(id);
        if(findByEmpNo(realm, id) == null){
            log.error("❌getUserById, not parsed");
            return null;
        }
        log.info("✅ empNo extracted for id, empNo: {}", empNo);
        return findByEmpNo(realm, empNo);
    }

    @Override
    public UserModel getUserByUsername(RealmModel realm, String username) {
        log.info(" 🔍getUserByEmpNo: username={} realm={}", username, realm);
        return findByEmpNo(realm, username);
    }

    @Override
    public UserModel getUserByEmail(RealmModel realm, String email) {
        log.info(" 🔍getUserByEmail: real={} email={}", realm, email);
        return null;
    }

    @Override
    public int getUsersCount(RealmModel realm) {
        log.info("🔍 getUsersCount: realm={}", realm);
        try {
            Long count = (Long) entityManager
                    .createQuery("SELECT COUNT(e) FROM employee_passwords e")
                    .getSingleResult();
            return count.intValue();
        } catch (Exception e) {
            log.error("❌Error counting users in employee_passwords table", e);
            return 0;
        }
    }

    @Override
    public Stream<UserModel> searchForUserStream(RealmModel realm, Map<String, String> params, Integer firstResult, Integer maxResults) {
        log.info("🔍searchForUserStream: params={}, first={}, max={}", params, firstResult, maxResults);

        try {
            if(!params.containsKey(KEY_USERNAME)){
                log.info("Result --> 🔍No username passed");
                return Stream.empty();
            }

            String baseQuery =
                    "SELECT e.emp_no, e.first_name, e.last_name " +
                            "FROM employees e " +
                            "JOIN employee_passwords p ON e.emp_no = p.emp_no";

            boolean filterByUsername = params.containsKey(KEY_USERNAME);
            if (filterByUsername) {
                baseQuery += " WHERE e.emp_no = :empNo";
            }

            Query query = entityManager.createNativeQuery(baseQuery);

            if (filterByUsername) {
                query.setParameter("empNo", Integer.parseInt(params.get(KEY_USERNAME)));
            }

            @SuppressWarnings("unchecked")
            List<Object[]> results = query.getResultList();

            log.info("🔍 Found {} user(s)", results.size());

            return results.stream()
                    .map(row -> {
                        String empNo = row[0].toString();
                        String firstName = row[1].toString();
                        String lastName = row[2].toString();
                        // Determine role based on presence in dept_manager table
                        Role role = isManager(empNo) ? Role.MANAGER : Role.EMPLOYEE;
                        log.info("👤 Mapping emp_no={} firstName={} lastName={} role={}", empNo, firstName, lastName, role);
                        return createAdapter(realm, empNo, firstName, lastName, role);
                    });
        } catch (Exception e) {
            log.error("❌ Error in searchForUserStream: {}", e.getMessage(), e);
            return Stream.empty();
        }
    }


    private UserModel findByEmpNo(RealmModel realm, String empNoParam) {
        try {
            if (empNoParam.contains("@")){
                log.error("❌ invalid emp no, empNo: {}", empNoParam);
               throw new NoResultException("empNoParam is an email or id");
            }
            String param = empNoParam;
            if(UUIDValidator.isValid(empNoParam)){
                param = UUIDValidator.extractEmpNo(empNoParam);
            }
            Object[] result = (Object[]) entityManager
                    .createNativeQuery("SELECT e.emp_no, e.first_name, e.last_name " +
                            "FROM employees e " +
                            "JOIN employee_passwords p ON e.emp_no = p.emp_no " +
                            "WHERE e.emp_no = :empNo")
                    .setParameter("empNo", Integer.parseInt(param == null ? "" : param))
                    .getSingleResult();

            String empNo = result[0].toString();
            String firstName = result[1].toString();
            String lastName = result[2].toString();

            Role role = isManager(empNo) ? Role.MANAGER : Role.EMPLOYEE;

            log.info("✅ Found user --> emp no:{} firstName:{} lastName:{} role:{}", empNo, firstName, lastName, role);
            return createAdapter(realm, empNo, firstName, lastName, role);
        } catch (NoResultException e) {
            log.info("No user '{}' found in employee_passwords", empNoParam);
            return null;
        } catch (Exception e) {
            log.error("❌ Error querying employee_passwords", e);
            return null;
        }
    }

    // Helper method to check if the employee is a manager
    private boolean isManager(String empNo) {
        try {
            long count = ((Number) entityManager
                    .createNativeQuery("SELECT COUNT(*) FROM dept_manager WHERE emp_no = :empNo AND to_date = '9999-01-01'")
                    .setParameter("empNo", Integer.parseInt(empNo))
                    .getSingleResult()).longValue();
            return count > 0;
        } catch (Exception e) {
            log.error("❌ Error checking manager status for emp_no={}", empNo, e);
            return false;
        }
    }

    @Override
    public Stream<UserModel> getGroupMembersStream(RealmModel realm, GroupModel group, Integer firstResult, Integer maxResults) {
        log.info("🔍 getGroupMembersStream: realm={}, group={}, first={}, max={}", realm, group ,firstResult, maxResults);
        return Stream.empty();
    }

    @Override
    public Stream<UserModel> searchForUserByUserAttributeStream(RealmModel realm, String attrName, String attrValue) {
        log.info("🔍 searchForUserByUserAttributeStream: realm={}, attrName={}, attrValue={}", realm, attrName, attrValue);
        return Stream.empty();
    }

    @Override
    public boolean removeUser(RealmModel realm, UserModel user) {
        String name = "";
        if(realm != null){
            name = realm.getName();
        }
        log.info("🗑️ removeUser: realm={}, user={}",name, user.getUsername());
        try {
            String empNo = UUIDValidator.extractEmpNo(user.getId());

            entityManager.getTransaction().begin();

            // Delete from employee_passwords
            entityManager.createNativeQuery("DELETE FROM employee_passwords WHERE emp_no = ?")
                    .setParameter(1, empNo)
                    .executeUpdate();

            // Delete from dept_emp
            entityManager.createNativeQuery("DELETE FROM dept_emp WHERE emp_no = ?")
                    .setParameter(1, empNo)
                    .executeUpdate();

            // Delete from dept_manager
            entityManager.createNativeQuery("DELETE FROM dept_manager WHERE emp_no = ?")
                    .setParameter(1, empNo)
                    .executeUpdate();

            // Delete from salaries
            entityManager.createNativeQuery("DELETE FROM salaries WHERE emp_no = ?")
                    .setParameter(1, empNo)
                    .executeUpdate();

            // Delete from titles
            entityManager.createNativeQuery("DELETE FROM titles WHERE emp_no = ?")
                    .setParameter(1, empNo)
                    .executeUpdate();

            // Finally, delete from employees
            int deletedCount = entityManager.createNativeQuery("DELETE FROM employees WHERE emp_no = ?")
                    .setParameter(1, empNo)
                    .executeUpdate();

            entityManager.getTransaction().commit();

            if (deletedCount > 0) {
                log.info("✅ Successfully removed user and related records for emp_no={}", empNo);
                return true;
            } else {
                log.info("⚠️ No employee found with emp_no={}", empNo);
                return false;
            }
        } catch (Exception e) {
            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }
            log.error("❌ Error removing user", e);
            return false;
        }
    }

    @Override
    public UserModel addUser(RealmModel realm, String username) {
        String name = "";
        if(realm != null){
            name = realm.getName();
        }
        log.info("🔍addUser: realm={}, username={}",name, username);
        try {
            long empNo = Long.parseLong(username);

            entityManager.getTransaction().begin();

            // Check if employee exists
            Long empCount = ((Number) entityManager
                    .createNativeQuery("SELECT COUNT(*) FROM employees WHERE emp_no = ?")
                    .setParameter(1, empNo)
                    .getSingleResult()).longValue();

            if (empCount == 0) {
                log.info("ℹ️ emp_no={} does not exist in employees table, inserting default employee", empNo);

                // Insert into employees
                entityManager.createNativeQuery("INSERT INTO employees (emp_no, birth_date, first_name, last_name, gender, hire_date) " +
                                "VALUES (?, ?, ?, ?, ?, ?)")
                        .setParameter(1, empNo)
                        .setParameter(2, java.sql.Date.valueOf("1990-01-01"))
                        .setParameter(3, "Default")
                        .setParameter(4, "User")
                        .setParameter(5, "M") // or "F"
                        .setParameter(6, java.sql.Date.valueOf("2020-01-01"))
                        .executeUpdate();

                // Insert into dept_emp
                entityManager.createNativeQuery("INSERT INTO dept_emp (emp_no, dept_no, from_date, to_date) VALUES (?, ?, ?, ?)")
                        .setParameter(1, empNo)
                        .setParameter(2, "d001") // Default department number
                        .setParameter(3, java.sql.Date.valueOf("2020-01-01"))
                        .setParameter(4, java.sql.Date.valueOf("2024-01-01"))
                        .executeUpdate();

                // Insert into salaries
                entityManager.createNativeQuery("INSERT INTO salaries (emp_no, salary, from_date, to_date) VALUES (?, ?, ?, ?)")
                        .setParameter(1, empNo)
                        .setParameter(2, 50000) // Default salary
                        .setParameter(3, java.sql.Date.valueOf("2020-01-01"))
                        .setParameter(4, java.sql.Date.valueOf("2024-01-01"))
                        .executeUpdate();

                // Insert into titles
                entityManager.createNativeQuery("INSERT INTO titles (emp_no, title, from_date, to_date) VALUES (?, ?, ?, ?)")
                        .setParameter(1, empNo)
                        .setParameter(2, "Staff") // Default title
                        .setParameter(3, java.sql.Date.valueOf("2020-01-01"))
                        .setParameter(4, java.sql.Date.valueOf("9999-01-01"))
                        .executeUpdate();

                String salt = "some_static_salt";
                String hash = md5Hex(salt + "Password@123");

                entityManager.createNativeQuery("INSERT INTO employee_passwords (emp_no, password_hash, salt) VALUES (?, ?, ?)")
                        .setParameter(1, empNo)
                        .setParameter(2, hash)
                        .setParameter(3, salt)
                        .executeUpdate();
            } else {
                log.info("ℹ️ emp_no={} already existis in employee table", empNo);
            }
            entityManager.getTransaction().commit();

            log.info("✅ User added to employees and related tables: {}", empNo);
            return findByEmpNo(realm, username);
        } catch (Exception e) {
            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }
            log.error("❌ Error adding user", e);
            return null;
        }
    }

    private UserModel createAdapter(
            RealmModel realm,
            String empNo,
            String firstName,
            String lastName,
            Role role
    ) {
        log.info("User creation: {} : {}: {}: {}: {}", realm, empNo, firstName, lastName, role);
        return new EmployeePasswordModel(session, realm, model, empNo, firstName, lastName, role);
    }
}