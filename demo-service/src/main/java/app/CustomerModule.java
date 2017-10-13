package app;

import app.demo.api.CustomerWebService;
import app.demo.customer.domain.Customer;
import app.demo.customer.service.CustomerService;
import app.demo.customer.web.CustomerWebServiceImpl;
import core.framework.api.Module;

public class CustomerModule extends Module {
    @Override
    protected void initialize() {
        db().repository(Customer.class);
        bind(CustomerService.class);
        api().service(CustomerWebService.class, bind(CustomerWebServiceImpl.class));
    }
}
