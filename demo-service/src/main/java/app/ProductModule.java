package app;

import app.demo.api.ProductWebService;
import app.demo.api.product.ProductView;
import app.demo.api.product.kafka.ProductUpdatedMessage;
import app.demo.product.kafka.ProductUpdatedMessageHandler;
import app.demo.product.service.ProductService;
import app.demo.product.web.ProductUpdatedMessageTestController;
import app.demo.product.web.ProductWebServiceImpl;
import core.framework.http.HTTPClient;
import core.framework.module.Module;

import java.time.Duration;

import static core.framework.http.HTTPMethod.GET;

/**
 * @author neo
 */
public class ProductModule extends Module {
    @Override
    protected void initialize() {
        bind(HTTPClient.class, HTTPClient.builder().build());

        cache().add(ProductView.class, Duration.ofHours(2)).local();

        bind(ProductService.class);

//        configureKafka();

        api().service(ProductWebService.class, bind(ProductWebServiceImpl.class));

        http().limitRate()
                .add("product", 3, 20, Duration.ofMinutes(1));
    }

    private void configureKafka() {
        kafka().uri("localhost");

        kafka().subscribe("product-updated", ProductUpdatedMessage.class, bind(ProductUpdatedBulkMessageHandler.class));
        kafka().poolSize(2);

        kafka().publish("product-updated", ProductUpdatedMessage.class);
        http().route(GET, "/kafka-test", bind(ProductUpdatedMessageTestController.class));
    }
}
