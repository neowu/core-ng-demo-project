package core.kibana;

import java.util.ArrayList;
import java.util.List;

/**
 * @author neo
 */
public class TSVB {
    public String title;
    public String type = "metrics";
    public Params params = new Params();

    public TSVB(String id, String index) {
        title = id;
        params.id = id;
        params.index_pattern = index + "-*";
    }

    public static class Params {
        public String id;
        public String type = "timeseries";
        public String time_field = "@timestamp";
        public String index_pattern;
        public String axis_scale = "normal";
        public String axis_position = "left";
        public int show_legend = 0;
        public int show_grid = 1;
        public List<Series> series = new ArrayList<>();
        public String time_range_mode;
        // "filter": {"query": "stats.ws_active_channels: *", "language": "kuery"},
        public Filter filter;
    }

    public static class Filter {
        public String query;
        public String language = "kuery";
    }

    public static class Series {
        public String id;
        public String label;
        public String color;
        public String split_mode = "everything";
        public String terms_field;
        public String terms_order_by;
        public String split_color_mode;
        public int separate_axis = 0;
        public String axis_position;
        public String formatter = "number";
        public String chart_type = "line";
        public int line_width = 1;
        public int point_size = 1;
        public double fill = 0.5;
        public String value_template;
        public String stacked = "none";
        public List<Metric> metrics = new ArrayList<>();
    }

    public static class Metric {
        public String id;
        public String type;
        public String field;
        public List<Percentile> percentiles;

        public Metric(String type, String field) {
            this.id = type;
            this.type = type;
            this.field = field;
        }
    }

    public static class Percentile {
        public String id;
        public String mode = "line";
        public String value;
        public double shade = 0.2;
        public String percentile = "";

        public Percentile(String value) {
            this.id = value;
            this.value = value;
        }
    }
}
