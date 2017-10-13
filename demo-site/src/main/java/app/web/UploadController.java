package app.web;

import core.framework.web.MultipartFile;
import core.framework.web.Request;
import core.framework.web.Response;

import javax.inject.Inject;

/**
 * @author neo
 */
public class UploadController {
    @Inject
    LanguageManager languageManager;

    public Response get(Request request) {
        return Response.html("/template/upload.html", new UploadPage(), languageManager.language());
    }

    public Response post(Request request) {
        MultipartFile file = request.file("test").get();
        return Response.text("uploaded, fileName=" + file.fileName);
    }
}
