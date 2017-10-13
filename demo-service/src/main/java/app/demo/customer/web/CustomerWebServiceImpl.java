package app.demo.customer.web;

import app.demo.api.CustomerWebService;
import app.demo.api.customer.CreateCustomerRequest;
import app.demo.api.customer.CustomerView;
import app.demo.api.customer.SearchCustomerRequest;
import app.demo.api.customer.SearchCustomerResponse;
import app.demo.api.customer.UpdateCustomerRequest;
import app.demo.customer.service.CustomerService;

import javax.inject.Inject;

public class CustomerWebServiceImpl implements CustomerWebService {
    @Inject
    CustomerService customerService;

    @Override
    public CustomerView create(CreateCustomerRequest request) {
        return customerService.create(request);
    }

    @Override
    public CustomerView update(Long id, UpdateCustomerRequest request) {
        return customerService.update(id, request);
    }

    @Override
    public SearchCustomerResponse search(SearchCustomerRequest request) {
        return customerService.search(request);
    }
}
