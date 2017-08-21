package app.demo.customer.domain;

import core.framework.api.db.Column;
import core.framework.api.db.PrimaryKey;
import core.framework.api.db.Table;
import core.framework.api.validate.NotEmpty;
import core.framework.api.validate.NotNull;

import java.time.LocalDateTime;

@Table(name = "customer")
public class Customer {
    @PrimaryKey(autoIncrement = true)
    @Column(name = "id")
    public Long id;

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
