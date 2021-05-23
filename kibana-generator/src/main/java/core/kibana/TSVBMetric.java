package core.kibana;

import java.util.ArrayList;
import java.util.List;

/**
 * @author neo
 */
public class TSVBMetric {
    public String title;
    public String type = "metrics";
    public Params params = new Params();

    public static class Params {
        public String id;
        public String type = "timeseries";
        public String time_field = "@timestamp";
        public String index_pattern;
        public String axis_formatter = "number";
        public String axis_scale = "normal";
        public String axis_position = "left";
        public int show_legend = 0;
        public int show_grid = 1;
        public List<Series> series = new ArrayList<>();
    }

    public static class Series {
        public String id;
        public String label;
        public String color;
        public String split_mode = "everything";
        public String terms_field;
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
    }

    public static class Percentile {
        public String id;
        public String mode = "line";
        public String value;
        public double shade = 0.2;
        public String percentile = "";
    }
}
