package app;

import app.web.ws.ChatController;
import app.web.ws.ChatListener;
import app.web.ws.ChatMessage;
import app.web.ws.ChatPage;
import core.framework.module.Module;

import java.time.Duration;

import static core.framework.http.HTTPMethod.GET;

public class ChatModule extends Module {
    @Override
    protected void initialize() {
        http().limitRate().add("message", 5, 1, Duration.ofSeconds(1));
        ws().listen("/ws/chat", ChatMessage.class, ChatMessage.class, new ChatListener());

        var controller = bind(ChatController.class);
        site().template("/template/chat.html", ChatPage.class);
        http().route(GET, "/chat", controller::chat);
        http().route(GET, "/chat-publish", controller::publish);
        http().route(GET, "/chat-close", controller::close);
    }
}
