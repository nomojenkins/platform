package lsfusion.http.controller.file;

import lsfusion.gwt.server.FileUtils;
import lsfusion.interop.session.ExternalUtils;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.springframework.web.HttpRequestHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

public class UploadFileRequestHandler implements HttpRequestHandler {

    public UploadFileRequestHandler() {}

    @Override
    public void handleRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {

            DiskFileItemFactory fileItemFactory = new DiskFileItemFactory();
            ServletFileUpload fileUpload = new ServletFileUpload(fileItemFactory);
            request.setCharacterEncoding(ExternalUtils.downloadCharset.name());

            List<FileItem> items = fileUpload.parseRequest(request);

            // in upload there is no savedTempFiles mechanism, since we don't identify the user who uploads the file
            for (FileItem item : items) {
                if (!item.isFormField())
                    FileUtils.writeFile(FileUtils.APP_UPLOAD_FOLDER_PATH, true, request.getParameter("sid") + "_" + item.getName(), fos -> {
                        fos.write(item.get());
                    });
            }
        } catch (Exception e) {
            Throwable cause = e.getCause();
            if (cause instanceof FileNotFoundException && cause.getMessage().contains("File name too long"))
                response.sendError(270);

            throw new ServletException(e);
        }
    }
}
