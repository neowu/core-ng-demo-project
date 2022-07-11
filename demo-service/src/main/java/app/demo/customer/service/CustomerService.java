package app.demo.customer.service;

import app.demo.api.customer.CreateCustomerRequest;
import app.demo.api.customer.CustomerView;
import app.demo.api.customer.SearchCustomerRequest;
import app.demo.api.customer.SearchCustomerResponse;
import app.demo.api.customer.UpdateCustomerRequest;
import app.demo.customer.domain.Customer;
import app.demo.customer.domain.CustomerStatus;
import core.framework.db.Query;
import core.framework.db.Repository;
import core.framework.inject.Inject;
import core.framework.util.Strings;
import core.framework.web.exception.ConflictException;
import core.framework.web.exception.NotFoundException;

import java.time.ZonedDateTime;
import java.util.Optional;
import java.util.stream.Collectors;

public class CustomerService {
    @Inject
    Repository<Customer> customerRepository;

    public CustomerView get(Long id) {
        Customer customer = customerRepository.get(id).orElseThrow(() -> new NotFoundException("customer not found, id=" + id));
        return view(customer);
    }

    public CustomerView create(CreateCustomerRequest request) {
        Optional<Customer> existingCustomer = customerRepository.selectOne("email = ?", request.email);
        if (existingCustomer.isPresent()) {
            throw new ConflictException("customer already exists, email=" + request.email);
        }

        var customer = new Customer();
        customer.status = CustomerStatus.ACTIVE;
        customer.email = request.email;
        customer.name = request.name;
        customer.updatedTime = ZonedDateTime.now();
        customer.id = customerRepository.insert(customer).orElseThrow();

        return view(customer);
    }

    public CustomerView update(Long id, UpdateCustomerRequest request) {
        Customer customer = customerRepository.get(id).orElseThrow(() -> new NotFoundException("customer not found, id=" + id));
        customer.name = request.name;
        customer.updatedTime = ZonedDateTime.now();
        customerRepository.partialUpdate(customer);
        return view(customer);
    }

    public SearchCustomerResponse search(SearchCustomerRequest request) {
        SearchCustomerResponse result = new SearchCustomerResponse();
        Query<Customer> query = customerRepository.select();
        query.skip(request.skip);
        query.limit(request.limit);
        if (!Strings.isBlank(request.email)) {
            query.where("email = ?", request.email);
        }
        if (!Strings.isBlank(request.name)) {
            query.where("name like ?", Strings.format("{}%", request.name));
        }
        result.customers = query.fetch().stream().map(this::view).collect(Collectors.toList());
        result.total = query.count();

        return result;
    }

    private CustomerView view(Customer customer) {
        var view = new CustomerView();
        view.id = String.valueOf(customer.id);
        view.email = customer.email;
        view.name = customer.name;
        view.updatedTime = customer.updatedTime;
        return view;
    }
}
