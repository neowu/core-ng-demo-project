package app.demo.customer.service;

import app.demo.api.customer.CreateCustomerRequest;
import app.demo.api.customer.CustomerView;
import app.demo.api.customer.SearchCustomerRequest;
import app.demo.api.customer.SearchCustomerResponse;
import app.demo.api.customer.UpdateCustomerRequest;
import app.demo.customer.domain.Customer;
import core.framework.api.db.Query;
import core.framework.api.db.Repository;
import core.framework.api.util.Strings;
import core.framework.api.web.exception.ConflictException;
import core.framework.api.web.exception.NotFoundException;

import javax.inject.Inject;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.stream.Collectors;

public class CustomerService {
    @Inject
    Repository<Customer> customerRepository;

    public CustomerView create(CreateCustomerRequest request) {
        Optional<Customer> existingCustomer = customerRepository.selectOne("email = ?", request.email);
        if (existingCustomer.isPresent()) {
            throw new ConflictException("customer already exists, email = " + request.email);
        }

        Customer customer = new Customer();
        customer.email = request.email;
        customer.firstName = request.firstName;
        customer.lastName = request.lastName;
        customer.updatedTime = LocalDateTime.now();
        customer.id = customerRepository.insert(customer).get();

        return view(customer);
    }

    public CustomerView update(Long id, UpdateCustomerRequest request) {
        Customer customer = customerRepository.get(id).orElseThrow(() -> new NotFoundException("customer not found, id = " + id.toString()));
        customer.updatedTime = LocalDateTime.now();
        customer.firstName = request.firstName;
        if (request.lastName != null) {
            customer.lastName = request.lastName;
        }
        customerRepository.update(customer);
        return view(customer);
    }

    public SearchCustomerResponse search(SearchCustomerRequest request) {
        SearchCustomerResponse result = new SearchCustomerResponse();
        Query<Customer> query = customerRepository.select();
        query.skip(request.skip)
            .limit(request.limit);
        if (!Strings.isEmpty(request.email)) {
            query.where("email = ?", request.email);
        }
        if (!Strings.isEmpty(request.firstName)) {
            query.where("first_name like ?", Strings.format("{}%", request.firstName));
        }
        if (!Strings.isEmpty(request.lastName)) {
            query.where("last_name like ?", Strings.format("{}%", request.lastName));
        }
        result.customers = query.fetch().stream().map(this::view).collect(Collectors.toList());
        result.total = query.count();

        return result;
    }

    private CustomerView view(Customer customer) {
        CustomerView result = new CustomerView();
        result.id = customer.id;
        result.email = customer.email;
        result.firstName = customer.firstName;
        result.lastName = customer.lastName;
        result.updatedTime = customer.updatedTime;
        return result;
    }
}
