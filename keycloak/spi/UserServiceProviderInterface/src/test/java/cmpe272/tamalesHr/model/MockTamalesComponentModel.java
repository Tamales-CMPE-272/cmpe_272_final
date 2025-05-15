package cmpe272.tamalesHr.model;

import cmpe272.tamalesHr.TamalesComponentModel;
import org.keycloak.component.ComponentModel;

public class MockTamalesComponentModel implements TamalesComponentModel {
    @Override
    public TamalesComponentModel create(ComponentModel model) {
        return null;
    }

    @Override
    public ComponentModel componentModel() {
        return null;
    }
}
