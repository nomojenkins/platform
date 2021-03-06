package lsfusion.server.logics.classes.data.link;

import lsfusion.interop.classes.DataType;
import lsfusion.server.logics.classes.data.DataClass;

import java.util.ArrayList;
import java.util.Collection;

public class CSVLinkClass extends StaticFormatLinkClass {

    protected String getFileSID() {
        return "CSVLINK";
    }

    private static Collection<CSVLinkClass> instances = new ArrayList<>();

    public static CSVLinkClass get(boolean multiple) {
        for (CSVLinkClass instance : instances)
            if (instance.multiple == multiple)
                return instance;

        CSVLinkClass instance = new CSVLinkClass(multiple);
        instances.add(instance);
        DataClass.storeClass(instance);
        return instance;
    }

    private CSVLinkClass(boolean multiple) {
        super(multiple);
    }

    public byte getTypeID() {
        return DataType.CSVLINK;
    }

    @Override
    public String getDefaultCastExtension() {
        return "csv";
    }
}