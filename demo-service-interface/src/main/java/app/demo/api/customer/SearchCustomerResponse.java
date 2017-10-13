package app.demo.api.customer;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import java.util.List;

@XmlAccessorType(XmlAccessType.FIELD)
public class SearchCustomerResponse {
    @XmlElement(name = "total")
    public Integer total;

    @XmlElementWrapper(name = "customers")
    @XmlElement(name = "customer")
    public List<CustomerView> customers;
}
