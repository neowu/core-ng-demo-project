package app.demo.api.customer;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

@XmlAccessorType(XmlAccessType.FIELD)
public class SearchCustomerRequest {
    @XmlElement(name = "skip")
    public Integer skip = 0;

    @XmlElement(name = "limit")
    public Integer limit = 1000;

    @XmlElement(name = "email")
    public String email;

    @XmlElement(name = "first_name")
    public String firstName;

    @XmlElement(name = "last_name")
    public String lastName;
}
