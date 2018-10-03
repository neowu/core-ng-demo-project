package app.demo.product.service;

import core.framework.http.HTTPRequest;
import core.framework.web.service.WebServiceClientInterceptor;

/**
 * @author neo
 */
public class ProductWebServiceClientInterceptor implements WebServiceClientInterceptor {
    @Override
    public void intercept(HTTPRequest request) {
        request.headers.put("Signature", "value");
    }
}
