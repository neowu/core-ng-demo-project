package app.demo.customer.web;

import app.demo.api.CustomerMongoWebService;
import app.demo.api.customer.CreateCustomerRequest;
import app.demo.api.customer.CustomerView;
import app.demo.customer.service.CustomerMongoService;
import core.framework.inject.Inject;

public class CustomerMongoWebServiceImpl implements CustomerMongoWebService {
    @Inject
    CustomerMongoService customerService;

    @Override
    public CustomerView get(String id) {
        return customerService.get(id);
    }

    @Override
    public CustomerView create(CreateCustomerRequest request) {
        return customerService.create(request);
    }
}
