package app.web;

import app.web.interceptor.Protected;
import core.framework.inject.Inject;
import core.framework.web.Request;
import core.framework.web.Response;
import core.framework.web.Session;
import core.framework.web.site.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author neo
 */
public class IndexController {
    private final Logger logger = LoggerFactory.getLogger(IndexController.class);
    @Inject
    Message message;
    @Inject
    LanguageManager languageManager;

    @Protected(operation = "index")
    public Response index(Request request) {
        IndexPage model = new IndexPage();
        model.name = message.get("key.name", languageManager.language());
        model.imageURL = "https://image.com/image123.jpg";

        Session session = request.session();
//        Optional<String> hello = session.get("hello");
        session.set("hello", "world");
        Response response = Response.html("/template/index.html", model, languageManager.language());
        response.cookie(Cookies.TEST, "1+2");
        response.cookie(Cookies.TEST1, "hello \"\" cookies");
        return response;
    }

    public Response submit(Request request) {
        logger.warn("test");
        return Response.text("hello " + request.formParams().getOrDefault("name", "nobody"));
    }

    public Response logout(Request request) {
        request.session().invalidate();
        return Response.text("logout");
    }
}
