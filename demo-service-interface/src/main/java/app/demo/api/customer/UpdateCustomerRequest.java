package app.demo.api.customer;

import core.framework.api.validate.NotEmpty;
import core.framework.api.validate.NotNull;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

@XmlAccessorType(XmlAccessType.FIELD)
public class UpdateCustomerRequest {
    @NotNull
    @NotEmpty
    @XmlElement(name = "first_name")
    public String firstName;

    @NotEmpty
    @XmlElement(name = "last_name")
    public String lastName;
}
