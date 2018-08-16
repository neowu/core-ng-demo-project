package app.demo.product.kafka;

import app.demo.api.product.kafka.ProductUpdatedMessage;
import core.framework.kafka.MessageHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author neo
 */
public class ProductUpdatedMessageHandler implements MessageHandler<ProductUpdatedMessage> {
    private final Logger logger = LoggerFactory.getLogger(ProductUpdatedMessageHandler.class);

    @Override
    public void handle(String key, ProductUpdatedMessage messages) {
        logger.info("{}-{}", key, messages.name);
    }
}
