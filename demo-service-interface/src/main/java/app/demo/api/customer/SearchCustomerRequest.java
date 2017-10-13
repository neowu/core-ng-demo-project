package app.demo.api.customer;

import core.framework.api.json.Property;

public class SearchCustomerRequest {
    @Property(name = "skip")
    public Integer skip = 0;

    @Property(name = "limit")
    public Integer limit = 1000;

    @Property(name = "email")
    public String email;

    @Property(name = "first_name")
    public String firstName;

    @Property(name = "last_name")
    public String lastName;
}
