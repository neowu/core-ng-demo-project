package app.demo.product.kafka;

import app.demo.api.product.kafka.ProductUpdatedMessage;
import core.framework.kafka.BulkMessageHandler;
import core.framework.kafka.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * @author neo
 */
public class ProductUpdatedMessageHandler implements BulkMessageHandler<ProductUpdatedMessage> {
    private final Logger logger = LoggerFactory.getLogger(ProductUpdatedMessageHandler.class);

//    @Override
//    public void handle(String key, LsProductUpdatedMessage messages) {
//        logger.debug("{}-{}", key, messages.name);
//    }

    @Override
    public void handle(List<Message<ProductUpdatedMessage>> messages) throws Exception {
        for (Message<ProductUpdatedMessage> message : messages) {
            logger.debug("{}-{}", message.key, message.value);
        }
    }
}
