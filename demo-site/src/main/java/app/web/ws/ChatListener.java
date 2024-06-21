package app.web.ws;

import core.framework.web.Request;
import core.framework.web.rate.LimitRate;
import core.framework.web.websocket.Channel;
import core.framework.web.websocket.ChannelListener;

/**
 * @author neo
 */
public class ChatListener implements ChannelListener<ChatMessage, ChatMessage> {
    @Override
    public void onConnect(Request request, Channel<ChatMessage> channel) {
        channel.context().put("name", request.session().get("hello").orElse("no session"));
        channel.join("private");
    }

    @Override
    @LimitRate("message")
    public void onMessage(Channel<ChatMessage> channel, ChatMessage message) {
        if ("stop".equals(message.text)) {
            channel.close();
            return;
        }
        if ("error".equals(message.text)) {
            throw new Error("syntactic error");
        }
        ChatMessage response = new ChatMessage();
        response.text = "mirror back: " + message.text + " by " + channel.context().get("name");
        channel.send(response);
    }

    @Override
    public void onClose(Channel<ChatMessage> channel, int code, String reason) {
        System.out.println("channel closed, code=" + code + ", name=" + channel.context().get("name"));
    }
}
