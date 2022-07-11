package app;

import core.framework.api.http.HTTPStatus;
import core.framework.http.HTTPMethod;
import core.framework.module.App;
import core.framework.module.SystemModule;
import core.framework.web.Response;

/**
 * @author neo
 */
public class DemoServiceApp extends App {
    @Override
    protected void initialize() {
        load(new SystemModule("sys.properties"));

//        redis().host("localhost");
        load(new ProductModule());
//        load(new JobModule());
        load(new CustomerModule());

        http().route(HTTPMethod.GET, "/503", request -> Response.text("hello").status(HTTPStatus.SERVICE_UNAVAILABLE));
        http().route(HTTPMethod.GET, "/204", request -> Response.empty());
    }
}
