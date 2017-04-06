package app;

import app.demo.api.ProductWebService;
import app.demo.api.product.ProductView;
import app.demo.api.product.kafka.ProductUpdatedMessage;
import app.demo.product.kafka.ProductUpdatedMessageHandler;
import app.demo.product.service.ProductService;
import app.demo.product.web.ProductUpdatedMessageTestController;
import app.demo.product.web.ProductWebServiceImpl;
import core.framework.api.Module;

import java.time.Duration;

/**
 * @author neo
 */
public class ProductModule extends Module {
    @Override
    protected void initialize() {
        cache().add(ProductView.class, Duration.ofSeconds(60));

        bind(ProductService.class);

        configureKafka();

        api().service(ProductWebService.class, bind(ProductWebServiceImpl.class));
    }

    private void configureKafka() {
        kafka().uri("localhost:9092");

        kafka().subscribe("product-updated", ProductUpdatedMessage.class, bind(ProductUpdatedMessageHandler.class))
               .poolSize(2);

        kafka().publish("product-updated", ProductUpdatedMessage.class);
        route().get("/kafka-test", bind(ProductUpdatedMessageTestController.class));
    }
}
