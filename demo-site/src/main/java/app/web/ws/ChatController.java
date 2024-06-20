package app.web.ws;

import app.web.LanguageManager;
import core.framework.inject.Inject;
import core.framework.web.Request;
import core.framework.web.Response;
import core.framework.web.websocket.Channel;
import core.framework.web.websocket.WebSocketContext;

import java.util.List;

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
        final List<Channel<ChatMessage>> channels = context.all();
        for (Channel<ChatMessage> channel : channels) {
            String text = "hello " + channel.context().get("neo") + "! (from all)";
            var message = new ChatMessage();
            message.text = text;
            channel.send(message);
        }

        List<Channel<ChatMessage>> room = context.room("private");
        for (Channel<ChatMessage> channel : room) {
            var message = new ChatMessage();
            message.text = "hello from private";
            channel.send(message);
        }

        return Response.text("done");
    }

    public Response close(Request request) {
        for (Channel<?> channel : context.all()) {
            channel.close();
        }
        return Response.text("done");
    }
}
