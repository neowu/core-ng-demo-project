package app.web.sse;

import core.framework.log.ActionLogContext;
import core.framework.util.Strings;
import core.framework.util.Threads;
import core.framework.web.Request;
import core.framework.web.exception.UnauthorizedException;
import core.framework.web.rate.LimitRate;
import core.framework.web.sse.Channel;
import core.framework.web.sse.ChannelListener;

import java.nio.charset.StandardCharsets;
import java.time.Duration;

public class NotificationEventListener implements ChannelListener<NotificationEvent> {
    @LimitRate("sse")
    @Override
    public void onConnect(Request request, Channel<NotificationEvent> channel, String lastEventId) {
        ActionLogContext.triggerTrace(true);

        var body = new String(request.body().orElse(Strings.bytes("no_body")), StandardCharsets.UTF_8);

        channel.join("group1");

        channel.context().put("some_key", "value");

        for (int i = 0; i < 10; i++) {
            final NotificationEvent event = new NotificationEvent();
            event.text = "body=" + body + ", on connect " + i;
            channel.send(String.valueOf(i), event);
            Threads.sleepRoughly(Duration.ofSeconds(1));
        }

//        channel.close();
        throw new UnauthorizedException("test unauthorized exception");
    }

    @Override
    public void onClose(Channel<NotificationEvent> channel) {
        ActionLogContext.triggerTrace(true);
        ActionLogContext.put("some_key", channel.context().get("some_key"));
    }
}
