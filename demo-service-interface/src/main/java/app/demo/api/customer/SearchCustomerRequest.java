package app.demo.api.customer;

import core.framework.api.web.service.QueryParam;

public class SearchCustomerRequest {
    @QueryParam(name = "skip")
    public Integer skip = 0;

    @QueryParam(name = "limit")
    public Integer limit = 1000;

    @QueryParam(name = "email")
    public String email;

    @QueryParam(name = "first_name")
    public String firstName;

    @QueryParam(name = "last_name")
    public String lastName;
}
