package app.web.ws;

import core.framework.web.Request;
import core.framework.web.websocket.Channel;
import core.framework.web.websocket.ChannelListener;

/**
 * @author neo
 */
public class ChatListener implements ChannelListener<ChatMessage> {
    @Override
    public void onConnect(Request request, Channel channel) {
        channel.context().put("name", request.session().get("hello").orElse("no session"));
        channel.join("private");
    }

    @Override
    public void onMessage(Channel channel, ChatMessage message) {
        if ("stop".equals(message.text)) {
            channel.close();
            return;
        }
        ChatMessage response = new ChatMessage();
        response.text = "mirror back: " + message.text + " by " + channel.context().get("name");
        channel.send(response);
    }
}
