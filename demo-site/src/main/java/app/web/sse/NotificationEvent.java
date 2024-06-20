package app.web.sse;

import core.framework.api.json.Property;

/**
 * @author neo
 */
public class NotificationEvent {
    @Property(name = "text")
    public String text;
}
