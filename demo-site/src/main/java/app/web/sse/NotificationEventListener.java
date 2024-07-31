package app.web.sse;

import core.framework.log.ActionLogContext;
import core.framework.util.Threads;
import core.framework.web.Request;
import core.framework.web.sse.Channel;
import core.framework.web.sse.ChannelListener;

import java.time.Duration;

public class NotificationEventListener implements ChannelListener<NotificationEvent> {
    @Override
    public void onConnect(Request request, Channel<NotificationEvent> channel, String lastEventId) {
        ActionLogContext.triggerTrace(true);

        channel.join("group1");

        for (int i = 0; i < 10; i++) {
            final NotificationEvent event = new NotificationEvent();
            event.text = "on connect " + i;
            channel.send(String.valueOf(i), event);
            Threads.sleepRoughly(Duration.ofSeconds(1));
        }

        channel.close();
//        throw new Error("test error");
    }
}
