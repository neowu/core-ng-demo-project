package app;

import app.demo.api.CustomerMongoWebService;
import app.demo.api.CustomerWebService;
import app.demo.customer.domain.Customer;
import app.demo.customer.domain.CustomerEntity;
import app.demo.customer.service.CustomerMongoService;
import app.demo.customer.service.CustomerService;
import app.demo.customer.web.CustomerMongoWebServiceImpl;
import app.demo.customer.web.CustomerWebServiceImpl;
import core.framework.module.Module;
import core.framework.mongo.module.MongoConfig;

public class CustomerModule extends Module {
    @Override
    protected void initialize() {
        db().repository(Customer.class);
        bind(CustomerService.class);
        api().service(CustomerWebService.class, bind(CustomerWebServiceImpl.class));

        MongoConfig config = config(MongoConfig.class);
        config.uri(requiredProperty("sys.mongo.uri"));
        config.collection(CustomerEntity.class);
        bind(CustomerMongoService.class);
        api().service(CustomerMongoWebService.class, bind(CustomerMongoWebServiceImpl.class));
    }
}
