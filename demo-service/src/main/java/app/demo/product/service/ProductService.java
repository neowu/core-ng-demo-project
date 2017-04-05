package app.demo.product.service;

import app.demo.api.product.CreateProductRequest;
import app.demo.api.product.ProductView;
import core.framework.api.util.Maps;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;

/**
 * @author neo
 */
public class ProductService {
    private final Map<String, ProductView> products = Maps.newConcurrentHashMap();

    public Optional<ProductView> get(String id) {
        ProductView view = products.get(id);
        return Optional.ofNullable(view);
    }

    public void create(CreateProductRequest request) {
        ProductView product = new ProductView();
        product.id = request.id;
        product.name = request.name;
        product.description = request.description;
        product.createdTime = LocalDateTime.now();
        products.put(product.id, product);
    }
}
