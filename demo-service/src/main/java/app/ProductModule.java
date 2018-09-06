package app;

import app.demo.api.ProductWebService;
import app.demo.api.product.ProductView;
import app.demo.api.product.kafka.ProductUpdatedMessage;
import app.demo.product.kafka.ProductUpdatedMessageHandler;
import app.demo.product.service.ProductService;
import app.demo.product.web.ProductUpdatedMessageTestController;
import app.demo.product.web.ProductWebServiceImpl;
import core.framework.http.HTTPClient;
import core.framework.http.HTTPClientBuilder;
import core.framework.module.Module;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

import static core.framework.http.HTTPMethod.GET;

/**
 * @author neo
 */
public class ProductModule extends Module {
    @Override
    protected void initialize() {
        bind(HTTPClient.class, new HTTPClientBuilder().build());

        cache().add(ProductView.class, Duration.ofSeconds(60));

        bind(ProductService.class);

        configureKafka();

        api().service(ProductWebService.class, bind(ProductWebServiceImpl.class));

        http().limitRate()
              .add("product", 3, 20, TimeUnit.MINUTES);
    }

    private void configureKafka() {
        kafka().uri("localhost:9092");

        kafka().subscribe("product-updated", ProductUpdatedMessage.class, bind(ProductUpdatedMessageHandler.class));
        kafka().poolSize(2);

        kafka().publish("product-updated", ProductUpdatedMessage.class);
        http().route(GET, "/kafka-test", bind(ProductUpdatedMessageTestController.class));
    }
}
