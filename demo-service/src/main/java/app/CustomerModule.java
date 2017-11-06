package app;

import app.demo.api.CustomerWebService;
import app.demo.api.customer.CreateCustomerRequest;
import app.demo.customer.domain.Customer;
import app.demo.customer.service.CustomerService;
import app.demo.customer.web.CustomerWebServiceImpl;
import core.framework.module.Module;
import core.framework.web.Response;

public class CustomerModule extends Module {
    @Override
    protected void initialize() {
        db().repository(Customer.class);
        CustomerService service = bind(CustomerService.class);
        api().service(CustomerWebService.class, bind(CustomerWebServiceImpl.class));

        route().get("/db-test", request -> {
            service.create(new CreateCustomerRequest());
            return Response.empty();
        });
    }
}
