package lsfusion.gwt.client.form.property.cell.classes.controller;

import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.Event;
import lsfusion.gwt.client.base.GwtClientUtils;
import lsfusion.gwt.client.base.view.EventHandler;
import lsfusion.gwt.client.form.property.GPropertyDraw;
import lsfusion.gwt.client.form.property.PValue;
import lsfusion.gwt.client.form.property.cell.controller.EditManager;
import lsfusion.gwt.client.form.property.cell.controller.KeepCellEditor;
import lsfusion.gwt.client.form.property.cell.view.CellRenderer;
import lsfusion.gwt.client.form.property.cell.view.RenderContext;

public class RichTextCellEditor extends ARequestValueCellEditor implements RequestEmbeddedCellEditor, KeepCellEditor {

    private final GPropertyDraw property;

    private String oldValue;

    public RichTextCellEditor(EditManager editManager, GPropertyDraw property) {
        super(editManager);
        this.property = property;
    }

    @Override
    public void start(EventHandler handler, Element parent, RenderContext renderContext, boolean notFocusable, PValue oldValue) {
        this.oldValue = getEditorValue(parent);

        String value = handler != null ? TextBasedCellEditor.checkStartEvent(handler.event, parent, null) : null;
        boolean selectAll = value == null;

        if(value == null) {
            value = PValue.getStringValue(oldValue);
            if (value != null)
                value = value.replaceAll("<div", "<p").replaceAll("</div>", "</p>");
        }

        enableRichTextEditing(parent, true);
        start(parent, value, selectAll && !property.notSelectAll);
    }

    private void enableRichTextEditing(Element parent, boolean enable) {
        enableEditing(parent, enable);
        CellRenderer.setIsEditing(null, parent, enable);

        if (enable)
            GwtClientUtils.addClassName(parent, "property-hide-toolbar");
        else
            GwtClientUtils.removeClassName(parent, "property-hide-toolbar");
    }

    protected native void start(Element element, String value, boolean selectAll)/*-{
        var quill = element.quill;
        quill.focus();

        this.@RichTextCellEditor::setEditorValue(*)(element, value);
        if (selectAll === true) {
            if (value === "")
                quill.deleteText(0, quill.getLength());

            if (value != null)
                this.@RichTextCellEditor::selectContent(*)(quill, 0, value.length);
        } else {
            this.@RichTextCellEditor::selectContent(*)(quill, quill.getLength(), 0); //set the cursor to the end
        }
    }-*/;

    protected native void selectContent(Element quill, int from, int to)/*-{
        setTimeout(function setSelection() {
            quill.setSelection(from, to);
        }, 0);
    }-*/;


    protected native void setEditorValue(Element element, String value)/*-{
        if (this.@RichTextCellEditor::getEditorValue(*)(element) !== value) {
            var quill = element.quill;
            quill.deleteText(0, quill.getLength());
            quill.root.innerHTML = value;
        }
    }-*/;

    protected native String getEditorValue(Element element)/*-{
        var quill = element.quill;

        //quilljs Documentation says:
        // "Note even when Quill is empty, there is still a blank line represented by '\n', so getLength() will return 1."
        return quill != null && quill.getLength() > 1 ? quill.root.innerHTML : null;
    }-*/;

    protected native void enableEditing(Element element, boolean enableEditing)/*-{
        element.quill.enable(enableEditing);
    }-*/;

    @Override
    public void stop(Element parent, boolean cancel, boolean blurred) {
        enableRichTextEditing(parent, false);
        if (cancel)
            setEditorValue(parent, oldValue); //to return the previous value after pressing esc
    }

    @Override
    public PValue getCommitValue(Element parent, Integer contextAction) {
        return PValue.getPValue(getEditorValue(parent));
    }

    @Override
    public boolean checkEnterEvent(Event event) {
        return event.getShiftKey();
    }
}
