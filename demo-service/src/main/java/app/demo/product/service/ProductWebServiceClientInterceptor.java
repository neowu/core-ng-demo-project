package app.demo.product.service;

import core.framework.api.http.HTTPRequest;
import core.framework.api.web.service.WebServiceClientInterceptor;

/**
 * @author neo
 */
public class ProductWebServiceClientInterceptor implements WebServiceClientInterceptor {
    @Override
    public void intercept(HTTPRequest request) {
        request.header("Signature", "value");
    }
}
