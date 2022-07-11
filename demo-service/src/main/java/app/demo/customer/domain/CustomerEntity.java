package app.demo.customer.domain;

import core.framework.api.validate.NotBlank;
import core.framework.api.validate.NotNull;
import core.framework.mongo.Collection;
import core.framework.mongo.Field;
import core.framework.mongo.Id;
import org.bson.types.ObjectId;

import java.time.ZonedDateTime;

/**
 * @author neo
 */
@Collection(name = "customer")
public class CustomerEntity {
    @Id
    public ObjectId id;

    @NotNull
    @NotBlank
    @Field(name = "email")
    public String email;

    @NotNull
    @NotBlank
    @Field(name = "name")
    public String name;

    @NotNull
    @Field(name = "updated_time")
    public ZonedDateTime updatedTime;
}
