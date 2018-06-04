package app.demo.api;

import app.demo.api.customer.CreateCustomerRequest;
import app.demo.api.customer.CustomerView;
import app.demo.api.customer.SearchCustomerRequest;
import app.demo.api.customer.SearchCustomerResponse;
import app.demo.api.customer.UpdateCustomerRequest;
import core.framework.api.http.HTTPStatus;
import core.framework.api.web.service.GET;
import core.framework.api.web.service.POST;
import core.framework.api.web.service.PUT;
import core.framework.api.web.service.Path;
import core.framework.api.web.service.PathParam;
import core.framework.api.web.service.ResponseStatus;

public interface CustomerWebService {
    @GET
    @Path("/customer/:id")
    CustomerView get(@PathParam("id") Long id);

    @POST
    @Path("/customer")
    @ResponseStatus(HTTPStatus.CREATED)
    CustomerView create(CreateCustomerRequest request);

    @PUT
    @Path("/customer/:id")
    CustomerView update(@PathParam("id") Long id, UpdateCustomerRequest request);

    @GET
    @Path("/customer")
    SearchCustomerResponse search(SearchCustomerRequest request);
}
