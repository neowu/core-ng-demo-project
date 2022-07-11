package app.demo.api.customer;

import core.framework.api.json.Property;
import core.framework.api.validate.NotBlank;
import core.framework.api.validate.NotNull;

public class CreateCustomerRequest {
    @NotNull
    @NotBlank
    @Property(name = "email")
    public String email;

    @NotBlank
    @Property(name = "name")
    public String name;
}
