package core.framework.internal.template.model;

import java.util.List;

/**
 * @author neo
 */
public class SearchProductRequest {
    public String query;

    public Filter filter = new Filter();

    public Integer skip;

    public Integer limit;

    public List<Field> aggregations;

    public Sort sort;
}
