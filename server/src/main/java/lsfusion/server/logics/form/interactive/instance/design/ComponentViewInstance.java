package lsfusion.server.logics.form.interactive.instance.design;

import lsfusion.interop.form.property.PropertyReadType;
import lsfusion.server.logics.form.interactive.design.ComponentView;
import lsfusion.server.logics.form.interactive.instance.CellInstance;
import lsfusion.server.logics.form.interactive.instance.property.PropertyObjectInstance;
import lsfusion.server.logics.form.interactive.instance.property.PropertyReaderInstance;

public class ComponentViewInstance<T extends ComponentView> extends CellInstance<T> {

    public final ComponentViewInstance.ElementClassReaderInstance elementClassReader;

    public final PropertyObjectInstance propertyElementClass;

    public ComponentViewInstance(T entity, PropertyObjectInstance propertyElementClass) {
        super(entity);

        this.propertyElementClass = propertyElementClass;
        this.elementClassReader = new ComponentViewInstance.ElementClassReaderInstance();
    }

    public class ElementClassReaderInstance implements PropertyReaderInstance {

        public PropertyObjectInstance getReaderProperty() {
            return propertyElementClass;
        }

        @Override
        public byte getTypeID() {
            return PropertyReadType.COMPONENT_ELEMENTCLASS;
        }

        @Override
        public int getID() {
            return ComponentViewInstance.this.getID();
        }
        @Override
        public Object getProfiledObject() {
            return null;
        }
    }

}
