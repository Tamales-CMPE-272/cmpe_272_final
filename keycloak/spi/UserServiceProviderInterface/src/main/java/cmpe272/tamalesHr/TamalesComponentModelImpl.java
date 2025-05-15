package cmpe272.tamalesHr;

import org.keycloak.component.ComponentModel;

public class TamalesComponentModelImpl implements TamalesComponentModel{
    ComponentModel model;

    @Override
    public TamalesComponentModel create(ComponentModel model) {
        this.model = model;
        return this;
    }

    @Override
    public ComponentModel componentModel() {
        return this.model;
    }
}
