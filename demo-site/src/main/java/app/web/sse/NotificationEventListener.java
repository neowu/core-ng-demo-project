package app.web.sse;

import core.framework.web.Request;
import core.framework.web.sse.Channel;
import core.framework.web.sse.ChannelListener;

public class NotificationEventListener implements ChannelListener<NotificationEvent> {
    @Override
    public void onConnect(Request request, Channel<NotificationEvent> channel, String lastEventId) {
        System.err.println(lastEventId);
        channel.join("group1");
//        throw new Error("test error");
    }
}
