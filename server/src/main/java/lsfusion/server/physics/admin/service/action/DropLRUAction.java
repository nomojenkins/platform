package lsfusion.server.physics.admin.service.action;

import lsfusion.base.col.lru.ALRUMap;
import lsfusion.server.data.sql.exception.SQLHandledException;
import lsfusion.server.data.value.DataObject;
import lsfusion.server.data.value.ObjectValue;
import lsfusion.server.logics.action.controller.context.ExecutionContext;
import lsfusion.server.logics.classes.ValueClass;
import lsfusion.server.logics.property.classes.ClassPropertyInterface;
import lsfusion.server.physics.admin.service.ServiceLogicsModule;
import lsfusion.server.physics.dev.integration.internal.to.InternalAction;

import java.sql.SQLException;
import java.util.Iterator;

public class DropLRUAction extends InternalAction {

    @Override
    protected boolean allowNulls() {
        return true;
    }

    private final ClassPropertyInterface percentInterface;
    private final ClassPropertyInterface randomInterface;

    public DropLRUAction(ServiceLogicsModule LM, ValueClass... classes) {
        super(LM, classes);

        Iterator<ClassPropertyInterface> i = interfaces.iterator();
        percentInterface = i.next();
        randomInterface = i.next();

    }

    @Override
    protected void executeInternal(ExecutionContext<ClassPropertyInterface> context) {
        final ObjectValue keyValue = context.getKeyValue(percentInterface);
        if(keyValue instanceof DataObject) {
            final double percent = ((Double) ((DataObject) keyValue).object) / 100.0;
            if (context.getKeyValue(randomInterface).isNull()) {
                ALRUMap.forceRemoveAllLRU(percent);
            } else {
                ALRUMap.forceRandomRemoveAllLRU(percent);
            }
        }
    }
}
