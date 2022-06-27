package app.demo.product.web;

import app.demo.api.product.kafka.ProductUpdatedMessage;
import core.framework.inject.Inject;
import core.framework.kafka.MessagePublisher;
import core.framework.log.ActionLogContext;
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
    @IOWarning(operation = "kafka", maxTotalWrites = 2)
    public Response execute(Request request) {
        ActionLogContext.triggerTrace(true);
        for (int i = 0; i < 10; i++) {
            ProductUpdatedMessage value = new ProductUpdatedMessage();
            value.id = String.valueOf(i);
            value.name = "name-" + i + Randoms.alphaNumeric(1024 * 1025);
            publisher.publish(value.id, value);
        }
        return Response.empty();
    }
}
