package app.web.sse;

import app.web.LanguageManager;
import core.framework.inject.Inject;
import core.framework.web.Request;
import core.framework.web.Response;
import core.framework.web.sse.ServerSentEventChannel;
import core.framework.web.sse.ServerSentEventContext;

import java.time.ZonedDateTime;

public class NotificationController {
    @Inject
    ServerSentEventContext<NotificationEvent> context;
    @Inject
    LanguageManager languageManager;

    public Response notification(Request request) {
        return Response.html("/template/notification.html", new NotificationPage(), languageManager.language());
    }

    public Response publish(Request request) {
        for (ServerSentEventChannel<NotificationEvent> channel : context.all()) {
            var event = new NotificationEvent();
            event.text = "message at " + ZonedDateTime.now() + "1234".repeat(2000) + "9999";
            channel.send(event);
        }

        return Response.text("done");
    }

    public Response close(Request request) {
        for (var channel : context.all()) {
            channel.close();
        }
        return Response.text("done");
    }
}
