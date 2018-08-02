package app.web;

import java.util.List;

/**
 * @author neo
 */
public class IndexPage {
    public String name;
    public String backgroundColor;
    public String imageURL;

    public String welcomeMessage() {
        return "hello " + name;
    }

    public List<String> items() {
        return List.of("a", "b", "c");
    }

    public Boolean selected(String item) {
        return "b".equals(item);
    }
}
