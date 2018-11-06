package app;

import app.demo.api.ProductWebService;
import app.demo.api.product.ProductView;
import app.demo.api.product.kafka.ProductUpdatedMessage;
import app.demo.product.kafka.ProductUpdatedMessageHandler;
import app.demo.product.service.ProductService;
import app.demo.product.web.ProductUpdatedMessageTestController;
import app.demo.product.web.ProductWebServiceImpl;
import core.framework.module.Module;
import core.framework.web.Response;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

import static core.framework.http.HTTPMethod.GET;
import static core.framework.http.HTTPMethod.PUT;

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

        http().route(POST, "/http-test", request -> {
//            Thread.sleep(10000);
            return Response.empty().status(HTTPStatus.SERVICE_UNAVAILABLE);
        });
    }

    private void configureKafka() {
        kafka().uri("localhost:9092");

        kafka().subscribe("product-updated", ProductUpdatedMessage.class, bind(ProductUpdatedMessageHandler.class));
        kafka().poolSize(2);

        kafka().publish("product-updated", ProductUpdatedMessage.class);
        http().route(GET, "/kafka-test", bind(ProductUpdatedMessageTestController.class));
    }
}
