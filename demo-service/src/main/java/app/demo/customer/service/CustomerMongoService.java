package app.demo.customer.service;

import app.demo.api.customer.CreateCustomerRequest;
import app.demo.api.customer.CustomerView;
import app.demo.customer.domain.CustomerEntity;
import core.framework.inject.Inject;
import core.framework.mongo.MongoCollection;
import core.framework.web.exception.NotFoundException;
import org.bson.types.ObjectId;

import java.time.ZonedDateTime;

/**
 * @author neo
 */
public class CustomerMongoService {
    @Inject
    MongoCollection<CustomerEntity> customerCollection;

    public CustomerView get(String id) {
        CustomerEntity customer = customerCollection.get(new ObjectId(id)).orElseThrow(() -> new NotFoundException("customer not found, id=" + id));
        return view(customer);
    }

    public CustomerView create(CreateCustomerRequest request) {
        var customer = new CustomerEntity();
        customer.email = request.email;
        customer.name = request.name;
        customer.updatedTime = ZonedDateTime.now();
        customerCollection.insert(customer);
        return view(customer);
    }

    private CustomerView view(CustomerEntity customer) {
        var view = new CustomerView();
        view.id = customer.id.toString();
        view.email = customer.email;
        view.name = customer.name;
        view.updatedTime = customer.updatedTime;
        return view;
    }
}
