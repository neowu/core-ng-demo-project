package app.demo.api.product.kafka;

import core.framework.api.validate.NotEmpty;
import core.framework.api.validate.NotNull;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

/**
 * @author neo
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class ProductUpdatedMessage {
    @NotNull(message = "id is required")
    @XmlElement(name = "id")
    public String id;

    @NotEmpty
    @XmlElement(name = "name")
    public String name;

    @XmlElement(name = "desc")
    public String desc;
}
