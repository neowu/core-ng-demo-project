package app.demo.api.product;

import core.framework.api.json.Property;
import core.framework.api.validate.NotNull;

/**
 * @author neo
 */
public class CreateProductRequest {
    @NotNull(message = "id is required")
    @Property(name = "id")
    public String id;

    @NotNull(message = "name is required")
    @Property(name = "name")
    public String name;

    @Property(name = "desc")
    public String description;
}
