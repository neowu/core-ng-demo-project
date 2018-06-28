package app.demo.api.product;

import core.framework.api.json.Property;

import java.util.List;

/**
 * @author neo
 */
public class SearchProductResponse {
    @Property(name = "products")
    public List<ProductView> products;
}
