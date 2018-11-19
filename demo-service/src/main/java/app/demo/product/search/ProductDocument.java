package app.demo.product.search;

import core.framework.api.json.Property;
import core.framework.api.validate.NotNull;
import core.framework.search.Index;

import java.time.LocalDateTime;

/**
 * @author neo
 */
@Index(name = "product")
public class ProductDocument {
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
