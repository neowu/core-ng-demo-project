package app.demo.api.customer;

import core.framework.api.validate.NotNull;
import core.framework.api.web.service.QueryParam;

public class SearchCustomerRequest {
    @QueryParam(name = "email")
    public String email;

    @QueryParam(name = "name")
    public String name;

    @NotNull
    @QueryParam(name = "skip")
    public Integer skip = 0;

    @NotNull
    @QueryParam(name = "limit")
    public Integer limit = 1000;
}
