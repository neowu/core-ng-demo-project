package core.kibana;

import core.util.JSON;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static core.kibana.KibanaObject.visualization;

/**
 * @author neo
 */
public class KibanaObjectBuilder {
    private static final String[] COLOR_PALETTE = new String[]{
            "#F94144", "#F3722C", "#F8961E", "#F9844A", "#F9C74F",
            "#90BE6D", "#43AA8B", "#4D908E", "#577590", "#277DA1"
    };
    final List<KibanaObject> objects = new ArrayList<>();
    private int colorIndex;

    public String build() {
        buildVisualization();
        return JSON.toJSON(objects);
    }

    private void buildVisualization() {
        objects.add(visualization(splitByTerm("action-count-by-response_code", "context.response_code", true)));
        objects.add(visualization(splitByTerm("action-count-by-app", "app", false)));
        objects.add(visualization(splitByTerm("action-count-by-action", "action", true)));
        objects.add(visualization(elapsedByAction()));
        objects.add(visualization(percentile("action-elapsed", "elapsed", "elapsed", color(), new String[]{"99", "90", "50"})));
        objects.add(visualization(percentile("action-cpu_time", "stats.cpu_time", "cpu time", color(), new String[]{"99", "90", "50"})));

        objects.add(visualization(maxAvg("stat-sys_load", "stat", "stats.sys_load_avg", "sys load", color(), "number")));
        objects.add(visualization(maxAvg("stat-cpu_usage", "stat", "stats.cpu_usage", "cpu usage", color(), "percent")));

        objects.add(visualization(statMem()));

        objects.add(visualization(maxAvg("stat-thread", "stat", "stats.thread_count", "thread count", color(), "number")));

        objects.add(visualization(heap("jvm")));
        objects.add(visualization(gc("jvm")));

        objects.add(visualization(httpActiveRequests()));
        objects.add(visualization(businessUniqueVisitor()));

        addPerf("db", "rows", "db");
        objects.add(visualization(poolCount("db")));

        addPerf("redis", "keys", "redis");
        objects.add(visualization(poolCount("redis")));
        objects.add(visualization(max("stat-redis_keys", "stat", "stats.redis_keys", "keys", "number", color())));
        objects.add(visualization(usedMax("stat-redis_mem", "stats.redis_mem_max", "stats.redis_mem_used", "memory")));

        objects.add(visualization(poolCount("redis-cache")));
        objects.add(visualization(cacheHitRate()));
        objects.add(visualization(maxAvg("stat-cache_size", "stat", "stats.cache_size", "cache size", color(), "number")));

        objects.add(visualization(valueTemplate(maxAvg("action-consumer_delay", "action", "stats.consumer_delay", "consumer delay", color(), "number"), "{{value}} ns")));

        objects.add(visualization(kafkaConsumedRate()));
        objects.add(visualization(kafkaFetchRate(color())));
        objects.add(visualization(max("stat-kafka_max_lag", "stat", "stats.kafka_consumer_records_max_lag", "max lag", "number", color())));
        objects.add(visualization(kafkaProduceRate(null)));
        objects.add(visualization(kafkaRequestSize(null)));

        objects.add(visualization(kafkaProduceRate("log-forwarder")));
        objects.add(visualization(kafkaRequestSize("log-forwarder")));

        addPerf("kafka", "msgs", "kafka");

        objects.add(visualization(heap("kafka")));
        objects.add(visualization(gc("kafka")));
        objects.add(visualization(kafkaBytesRate()));
        objects.add(visualization(max("stat-kafka_disk", "stat", "stats.kafka_disk_used", "used disk size", "bytes", color())));

        addPerf("es", "docs", "elasticsearch");
        objects.add(visualization(usedMax("stat-es_disk", "stats.es_disk_max", "stats.es_disk_used", "disk")));
        objects.add(visualization(heap("es")));
        objects.add(visualization(gc("es")));
        objects.add(visualization(max("stat-es_docs", "stat", "stats.es_docs", "docs", "number", color())));

        addPerf("mongo", "docs", "mongo");

        objects.add(visualization(traceCountByResult()));

        objects.add(visualization(metric("business-customer_registered", "stats.customer_registered", "Customer Registered")));
        objects.add(visualization(metric("business-order_amount", "stats.order_amount", "Total Order Amount")));
        objects.add(visualization(metric("business-order_placed", "stats.order_placed", "Order Placed")));
    }

