package app.demo.api.customer;

import core.framework.api.validate.NotEmpty;
import core.framework.api.validate.NotNull;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import java.time.LocalDateTime;

@XmlAccessorType(XmlAccessType.FIELD)
public class CustomerView {
    @NotNull
    @XmlElement(name = "id")
    public Long id;

    @NotNull
    @NotEmpty
    @XmlElement(name = "email")
    public String email;

    @NotNull
    @NotEmpty
    @XmlElement(name = "first_name")
    public String firstName;

    @NotEmpty
    @XmlElement(name = "last_name")
    public String lastName;

    @NotNull
    @XmlElement(name = "updated_time")
    public LocalDateTime updatedTime;
}
