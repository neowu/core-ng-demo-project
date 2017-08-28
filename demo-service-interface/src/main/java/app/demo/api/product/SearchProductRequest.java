package app.demo.api.product;

import core.framework.api.validate.NotNull;
import core.framework.api.web.service.QueryParam;

/**
 * @author neo
 */
public class SearchProductRequest {
    @NotNull(message = "name is required")
    @QueryParam(name = "name")
    public String name;

    @QueryParam(name = "desc")
    public String description;
}
