package core.kibana;

import core.util.JSON;

import java.util.ArrayList;
import java.util.List;

/**
 * @author neo
 */
public class KibanaObjectBuilder {
    public String build() {
        List<KibanaObject> objects = new ArrayList<>();
        buildVisualization(objects);
        return JSON.toJSON(objects);
    }

    private void buildVisualization(List<KibanaObject> objects) {
        KibanaObject visualization = visualization("action-count-by-response_code", visState("action-count-by-response_code"));


        objects.add(visualization);
    }

    private String visState(String id) {
        var visState = new TSVBMetric();
        visState.title = id;
        visState.params.id = id;
        visState.params.index_pattern = "action-*";
        var series = new TSVBMetric.Series();
        series.id = "count";
        series.label = "count";
        series.color = "#68BC00";
        series.split_mode = "terms";
        series.split_color_mode = "rainbow";
        series.terms_field = "context.response_code";
        series.chart_type = "bar";
        series.stacked = "stacked";
        var metric = new TSVBMetric.Metric();
        metric.id = "count";
        metric.type = "count";
        series.metrics.add(metric);
        visState.params.series.add(series);
        return JSON.toJSON(visState);
    }

    private KibanaObject visualization(String id, String visState) {
        var visualization = new KibanaObject();
        visualization.id = id;
        visualization.type = "visualization";
        visualization.attributes.title = id;
        visualization.attributes.visState = visState;
        return visualization;
    }
}
