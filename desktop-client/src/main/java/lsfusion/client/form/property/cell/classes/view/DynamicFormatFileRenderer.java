package lsfusion.client.form.property.cell.classes.view;

import lsfusion.base.file.FileData;
import lsfusion.client.base.SwingUtils;
import lsfusion.client.form.property.ClientPropertyDraw;

public class DynamicFormatFileRenderer extends FilePropertyRenderer {

    public DynamicFormatFileRenderer(ClientPropertyDraw property) {
        super(property);
    }

    public void setValue(Object value) {
        super.setValue(value);
        
        if (value != null) {
            FileData fileData = (FileData) value;
            getComponent().setIcon(SwingUtils.getSystemIcon(fileData.getExtension()));
        }
    }
}