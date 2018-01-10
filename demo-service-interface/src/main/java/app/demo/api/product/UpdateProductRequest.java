package app.demo.api.product;

import core.framework.api.json.Property;

/**
 * @author neo
 */
public class UpdateProductRequest {
    @Property(name = "name")
    public String name;

    @Property(name = "desc")
    public String description;
}
