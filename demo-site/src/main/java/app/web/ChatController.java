package app.web;

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
        for (Channel channel : context.room("all")) {
            channel.send("hello " + channel.context().get("neo") + "! (from all room)");
        }

        for (Channel channel : context.room("private")) {
            channel.send("hello from private room");
        }

        return Response.text("done");
    }
}
