package app.demo.api.customer;

import core.framework.api.json.Property;
import core.framework.api.validate.NotBlank;
import core.framework.api.validate.NotNull;

public class UpdateCustomerRequest {
    @NotNull
    @NotBlank
    @Property(name = "name")
    public String name;
}
