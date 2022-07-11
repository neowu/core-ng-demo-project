package app.demo.api.customer;

import core.framework.api.json.Property;
import core.framework.api.validate.NotBlank;
import core.framework.api.validate.NotNull;

import java.time.ZonedDateTime;

public class CustomerView {
    @NotNull
    @Property(name = "id")
    public String id;

    @NotNull
    @NotBlank
    @Property(name = "email")
    public String email;

    @NotBlank
    @Property(name = "name")
    public String name;

    @NotNull
    @Property(name = "updatedTime")
    public ZonedDateTime updatedTime;
}
