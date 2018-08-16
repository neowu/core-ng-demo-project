package app.demo.product.web;

import app.demo.api.product.kafka.ProductUpdatedMessage;
import core.framework.inject.Inject;
import core.framework.kafka.MessagePublisher;
import core.framework.web.Controller;
import core.framework.web.Request;
import core.framework.web.Response;

/**
 * @author neo
 */
public class ProductUpdatedMessageTestController implements Controller {
    @Inject
    MessagePublisher<ProductUpdatedMessage> publisher;

    @Override
    public Response execute(Request request) {
        for (int i = 0; i < 100; i++) {
            ProductUpdatedMessage value = new ProductUpdatedMessage();
            value.id = String.valueOf(i);
            value.name = "name-" + i;
            publisher.publish(value.id, value);
        }
        return Response.empty();
    }
}
