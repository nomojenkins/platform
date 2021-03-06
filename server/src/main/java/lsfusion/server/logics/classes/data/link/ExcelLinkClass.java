package lsfusion.server.logics.classes.data.link;

import lsfusion.interop.classes.DataType;
import lsfusion.server.logics.classes.data.DataClass;

import java.util.ArrayList;
import java.util.Collection;

public class ExcelLinkClass extends StaticFormatLinkClass {

    protected String getFileSID() {
        return "EXCELLINK";
    }

    private static Collection<ExcelLinkClass> instances = new ArrayList<>();

    public static ExcelLinkClass get(boolean multiple) {
        for (ExcelLinkClass instance : instances)
            if (instance.multiple == multiple)
                return instance;

        ExcelLinkClass instance = new ExcelLinkClass(multiple);
        instances.add(instance);
        DataClass.storeClass(instance);
        return instance;
    }

    private ExcelLinkClass(boolean multiple) {
        super(multiple);
    }

    public byte getTypeID() {
        return DataType.EXCELLINK;
    }

    @Override
    public String getDefaultCastExtension() {
        return "xls";
    }
}