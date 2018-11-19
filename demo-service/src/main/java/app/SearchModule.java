package app;

import app.demo.product.search.ProductDocument;
import core.framework.module.Module;
import core.framework.search.module.SearchConfig;

/**
 * @author neo
 */
public class SearchModule extends Module {
    @Override
    protected void initialize() {
        SearchConfig search = config(SearchConfig.class);
        search.host(requiredProperty("sys.elasticsearch.host"));

        search.type(ProductDocument.class);
    }
}
