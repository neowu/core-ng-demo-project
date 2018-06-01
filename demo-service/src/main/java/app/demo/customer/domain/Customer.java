package app.demo.customer.domain;

import core.framework.api.validate.NotEmpty;
import core.framework.api.validate.NotNull;
import core.framework.db.Column;
import core.framework.db.PrimaryKey;
import core.framework.db.Table;

import java.time.LocalDateTime;

@Table(name = "customer")
public class Customer {
    @PrimaryKey(autoIncrement = true)
    @Column(name = "id")
    public Long id;

    @NotNull
    @Column(name = "status")
    public CustomerStatus status;

    @NotNull
    @NotEmpty
    @Column(name = "email")
    public String email;

    @NotNull
    @NotEmpty
    @Column(name = "first_name")
    public String firstName;

    @NotEmpty
    @Column(name = "last_name")
    public String lastName;

    @NotNull
    @Column(name = "updated_time")
    public LocalDateTime updatedTime;
}
