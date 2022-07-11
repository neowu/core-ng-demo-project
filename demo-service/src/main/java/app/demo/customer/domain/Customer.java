package app.demo.customer.domain;

import core.framework.api.validate.NotBlank;
import core.framework.api.validate.NotNull;
import core.framework.db.Column;
import core.framework.db.PrimaryKey;
import core.framework.db.Table;

import java.time.ZonedDateTime;

@Table(name = "customer")
public class Customer {
    @PrimaryKey(autoIncrement = true)
    @Column(name = "id")
    public Long id;

    @NotNull
    @Column(name = "status")
    public CustomerStatus status;

    @NotNull
    @NotBlank
    @Column(name = "email")
    public String email;

    @NotBlank
    @Column(name = "name")
    public String name;

    @NotNull
    @Column(name = "updated_time")
    public ZonedDateTime updatedTime;
}
