import com.google.adk.agents.LlmAgent;
import com.google.adk.agents.RunConfig;
import com.google.adk.events.Event;
import com.google.adk.runner.InMemoryRunner;
import com.google.adk.sessions.Session;
import com.google.genai.types.Content;
import com.google.genai.types.Part;
import core.framework.util.Files;
import io.reactivex.rxjava3.core.Flowable;

import java.nio.file.Path;
import java.util.Scanner;

import static java.nio.charset.StandardCharsets.UTF_8;

public class Main {
    static void main() {
//        var agent = TimeAgent.initAgent();
        var agent = LlmAgent.builder()
                .name("image-agent")
                .description("Tells what's in the image")
                .instruction("""
                        """)
                .model("gemini-flash-latest")
                .build();
        RunConfig runConfig = RunConfig.builder().streamingMode(RunConfig.StreamingMode.NONE).build();
        InMemoryRunner runner = new InMemoryRunner(agent);

//        chat(runner, runConfig);

        Session session = runner
                .sessionService()
                .createSession(runner.appName(), "demo-user")
                .blockingGet();

        Content userMsg = Content.fromParts(Part.fromText("what's in the image"), Part.fromBytes(Files.bytes(Path.of("/Users/neo/Desktop/cat.jpg")), "image/jpg"));
        Flowable<Event> events = runner.runAsync(session.userId(), session.id(), userMsg, runConfig);
        events.blockingForEach(event -> {
            if (event.finalResponse()) {
                System.out.println(event.stringifyContent());
            }
        });
    }

    private static void chat(InMemoryRunner runner, RunConfig runConfig) {
        Session session = runner
                .sessionService()
                .createSession(runner.appName(), "demo-user")
                .blockingGet();

        try (Scanner scanner = new Scanner(System.in, UTF_8)) {
            while (true) {
                System.out.print("\nYou > ");
                String userInput = scanner.nextLine();
                if ("quit".equalsIgnoreCase(userInput)) {
                    break;
                }

                Content userMsg = Content.fromParts(Part.fromText(userInput));
                Flowable<Event> events = runner.runAsync(session.userId(), session.id(), userMsg, runConfig);

                System.out.print("\nAgent > ");
                events.blockingForEach(event -> {
                    if (event.finalResponse()) {
                        System.out.println(event.stringifyContent());
                    }
                });
            }
        }
    }
}
