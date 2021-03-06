package lsfusion.base.file;

import com.google.common.base.Throwables;
import lsfusion.interop.action.ClientActionDispatcher;
import lsfusion.interop.action.ExecuteClientAction;

import java.io.IOException;

public class WriteClientAction extends ExecuteClientAction {
    public final RawFileData file;
    public final String path;
    public final String extension;
    public final boolean append;
    public final boolean isDialog;

    public WriteClientAction(RawFileData file, String path, String extension, boolean append, boolean isDialog) {
        this.file = file;
        this.path = path;
        this.extension = extension;
        this.append = append;
        this.isDialog = isDialog;
    }

    @Override
    public void execute(ClientActionDispatcher dispatcher) {
        try {
            String filePath = path;
            String fileExtension = extension;
            if (isDialog) {
                filePath = FileDialogUtils.showSaveFileDialog(WriteUtils.appendExtension(path, extension), file);
                fileExtension = null;
            }
            if (filePath != null) {
                WriteUtils.write(file, filePath, fileExtension, true, append);
            }
        } catch (Exception e) {
            throw Throwables.propagate(e);
        }
    }
}