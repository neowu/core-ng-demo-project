package app.demo.product.web;

import app.demo.api.ProductWebService;
import app.demo.api.product.CreateProductRequest;
import app.demo.api.product.ProductView;
import app.demo.api.product.SearchProductRequest;
import app.demo.product.service.ProductService;
import core.framework.inject.Inject;
import core.framework.log.ActionLogContext;
import core.framework.util.Lists;
import core.framework.web.rate.LimitRate;

import java.util.List;
import java.util.Optional;

/**
 * @author neo
 */
public class ProductWebServiceImpl implements ProductWebService {
    @Inject
    ProductService productService;

    @Override
    public List<ProductView> search(SearchProductRequest request) {
        return Lists.newArrayList();
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
}
