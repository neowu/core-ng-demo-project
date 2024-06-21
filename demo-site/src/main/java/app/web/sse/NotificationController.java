package app.web.sse;

import app.web.LanguageManager;
import core.framework.inject.Inject;
import core.framework.web.Request;
import core.framework.web.Response;
import core.framework.web.sse.Channel;
import core.framework.web.sse.ServerSentEventContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.ZonedDateTime;
import java.util.concurrent.atomic.AtomicInteger;

public class NotificationController {
    private final Logger logger = LoggerFactory.getLogger(NotificationController.class);
    private final AtomicInteger id = new AtomicInteger();
    @Inject
    ServerSentEventContext<NotificationEvent> context;
    @Inject
    LanguageManager languageManager;

    public Response notification(Request request) {
        return Response.html("/template/notification.html", new NotificationPage(), languageManager.language());
    }

    public Response publish(Request request) {
        logger.warn("test");
        var event = new NotificationEvent();
        event.text = "message at " + ZonedDateTime.now(); //"1234".repeat(2000) + "9999";
        for (Channel<NotificationEvent> channel : context.all()) {
            channel.send(String.valueOf(id.incrementAndGet()), event);
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
