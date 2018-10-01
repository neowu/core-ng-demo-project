package app.demo.api.customer;

import core.framework.api.json.Property;
import core.framework.api.validate.NotNull;

public class UpdateCustomerRequest {
    @NotNull
    @Property(name = "first_name")
    public String firstName;

    @Property(name = "last_name")
    public String lastName;
}
