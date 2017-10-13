package app.demo.api.product;

import core.framework.api.json.Property;
import core.framework.api.validate.NotNull;

import java.time.LocalDateTime;

/**
 * @author neo
 */
public class ProductView {
    @Property(name = "id")
    public String id;

    @NotNull(message = "name is required")
    @Property(name = "name")
    public String name;

    @Property(name = "desc")
    public String description;

    @Property(name = "created_time")
    public LocalDateTime createdTime;
}
