package app.demo.product.service;

import app.demo.api.product.CreateProductRequest;
import app.demo.api.product.ProductView;
import app.demo.api.product.UpdateProductRequest;
import core.framework.cache.Cache;
import core.framework.inject.Inject;
import core.framework.util.Maps;
import core.framework.web.exception.NotFoundException;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;

/**
 * @author neo
 */
public class ProductService {
    private final Map<String, ProductView> products = Maps.newConcurrentHashMap();
    @Inject
    Cache<ProductView> cache;

    public Optional<ProductView> get(String id) {
        ProductView view = cache.get(id, products::get);
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

    public void update(String id, UpdateProductRequest request) {
        ProductView product = products.get(id);
        if (product == null) throw new NotFoundException("product not found");
        if (request.name != null) product.name = request.name;
    }
}
