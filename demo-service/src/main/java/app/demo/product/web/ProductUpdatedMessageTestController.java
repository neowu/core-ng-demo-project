package app.demo.product.web;

import app.demo.api.product.kafka.ProductUpdatedMessage;
import core.framework.api.kafka.MessagePublisher;
import core.framework.api.web.Controller;
import core.framework.api.web.Request;
import core.framework.api.web.Response;

import javax.inject.Inject;

/**
 * @author neo
 */
public class ProductUpdatedMessageTestController implements Controller {
    @Inject
    MessagePublisher<ProductUpdatedMessage> publisher;

    @Override
    public Response execute(Request request) throws Exception {
        for (int i = 0; i < 100; i++) {
            ProductUpdatedMessage value = new ProductUpdatedMessage();
            value.id = String.valueOf(i);
            value.name = "name-" + i;
            publisher.publish(value.id, value);
        }
        return Response.empty();
    }
}
