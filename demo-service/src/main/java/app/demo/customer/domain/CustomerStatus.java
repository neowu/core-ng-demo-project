package app.demo.customer.domain;

import core.framework.db.DBEnumValue;

/**
 * @author neo
 */
public enum CustomerStatus {
    @DBEnumValue("ACTIVE")
    ACTIVE,
    @DBEnumValue("INACTIVE")
    INACTIVE
}
