package app;

import app.web.sse.NotificationController;
import app.web.sse.NotificationEvent;
import app.web.sse.NotificationEventListener;
import app.web.sse.NotificationPage;
import core.framework.module.Module;

import static core.framework.http.HTTPMethod.GET;

public class NotificationModule extends Module {
    @Override
    protected void initialize() {
        sse().listen("/sse", NotificationEvent.class, bind(NotificationEventListener.class));

        var controller = bind(NotificationController.class);
        site().template("/template/notification.html", NotificationPage.class);
        http().route(GET, "/notification", controller::notification);
        http().route(GET, "/notification/publish", controller::publish);
        http().route(GET, "/notification/close", controller::close);
    }
}
