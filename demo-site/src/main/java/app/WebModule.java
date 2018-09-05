package app;

import app.web.ChatController;
import app.web.ChatListener;
import app.web.ChatPage;
import app.web.IndexController;
import app.web.IndexPage;
import app.web.LanguageManager;
import app.web.UploadController;
import app.web.UploadPage;
import app.web.WildcardController;
import app.web.ajax.AJAXController;
import app.web.interceptor.TestInterceptor;
import core.framework.api.http.HTTPStatus;
import core.framework.http.ContentType;
import core.framework.module.Module;
import core.framework.web.Response;

import java.time.Duration;
import java.util.List;

/**
 * @author neo
 */
public class WebModule extends Module {
    @Override
    protected void initialize() {
        http().intercept(bind(TestInterceptor.class));

        route().get("/hello", request -> Response.text("hello").status(HTTPStatus.CREATED).contentType(ContentType.TEXT_PLAIN));
        route().get("/hello/", request -> Response.text("hello with trailing slash").status(HTTPStatus.CREATED).contentType(ContentType.TEXT_PLAIN));
        route().get("/hello/:name", request -> Response.text("hello " + request.pathParam("name")).status(HTTPStatus.CREATED).contentType(ContentType.TEXT_PLAIN));
        route().get("/hello-redirect", request -> Response.redirect("/hello"));

        site().staticContent("/static").cache(Duration.ofHours(1));
        site().staticContent("/favicon.ico").cache(Duration.ofHours(1));
        site().staticContent("/robots.txt");

        List<String> messages = List.of("messages/main.properties", "messages/main_en.properties", "messages/main_en_CA.properties");
        site().message(messages, "en_US", "en_CA");

        site().template("/template/index.html", IndexPage.class);
        site().template("/template/upload.html", UploadPage.class);

        bind(LanguageManager.class);

        IndexController index = bind(IndexController.class);
        route().get("/", index::index);
        route().post("/submit", index::submit);
        route().get("/logout", index::logout);

        UploadController upload = bind(UploadController.class);
        route().get("/upload", upload::get);
        route().post("/upload", upload::post);

        route().post("/ajax", bind(AJAXController.class)::ajax);

        WildcardController wildcardController = bind(WildcardController.class);
        route().get("/:all(*)", wildcardController::wildcard);

        configureChat();
    }

    private void configureChat() {
        ws().add("/ws/chat", new ChatListener());

        ChatController controller = bind(ChatController.class);
        site().template("/template/chat.html", ChatPage.class);
        route().get("/chat", controller::chat);
        route().get("/chat-publish", controller::publish);
    }
}
