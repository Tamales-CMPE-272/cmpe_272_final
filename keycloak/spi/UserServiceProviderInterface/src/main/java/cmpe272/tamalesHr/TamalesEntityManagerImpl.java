package cmpe272.tamalesHr;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Query;
import org.hibernate.jpa.HibernatePersistenceProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;

public class TamalesEntityManagerImpl implements TamalesEntityManager{

    private static final Logger log = LoggerFactory.getLogger(MySQLUserStorageProvider.class);

    private EntityManager entityManager;

    @Override
    public Query createNativeQuery(String var1) {
        return entityManager.createNativeQuery(var1);
    }

    @Override
    public TamalesEntityManager create() {
        try{
            log.info("⚠️  Trying to create entity manager");
            Properties props = new Properties();
            props.put("jakarta.persistence.jdbc.url", "jdbc:mysql://host.docker.internal:3306/employees");
            props.put("jakarta.persistence.jdbc.user", "root");
            props.put("jakarta.persistence.jdbc.password", "");
            props.put("jakarta.persistence.jdbc.driver", "com.mysql.cj.jdbc.Driver");
            props.put("hibernate.dialect", "org.hibernate.dialect.MySQLDialect");
            EntityManagerFactory emf = new HibernatePersistenceProvider()
                    .createEntityManagerFactory("user-store", props);
            if(emf == null){
                log.error("❌ EntityManagerFactory was not initialized");
            }
            entityManager = emf.createEntityManager();
            if(entityManager == null){
                log.error("❌ EntityManager  was not initialized");
            }
            log.info("✅ EntityManager was initialized{} database:{}", entityManager.getProperties(), entityManager.getMetamodel());
            return this;
        }catch(Exception e){
            log.error("❌Entity Manager was not created --> ", e);
            return null;
        }
    }

    @Override
    public Query createQuery(String query) {
        return entityManager.createQuery(query);
    }

    @Override
    public EntityTransaction getTransaction() {
        return entityManager.getTransaction();
    }
}
