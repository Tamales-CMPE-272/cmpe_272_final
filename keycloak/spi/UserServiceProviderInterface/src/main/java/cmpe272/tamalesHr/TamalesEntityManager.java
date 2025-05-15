package cmpe272.tamalesHr;

import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Query;

public interface TamalesEntityManager {
    TamalesEntityManager create();

    Query createNativeQuery(String var1);

    Query createQuery(String query);

    EntityTransaction getTransaction();
}
