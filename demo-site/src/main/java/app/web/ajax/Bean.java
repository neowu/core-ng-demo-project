package app.web.ajax;

import core.framework.api.json.Property;

/**
 * @author neo
 */
public class Bean {
    @Property(name = "name")
    public String name;
    @Property(name = "desc")
    public String description;
    @Property(name = "password")
    public String password;
}
