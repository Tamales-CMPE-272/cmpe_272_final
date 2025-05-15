package cmpe272.tamalesHr;

import org.keycloak.component.ComponentModel;

public interface TamalesComponentModel {
    TamalesComponentModel create(ComponentModel model);
    ComponentModel componentModel();
}

