package app.demo.product.web;

import app.demo.api.ProductWebService;
import app.demo.api.product.CreateProductRequest;
import app.demo.api.product.ProductView;
import app.demo.api.product.SearchProductRequest;
import app.demo.api.product.SearchProductResponse;
import app.demo.api.product.UpdateProductRequest;
import app.demo.product.service.ProductService;
import core.framework.inject.Inject;
import core.framework.log.ActionLogContext;
import core.framework.util.Lists;
import core.framework.web.rate.LimitRate;

import java.util.Optional;

/**
 * @author neo
 */
public class ProductWebServiceImpl implements ProductWebService {
    @Inject
    ProductService productService;

    @Override
    public SearchProductResponse search(SearchProductRequest request) {
        SearchProductResponse response = new SearchProductResponse();
        response.products = Lists.newArrayList();
        return response;
    }

    @LimitRate("product")
    @Override
    public Optional<ProductView> get(String id) {
        ActionLogContext.put("pid", id);
        return productService.get(id);
    }

    @Override
    public void create(CreateProductRequest request) {
        productService.create(request);
    }

    @Override
    public void update(String id, UpdateProductRequest request) {
        productService.update(id, request);
    }
}
