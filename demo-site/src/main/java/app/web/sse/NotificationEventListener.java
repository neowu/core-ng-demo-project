package app.web.sse;

import core.framework.web.Request;
import core.framework.web.sse.ServerSentEventChannel;
import core.framework.web.sse.ServerSentEventListener;

public class NotificationEventListener implements ServerSentEventListener<NotificationEvent> {
    @Override
    public void onConnect(Request request, ServerSentEventChannel<NotificationEvent> channel, String lastEventId) {
        System.err.println(lastEventId);
//        throw new Error("test error");
    }
}
