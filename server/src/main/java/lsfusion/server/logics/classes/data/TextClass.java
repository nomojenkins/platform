package lsfusion.server.logics.classes.data;

import lsfusion.interop.base.view.FlexAlignment;
import lsfusion.interop.classes.DataType;
import lsfusion.interop.form.property.ExtInt;
import lsfusion.server.logics.classes.ValueClass;
import lsfusion.server.logics.form.interactive.controller.remote.serialization.FormInstanceContext;
import lsfusion.server.physics.dev.i18n.LocalizedString;

import java.io.DataOutputStream;
import java.io.IOException;

public class TextClass extends StringClass {

    public final static StringClass instance = new TextClass(null);

    public TextClass(String type) {
        super(LocalizedString.create("{classes.text}" + (type != null ? (" " + type) : "")), false, ExtInt.UNLIMITED, true);
    }

    public static StringClass getInstance(String type) {
        switch (type) {
            case "HTMLTEXT":
                return HTMLTextClass.instance;
            case "RICHTEXT":
                return RichTextClass.instance;
            default:
                return instance;
        }
    }

    @Override
    public byte getTypeID() {
        return DataType.TEXT;
    }

    @Override
    public void serialize(DataOutputStream outStream) throws IOException {
        outStream.writeByte(getTypeID());
    }

    @Override
    public DataClass getCompatible(DataClass compClass, boolean or) {
            return compClass instanceof StringClass ? this : super.getCompatible(compClass, or);
    }

    @Override
    public ValueClass getFilterMatchValueClass() {
        return this;
    }

    @Override
    public String getSID() {
        return "TEXT";
    }

    @Override
    public String getInputType(FormInstanceContext context) {
        return "textarea";
    }

    @Override
    public FlexAlignment getValueAlignmentVert() {
        return FlexAlignment.START;
    }
}
