package app.web;

import core.framework.web.Request;
import core.framework.web.websocket.Channel;
import core.framework.web.websocket.ChannelListener;

/**
 * @author neo
 */
public class ChatListener implements ChannelListener {
    @Override
    public void onConnect(Request request, Channel channel) {
        channel.context().put("name", "neo");
        channel.join("all");
        channel.join("private");
    }

    @Override
    public void onMessage(Channel channel, String message) {
        if ("stop".equals(message)) {
            channel.close();
            return;
        }
        channel.send("mirror back: " + message);
    }
}