    private void addPerf(String name, String unit, String key) {
        objects.add(visualization(percentile("perf-" + name, "perf_stats." + key + ".total_elapsed", "total elapsed", color(), new String[]{"99", "90", "50"})));

        var tsvb = new TSVB("perf-" + name + "_io", "action");
        var write = series("sum", "perf_stats." + key + ".write_entries", "write " + unit, "number", color());
        write.value_template = "{{value}} " + unit;
        tsvb.params.series.add(write);
        var read = series("sum", "perf_stats." + key + ".read_entries", "read " + unit, "number", color());
        read.value_template = write.value_template;
        tsvb.params.series.add(read);
        var operations = series("sum", "perf_stats." + key + ".count", "operations", "number", color());
        operations.fill = 0;
        tsvb.params.series.add(operations);
        objects.add(visualization(tsvb));
    }

    private TSVB max(String id, String index, String field, String label, String formatter, String color) {
        var tsvb = new TSVB(id, index);
        tsvb.params.series.add(series("max", field, label, formatter, color));
        return tsvb;
    }

    private TSVB usedMax(String id, String maxField, String usedField, String label) {
        String color = color();
        TSVB tsvb = max(id, "stat", maxField, "max " + label, "bytes", color);
        tsvb.params.series.add(series("max", usedField, "used " + label, "bytes", color));
        return tsvb;
    }

    private TSVB cacheHitRate() {   // hard code color to make miss/hit contrast
        var tsvb = new TSVB("stat-cache_hit_rate", "action");
        var hits = series("sum", "stats.cache_hits", "cache hits", "number", "#43AA8B");
        hits.chart_type = "bar";
        hits.stacked = "stacked";
        tsvb.params.series.add(hits);
        var misses = series("sum", "stats.cache_misses", "cache misses", "number", "#F94144");
        misses.chart_type = "bar";
        misses.stacked = "stacked";
        tsvb.params.series.add(misses);
        return tsvb;
    }

    private TSVB httpActiveRequests() {
        var tsvb = new TSVB("stat-http_active_requests", "stat");
        tsvb.params.series.add(series("sum", "stats.http_active_requests", "active requests", "number", color()));
        return tsvb;
    }

    private TSVB kafkaConsumedRate() {
        var tsvb = new TSVB("stat-kafka_consumed_rate", "stat");
        var s1 = series("avg", "stats.kafka_consumer_bytes_consumed_rate", "bytes consumed rate", "bytes", color());
        s1.value_template = "{{value}}/s";
        s1.fill = 0;
        tsvb.params.series.add(s1);
        var s2 = series("avg", "stats.kafka_consumer_records_consumed_rate", "records consumed rate", "number", color());
        s2.value_template = "{{value}}/s";
        s2.separate_axis = 1;
        s2.axis_position = "right";
        s2.fill = 0;
        tsvb.params.series.add(s2);
        return tsvb;
    }

    private TSVB kafkaProduceRate(String name) {
        String postfix = name == null ? "" : "_" + name;
        var tsvb = new TSVB("stat-kafka" + postfix + "_produced_rate", "stat");
        var s1 = series("avg", "stats.kafka_producer" + postfix + "_outgoing_byte_rate", "outgoing bytes rate", "bytes", color());
        s1.value_template = "{{value}}/s";
        s1.fill = 0;
        tsvb.params.series.add(s1);
        var s2 = series("avg", "stats.kafka_producer" + postfix + "_request_rate", "request rate", "number", color());
        s2.value_template = "{{value}}/s";
        s2.separate_axis = 1;
        s2.axis_position = "right";
        s2.fill = 0;
        tsvb.params.series.add(s2);
        return tsvb;
    }

