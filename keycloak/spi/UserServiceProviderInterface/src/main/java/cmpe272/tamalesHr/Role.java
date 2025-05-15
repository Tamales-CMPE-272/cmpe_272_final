package cmpe272.tamalesHr;

public enum Role {
    MANAGER("Manager"),
    EMPLOYEE("Employee");

    private final String key;

    Role(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }
}
