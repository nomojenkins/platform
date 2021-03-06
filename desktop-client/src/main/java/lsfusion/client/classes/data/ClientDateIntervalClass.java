package lsfusion.client.classes.data;

import lsfusion.client.form.property.ClientPropertyDraw;
import lsfusion.client.form.property.cell.classes.controller.IntervalPropertyEditor;
import lsfusion.client.form.property.cell.classes.controller.PropertyEditor;
import lsfusion.client.view.MainFrame;
import lsfusion.interop.classes.DataType;

import java.math.BigDecimal;
import java.text.FieldPosition;
import java.text.Format;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;

public class ClientDateIntervalClass extends ClientIntervalClass {

    public final static ClientIntervalClass instance = new ClientDateIntervalClass();

    @Override
    public byte getTypeId() {
        return DataType.DATEINTERVAL;
    }

    @Override
    public String formatString(Object obj) throws ParseException {
        return getDateIntervalDefaultFormat(obj).toString();
    }

    @Override
    protected PropertyEditor getDataClassEditorComponent(Object value, ClientPropertyDraw property) {
        return new IntervalPropertyEditor(value, (SimpleDateFormat) MainFrame.dateFormat, true);
    }

    @Override
    public Format getDefaultFormat() {
        return MainFrame.dateIntervalFormat;
    }

    @Override
    public String getIntervalType() {
        return "DATE";
    }

    public static class DateIntervalFormat extends Format {
        @Override
        public StringBuffer format(Object o, StringBuffer stringBuffer, FieldPosition fieldPosition) {
            if (o instanceof BigDecimal) {
                return getDateIntervalDefaultFormat(o);
            }
            return null;
        }

        @Override
        public Object parseObject(String s, ParsePosition parsePosition) {
            return null;
        }
    }

    public static StringBuffer getDateIntervalDefaultFormat(Object o) {
        return new StringBuffer(MainFrame.dateFormat.format(getDateFromInterval(o, true))
                + " - " + MainFrame.dateFormat.format(getDateFromInterval(o, false)));
    }
}