    private TSVB kafkaBytesRate() {
        var tsvb = new TSVB("stat-kafka_bytes_rate", "stat");
        var s1 = series("avg", "stats.kafka_bytes_in_rate", "bytes in rate", "bytes", color());
        s1.value_template = "{{value}}/s";
        s1.fill = 0;
        tsvb.params.series.add(s1);
        var s2 = series("avg", "stats.kafka_bytes_out_rate", "bytes out rate", "bytes", color());
        s2.value_template = "{{value}}/s";
        s2.fill = 0;
        tsvb.params.series.add(s2);
        return tsvb;
    }

    private TSVB kafkaRequestSize(String name) {
        String postfix = name == null ? "" : "_" + name;
        var tsvb = new TSVB("stat-kafka" + postfix + "_producer_request_size_avg", "stat");
        tsvb.params.series.add(series("avg", "stats.kafka_producer" + postfix + "_request_size_avg", "avg request size", "bytes", color()));
        return tsvb;
    }

    private TSVB kafkaFetchRate(String color) {
        var tsvb = new TSVB("stat-kafka_fetch_rate", "stat");
        var s1 = series("min", "stats.kafka_consumer_fetch_rate", "min fetch rate", "number", color);
        s1.value_template = "{{value}} req/s";
        tsvb.params.series.add(s1);
        var s2 = series("avg", "stats.kafka_consumer_fetch_rate", "avg fetch rate", "number", color);
        s2.value_template = "{{value}} req/s";
        tsvb.params.series.add(s2);
        return tsvb;
    }

    private TSVB businessUniqueVisitor() {
        var tsvb = new TSVB("business-unique_visitor", "action");
        tsvb.params.series.add(series("cardinality", "context.session_hash", "unique sessions", "number", color()));
        tsvb.params.series.add(series("cardinality", "context.client_ip", "unique client ips", "number", color()));
        return tsvb;
    }

    private TSVB statMem() {
        String color = color();
        TSVB tsvb = max("stat-mem", "stat", "stats.mem_max", "max mem", "bytes", color);
        tsvb.params.series.add(series("max", "stats.vm_rss", "vm rss", "bytes", color));
        return tsvb;
    }

    private TSVB poolCount(String name) {
        TSVB tsvb = max("stat-pool_" + name + "_count", "stat", "stats.pool_" + name + "_total_count", "total count", "number", color());
        tsvb.params.series.add(series("max", "stats.pool_" + name + "_active_count", "active count", "number", color()));
        return tsvb;
    }

    private TSVB heap(String name) {
        var tsvb = new TSVB("stat-" + name + "_heap", "stat");
        String color = color();
        tsvb.params.series.add(series("max", "stats." + name + "_heap_max", "max heap", "bytes", color));
        tsvb.params.series.add(series("max", "stats." + name + "_heap_used", "used heap", "bytes", color));
        var series = series("max", "stats." + name + "_non_heap_used", "used non heap", "bytes", color());
        series.fill = 0;
        tsvb.params.series.add(series);
        return tsvb;
    }

    private TSVB gc(String name) {
        var tsvb = new TSVB("stat-" + name + "_gc", "stat");
        var youngElapsed = series("sum", "stats." + name + "_gc_young_elapsed", "young gc elapsed", "number", color());
        youngElapsed.value_template = "{{value}} ns";
        var youngCount = series("sum", "stats." + name + "_gc_young_count", "young gc count", "number", color());
        youngCount.separate_axis = 1;
        youngCount.axis_position = "right";
        youngCount.fill = 0;

        var oldElapsed = series("sum", "stats." + name + "_gc_old_elapsed", "old gc elapsed", "number", color());
        oldElapsed.value_template = "{{value}} ns";
        var oldCount = series("sum", "stats." + name + "_gc_old_count", "old gc count", "number", color());
        oldCount.separate_axis = 1;
        oldCount.axis_position = "right";
        oldCount.fill = 0;

        tsvb.params.series.add(youngElapsed);
        tsvb.params.series.add(youngCount);
        tsvb.params.series.add(oldElapsed);
        tsvb.params.series.add(oldCount);
        return tsvb;
    }

