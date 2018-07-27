package app.demo.api.customer;

import core.framework.api.validate.NotNull;
import core.framework.api.web.service.QueryParam;

public class SearchCustomerRequest {
    @NotNull
    @QueryParam(name = "skip")
    public Integer skip = 0;

    @NotNull
    @QueryParam(name = "limit")
    public Integer limit = 1000;

    @QueryParam(name = "email")
    public String email;

    @QueryParam(name = "first_name")
    public String firstName;

    @QueryParam(name = "last_name")
    public String lastName;
}
