package app;

import app.web.IndexController;
import app.web.IndexPage;
import app.web.LanguageManager;
import app.web.UploadController;
import app.web.UploadPage;
import app.web.WildcardController;
import app.web.ajax.AJAXController;
import app.web.ajax.Bean;
import app.web.ajax.ErrorCodes;
import app.web.interceptor.TestInterceptor;
import core.framework.api.http.HTTPStatus;
import core.framework.module.Module;
import core.framework.web.Response;

import java.time.Duration;
import java.util.List;

import static core.framework.http.HTTPMethod.GET;
import static core.framework.http.HTTPMethod.POST;

/**
 * @author neo
 */
public class WebModule extends Module {
    @Override
    protected void initialize() {
        http().intercept(bind(TestInterceptor.class));

        http().route(GET, "/hello", request -> Response.text("hello").status(HTTPStatus.CREATED));
        http().route(GET, "/hello/", request -> Response.text("hello with trailing slash").status(HTTPStatus.CREATED));
        http().route(GET, "/hello/:name", request -> Response.text("hello " + request.pathParam("name")).status(HTTPStatus.CREATED));
        http().route(GET, "/hello-redirect", request -> Response.redirect("/hello"));

        site().staticContent("/static").cache(Duration.ofHours(1));
        site().staticContent("/favicon.ico").cache(Duration.ofHours(1));
        site().staticContent("/robots.txt");

        List<String> messages = List.of("messages/main.properties", "messages/main_en.properties", "messages/main_en_CA.properties");
        site().message(messages, "en_US", "en_CA");

        site().template("/template/index.html", IndexPage.class);
        site().template("/template/upload.html", UploadPage.class);

        bind(LanguageManager.class);

        IndexController index = bind(IndexController.class);
        http().route(GET, "/", index::index);
        http().route(POST, "/submit", index::submit);
        http().route(GET, "/logout", index::logout);

        UploadController upload = bind(UploadController.class);
        http().route(GET, "/upload", upload::get);
        http().route(POST, "/upload", upload::post);

        http().route(POST, "/ajax", bind(AJAXController.class)::ajax);
        http().bean(Bean.class);
        http().bean(ErrorCodes.class);

        var wildcardController = bind(WildcardController.class);
        http().route(GET, "/:all(*)", wildcardController::wildcard);

        load(new ChatModule());
        load(new NotificationModule());
    }
}
