package app.demo.customer.domain;

import core.framework.db.DBEnumValue;

/**
 * @author neo
 */
public enum CustomerStatus {
    @DBEnumValue("A")
    ACTIVE,
    @DBEnumValue("I")
    INACTIVE
}
