package app.demo.api.customer;

import core.framework.api.json.Property;
import core.framework.api.validate.NotEmpty;
import core.framework.api.validate.NotNull;

public class UpdateCustomerRequest {
    @NotNull
    @NotEmpty
    @Property(name = "first_name")
    public String firstName;

    @NotEmpty
    @Property(name = "last_name")
    public String lastName;
}
