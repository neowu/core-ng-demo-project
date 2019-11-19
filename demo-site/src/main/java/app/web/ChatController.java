package app.web;

import app.web.ws.ChatMessage;
import core.framework.inject.Inject;
import core.framework.web.Request;
import core.framework.web.Response;
import core.framework.web.websocket.Channel;
import core.framework.web.websocket.WebSocketContext;

/**
 * @author neo
 */
public class ChatController {
    @Inject
    WebSocketContext context;
    @Inject
    LanguageManager languageManager;

    public Response chat(Request request) {
        return Response.html("/template/chat.html", new ChatPage(), languageManager.language());
    }

    public Response publish(Request request) {
        for (Channel channel : context.all()) {
            String text = "hello " + channel.context().get("neo") + "! (from all)";
            ChatMessage message = new ChatMessage();
            message.text = text;
            channel.send(message);
        }

        for (Channel channel : context.room("private")) {
            ChatMessage message = new ChatMessage();
            message.text = "hello from private";
            channel.send(message);
        }

        return Response.text("done");
    }
}
