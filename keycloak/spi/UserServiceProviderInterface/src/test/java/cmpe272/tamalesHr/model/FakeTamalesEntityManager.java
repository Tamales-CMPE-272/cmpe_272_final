package cmpe272.tamalesHr.model;

import cmpe272.tamalesHr.TamalesEntityManager;
import jakarta.persistence.*;

import java.util.*;
import java.util.stream.Stream;

public class FakeTamalesEntityManager implements TamalesEntityManager {

    @Override
    public TamalesEntityManager create() {
        return this;
    }

    @Override
    public Query createNativeQuery(String query) {
        return new FakeQuery(query);
    }

    @Override
    public Query createQuery(String query) {
        if (query.startsWith("SELECT COUNT")) {
            return new FakeCountQuery();
        }
        throw new UnsupportedOperationException("createQuery not used in this test");
    }

    @Override
    public EntityTransaction getTransaction() {
        return new FakeTransaction();
    }

    static class FakeQuery implements Query {
        private String query;
        private int empNo;

        FakeQuery(String query) {
            this.query = query;
        }

        @Override
        public Query setParameter(String name, Object value) {
            if ("empNo".equals(name)) {
                empNo = Integer.parseInt(value.toString());
            }
            return this;
        }

        @Override
        public Query setParameter(String s, Calendar calendar, TemporalType temporalType) {
            return null;
        }

        @Override
        public Query setParameter(String s, Date date, TemporalType temporalType) {
            return null;
        }

        @Override
        public Query setParameter(int position, Object value) {
            if(value != null){
                empNo = Integer.parseInt(value.toString());
            }
            return this;
        }

        @Override
        public Query setParameter(int i, Calendar calendar, TemporalType temporalType) {
            return null;
        }

        @Override
        public Query setParameter(int i, Date date, TemporalType temporalType) {
            return null;
        }

        @Override
        public Set<Parameter<?>> getParameters() {
            return Set.of();
        }

        @Override
        public Parameter<?> getParameter(String s) {
            return null;
        }

        @Override
        public <T> Parameter<T> getParameter(String s, Class<T> aClass) {
            return null;
        }

        @Override
        public Parameter<?> getParameter(int i) {
            return null;
        }

        @Override
        public <T> Parameter<T> getParameter(int i, Class<T> aClass) {
            return null;
        }

        @Override
        public boolean isBound(Parameter<?> parameter) {
            return false;
        }

        @Override
        public <T> T getParameterValue(Parameter<T> parameter) {
            return null;
        }

        @Override
        public Object getParameterValue(String s) {
            return null;
        }

        @Override
        public Object getParameterValue(int i) {
            return null;
        }

        @Override
        public Query setFlushMode(FlushModeType flushModeType) {
            return null;
        }

        @Override
        public FlushModeType getFlushMode() {
            return null;
        }

        @Override
        public Query setLockMode(LockModeType lockModeType) {
            return null;
        }

        @Override
        public LockModeType getLockMode() {
            return null;
        }

        @Override
        public Object getSingleResult() {
            if (query.contains("employee_passwords")) {
                return new Object[]{"1234", "salt", "salt2"};
            }
            if (query.contains("dept_manager")) {
                return 1L; // Not a manager
            }
            if (query.contains("employees")) {
                return 1L; // Used for addUser check
            }
            return null;
        }

        @Override
        public List getResultList() {
            List<Object[]> results = new ArrayList<>();
            results.add(new Object[]{"123", "John", "Doe"});
            return results;
        }

        @Override public int executeUpdate() { return 1; }

        // Stub implementations
        @Override public Query setMaxResults(int maxResult) { return this; }

        @Override
        public int getMaxResults() {
            return 0;
        }

        @Override public Query setFirstResult(int startPosition) { return this; }

        @Override
        public int getFirstResult() {
            return 0;
        }

        @Override public Query setHint(String hintName, Object value) { return this; }

        @Override
        public Map<String, Object> getHints() {
            return Map.of();
        }

        @Override
        public <T> Query setParameter(Parameter<T> parameter, T t) {
            return null;
        }

        @Override
        public Query setParameter(Parameter<Calendar> parameter, Calendar calendar, TemporalType temporalType) {
            return null;
        }

        @Override
        public Query setParameter(Parameter<Date> parameter, Date date, TemporalType temporalType) {
            return null;
        }

        @Override public <T> T unwrap(Class<T> cls) { return null; }
        @Override public Stream getResultStream() { return null; }
    }

    static class FakeCountQuery implements Query {
        @Override public Object getSingleResult() { return 1L; }

        // Stub implementations
        @Override public Query setParameter(String name, Object value) { return this; }

        @Override
        public Query setParameter(String s, Calendar calendar, TemporalType temporalType) {
            return null;
        }

        @Override
        public Query setParameter(String s, Date date, TemporalType temporalType) {
            return null;
        }

        @Override public Query setParameter(int position, Object value) { return this; }

        @Override
        public Query setParameter(int i, Calendar calendar, TemporalType temporalType) {
            return null;
        }

        @Override
        public Query setParameter(int i, Date date, TemporalType temporalType) {
            return null;
        }

        @Override
        public Set<Parameter<?>> getParameters() {
            return Set.of();
        }

        @Override
        public Parameter<?> getParameter(String s) {
            return null;
        }

        @Override
        public <T> Parameter<T> getParameter(String s, Class<T> aClass) {
            return null;
        }

        @Override
        public Parameter<?> getParameter(int i) {
            return null;
        }

        @Override
        public <T> Parameter<T> getParameter(int i, Class<T> aClass) {
            return null;
        }

        @Override
        public boolean isBound(Parameter<?> parameter) {
            return false;
        }

        @Override
        public <T> T getParameterValue(Parameter<T> parameter) {
            return null;
        }

        @Override
        public Object getParameterValue(String s) {
            return null;
        }

        @Override
        public Object getParameterValue(int i) {
            return null;
        }

        @Override
        public Query setFlushMode(FlushModeType flushModeType) {
            return null;
        }

        @Override
        public FlushModeType getFlushMode() {
            return null;
        }

        @Override
        public Query setLockMode(LockModeType lockModeType) {
            return null;
        }

        @Override
        public LockModeType getLockMode() {
            return null;
        }

        @Override public int executeUpdate() { return 0; }
        @Override public Query setMaxResults(int maxResult) { return this; }

        @Override
        public int getMaxResults() {
            return 0;
        }

        @Override public Query setFirstResult(int startPosition) { return this; }

        @Override
        public int getFirstResult() {
            return 0;
        }

        @Override public Query setHint(String hintName, Object value) { return this; }

        @Override
        public Map<String, Object> getHints() {
            return Map.of();
        }

        @Override
        public <T> Query setParameter(Parameter<T> parameter, T t) {
            return null;
        }

        @Override
        public Query setParameter(Parameter<Calendar> parameter, Calendar calendar, TemporalType temporalType) {
            return null;
        }

        @Override
        public Query setParameter(Parameter<Date> parameter, Date date, TemporalType temporalType) {
            return null;
        }

        @Override public <T> T unwrap(Class<T> cls) { return null; }
        @Override public List getResultList() { return null; }
        @Override public Stream getResultStream() { return null; }
    }

    static class FakeTransaction implements EntityTransaction {
        @Override public void begin() {}
        @Override public void commit() {}
        @Override public void rollback() {}
        @Override public void setRollbackOnly() {}
        @Override public boolean getRollbackOnly() { return false; }
        @Override public boolean isActive() { return false; }
    }
}