    private TSVB splitByTerm(String id, String term, boolean stacked) {
        var tsvb = new TSVB(id, "action");
        tsvb.params.show_legend = 1;

        var series = new TSVB.Series();
        series.id = "count";
        series.label = "count";
        series.split_mode = "terms";
        series.split_color_mode = "rainbow";
        series.terms_field = term;
        if (stacked) {
            series.chart_type = "bar";
            series.stacked = "stacked";
        }
        series.metrics.add(new TSVB.Metric("count", null));
        tsvb.params.series.add(series);
        return tsvb;
    }

    private TSVB traceCountByResult() {
        var tsvb = new TSVB("trace-count-by-result", "trace");
        tsvb.params.show_legend = 1;

        var series = new TSVB.Series();
        series.id = "count";
        series.label = "count";
        series.split_mode = "terms";
        series.split_color_mode = "rainbow";
        series.terms_field = "result";
        series.chart_type = "bar";
        series.stacked = "stacked";
        series.metrics.add(new TSVB.Metric("count", null));
        tsvb.params.series.add(series);
        return tsvb;
    }

    private TSVB elapsedByAction() {
        var tsvb = new TSVB("action-elapsed-by-action", "action");
        tsvb.params.show_legend = 1;

        var series = new TSVB.Series();
        series.id = "elapsed";
        series.label = "elapsed";
        series.split_mode = "terms";
        series.split_color_mode = "rainbow";
        series.terms_field = "action";
        series.chart_type = "bar";
        series.value_template = "{{value}} ns";
        series.stacked = "stacked";
        series.metrics.add(new TSVB.Metric("sum", "elapsed"));
        tsvb.params.series.add(series);
        return tsvb;
    }

    private TSVB percentile(String id, String field, String label, String color, String[] percentiles) {
        var tsvb = new TSVB(id, "action");
        var series = new TSVB.Series();
        series.id = "percentile";
        series.label = label;
        series.color = color;
        series.value_template = "{{value}} ns";
        var metric = new TSVB.Metric("percentile", field);
        metric.percentiles = Arrays.stream(percentiles).map(TSVB.Percentile::new).collect(Collectors.toList());
        series.metrics.add(metric);
        tsvb.params.series.add(series);
        return tsvb;
    }

    private TSVB maxAvg(String id, String index, String field, String label, String color, String formatter) {
        var tsvb = new TSVB(id, index);
        tsvb.params.series.add(series("max", field, "max " + label, formatter, color));
        tsvb.params.series.add(series("avg", field, "avg " + label, formatter, color));
        return tsvb;
    }

    private TSVB valueTemplate(TSVB tsvb, String template) {
        for (TSVB.Series series : tsvb.params.series) {
            series.value_template = template;
        }
        return tsvb;
    }

    private TSVB.Series series(String type, String field, String label, String formatter, String color) {
        var avg = new TSVB.Series();
        avg.id = type + "-" + field.replace('.', '-');
        avg.label = label;
        avg.color = color;
        avg.formatter = formatter;
        avg.metrics.add(new TSVB.Metric(type, field));
        return avg;
    }

    private TSVB metric(String id, String field, String label) {
        var tsvb = new TSVB(id, "action");
        tsvb.params.time_range_mode = "entire_time_range";
        tsvb.params.type = "metric";
        var series = new TSVB.Series();
        series.id = id;
        series.label = label;
        series.metrics.add(new TSVB.Metric("sum", field));
        tsvb.params.series.add(series);
        return tsvb;
    }

    private String color() {
        return COLOR_PALETTE[colorIndex++ % COLOR_PALETTE.length];
    }
}
