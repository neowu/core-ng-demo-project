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
        objects.add(visualization(splitByTerm("action-count-by-response_code", "context.response_code")));
        objects.add(visualization(splitByTerm("action-count-by-app", "app")));
        objects.add(visualization(splitByTerm("action-count-by-action", "action")));
        objects.add(visualization(elapsedByAction()));
        objects.add(visualization(percentile("action-elapsed", "elapsed", "elapsed", color(), new String[]{"99", "90", "50"})));
        objects.add(visualization(percentile("action-cpu_time", "stats.cpu_time", "cpu time", color(), new String[]{"99", "90", "50"})));

        objects.add(visualization(maxAvg("stat-sys_load", "stat", "stats.sys_load_avg", "sys load", color(), "number")));
        objects.add(visualization(maxAvg("stat-cpu_usage", "stat", "stats.cpu_usage", "cpu usage", color(), "percent")));

        objects.add(visualization(statMem()));

        objects.add(visualization(maxAvg("stat-thread", "stat", "stats.thread_count", "thread count", color(), "number")));

        objects.add(visualization(heap("jvm")));
        objects.add(visualization(gc("jvm")));

        objects.add(visualization(maxAvg("stat-http_active_requests", "stat", "stats.http_active_requests", "active http requests", color(), "number")));
        objects.add(visualization(valueTemplate(maxAvg("action-http_delay", "action", "stats.http_delay", "http delay", color(), "number"), "{{value}} ns")));
        objects.add(visualization(actionHTTPIO()));

        objects.add(visualization(percentile("perf-http_client", "perf_stats.http.total_elapsed", "total elapsed", color(), new String[]{"99", "90", "50"})));
        objects.add(visualization(percentile("perf-http_client_dns", "perf_stats.http_dns.total_elapsed", "total elapsed", color(), new String[]{"99", "90", "50"})));
        objects.add(visualization(percentile("perf-http_client_conn", "perf_stats.http_conn.total_elapsed", "total elapsed", color(), new String[]{"99", "90", "50"})));
        objects.add(visualization(perfHTTPIO()));
        objects.add(visualization(httpRetries()));

        addPerf("db", "rows", "db");
        objects.add(visualization(poolCount("db")));
        objects.add(visualization(dbQueries()));

        addPerf("redis", "keys", "redis");
        objects.add(visualization(poolCount("redis")));
        objects.add(visualization(max("stat-redis_keys", "stat", "stats.redis_keys", "keys", "number", color())));
        objects.add(visualization(usedMax("stat-redis_mem", "stats.redis_mem_max", "stats.redis_mem_used", "memory")));

        objects.add(visualization(poolCount("redis-cache")));
        objects.add(visualization(poolCount("redis-session")));
        objects.add(visualization(cacheHitRate()));
        objects.add(visualization(maxAvg("stat-cache_size", "stat", "stats.cache_size", "cache size", color(), "number")));

        objects.add(visualization(valueTemplate(maxAvg("action-consumer_delay", "action", "stats.consumer_delay", "consumer delay", color(), "ns,s,"), "{{value}} s")));
        objects.add(visualization(valueTemplate(maxAvg("action-task_delay", "action", "stats.task_delay", "task delay", color(), "ns,s,"), "{{value}} s")));

        objects.add(visualization(kafkaConsumerConsumedRate()));
        objects.add(visualization(kafkaConsumerFetchRate(color())));
        objects.add(visualization(max("stat-kafka_consumer_max_lag", "stat", "stats.kafka_consumer_records_max_lag", "max lag", "number", color())));
        objects.add(visualization(kafkaProducerProducedRate(null)));
        objects.add(visualization(kafkaProducerRequestSize(null)));
        objects.add(visualization(max("stat-kafka_max_message_size", "action", "stats.kafka_max_message_size", "max message size", "bytes", color())));

        objects.add(visualization(kafkaProducerProducedRate("log-forwarder")));
        objects.add(visualization(kafkaProducerRequestSize("log-forwarder")));

        addPerf("kafka", "msgs", "kafka");
        objects.add(visualization(heap("kafka")));
        objects.add(visualization(gc("kafka")));
        objects.add(visualization(kafkaBytesRate()));
        objects.add(visualization(max("stat-kafka_disk", "stat", "stats.kafka_disk_used", "used disk size", "bytes", color())));

        addPerf("es", "docs", "elasticsearch");
        objects.add(visualization(maxAvg("stat-es_cpu_usage", "stat", "stats.es_cpu_usage", "cpu usage", color(), "percent")));
        objects.add(visualization(usedMax("stat-es_disk", "stats.es_disk_max", "stats.es_disk_used", "disk")));
        objects.add(visualization(heap("es")));
        objects.add(visualization(gc("es")));
        objects.add(visualization(max("stat-es_docs", "stat", "stats.es_docs", "docs", "number", color())));

        addPerf("mongo", "docs", "mongo");
        objects.add(visualization(poolCount("mongo")));
        objects.add(visualization(mongoObjects()));
        objects.add(visualization(usedMax("stat-mongo_disk", "stats.mongo_disk_max", "stats.mongo_disk_used", "disk")));

        objects.add(visualization(traceCountByResult()));

        objects.add(visualization(businessUniqueVisitor()));
        objects.add(visualization(metric("business-customer_registered", "stats.customer_registered", "Customer Registered")));
        objects.add(visualization(metric("business-order_amount", "stats.order_amount", "Total Order Amount")));
        objects.add(visualization(metric("business-order_placed", "stats.order_placed", "Order Placed")));
    }

    private TSVB mongoObjects() {
        TSVB tsvb = max("stat-mongo_objects", "stat", "stats.mongo_total_size", "total size", "bytes", color());
        TSVB.Series series = series("max", "stats.mongo_objects", "objects", "number", color());
        series.separate_axis = 1;
        series.axis_position = "right";
        series.fill = 0;
        tsvb.params.series.add(series);
        return tsvb;
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
        operations.separate_axis = 1;
        operations.axis_position = "right";
        operations.fill = 0;
        tsvb.params.series.add(operations);
        objects.add(visualization(tsvb));
    }

    private TSVB perfHTTPIO() {
        var tsvb = new TSVB("perf-http_client_io", "action");
        tsvb.params.show_grid = 0;
        tsvb.params.series.add(series("sum", "perf_stats.http.read_entries", "response body length", "bytes", color()));
        tsvb.params.series.add(series("sum", "perf_stats.http.write_entries", "request body length", "bytes", color()));
        var s1 = series("sum", "perf_stats.http.count", "http", "number", color());
        s1.separate_axis = 1;
        s1.axis_position = "right";
        s1.fill = 0;
        tsvb.params.series.add(s1);
        return tsvb;
    }

    private TSVB httpRetries() {
        var tsvb = new TSVB("action-http_client_retries", "action");
        TSVB.Series s1 = series("sum", "stats.http_retries", "http retries", "number", color());
        s1.chart_type = "bar";
        tsvb.params.series.add(s1);
        return tsvb;
    }

    private TSVB dbQueries() {
        var tsvb = new TSVB("action-db_queries", "action");
        tsvb.params.series.add(series("sum", "stats.db_queries", "db queries", "number", color()));
        return tsvb;
    }

    private TSVB actionHTTPIO() {
        var tsvb = new TSVB("action-http_io", "action");
        tsvb.params.series.add(series("sum", "stats.request_body_length", "request body length", "bytes", color()));
        tsvb.params.series.add(series("sum", "stats.response_body_length", "response body length", "bytes", color()));
        return tsvb;
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
        var tsvb = new TSVB("action-cache_hit_rate", "action");
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

    private TSVB kafkaConsumerFetchRate(String color) {
        var tsvb = new TSVB("stat-kafka_consumer_fetch_rate", "stat");
        var s1 = series("avg", "stats.kafka_consumer_fetch_rate", "avg fetch rate", "number", color);
        s1.value_template = "{{value}} req/s";
        tsvb.params.series.add(s1);
        var s2 = series("min", "stats.kafka_consumer_fetch_rate", "min fetch rate", "number", color);
        s2.value_template = "{{value}} req/s";
        tsvb.params.series.add(s2);
        return tsvb;
    }

    private TSVB kafkaConsumerConsumedRate() {
        var tsvb = new TSVB("stat-kafka_consumer_consumed_rate", "stat");
        tsvb.params.show_grid = 0;
        var s1 = series("avg", "stats.kafka_consumer_bytes_consumed_rate", "bytes consumed rate", "bytes", color());
        s1.value_template = "{{value}}/s";
        s1.separate_axis = 1;
        s1.axis_position = "left";  // seems only separate axis respect value_template
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

    private TSVB kafkaProducerProducedRate(String name) {
        String postfix = name == null ? "" : "_" + name;
        var tsvb = new TSVB("stat-kafka_producer" + postfix + "_produced_rate", "stat");
        tsvb.params.show_grid = 0;
        var s1 = series("avg", "stats.kafka_producer" + postfix + "_outgoing_byte_rate", "outgoing bytes rate", "bytes", color());
        s1.value_template = "{{value}}/s";
        s1.separate_axis = 1;
        s1.axis_position = "left";  // seems only separate axis respect value_template
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

    private TSVB kafkaProducerRequestSize(String name) {
        String postfix = name == null ? "" : "_" + name;
        var tsvb = new TSVB("stat-kafka_producer" + postfix + "_request_size", "stat");
        tsvb.params.series.add(series("max", "stats.kafka_producer" + postfix + "_request_size_max", "max request size", "bytes", color()));
        tsvb.params.series.add(series("avg", "stats.kafka_producer" + postfix + "_request_size_avg", "avg request size", "bytes", color()));
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
        tsvb.params.show_grid = 0;
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

    private TSVB splitByTerm(String id, String term) {
        var tsvb = new TSVB(id, "action");
        tsvb.params.show_legend = 1;

        var series = new TSVB.Series();
        series.color = "#000000";
        series.id = "count";
        series.label = "count";
        series.split_mode = "terms";
        series.split_color_mode = "rainbow";
        series.terms_field = term;
        series.terms_order_by = "count";
        series.chart_type = "bar";
        series.stacked = "stacked";
        series.metrics.add(new TSVB.Metric("count", null));
        tsvb.params.series.add(series);
        return tsvb;
    }

    private TSVB traceCountByResult() {
        var tsvb = new TSVB("trace-count-by-result", "trace");
        tsvb.params.show_legend = 1;

        var series = new TSVB.Series();
        series.color = "#000000";
        series.id = "count";
        series.label = "count";
        series.split_mode = "terms";
        series.split_color_mode = "rainbow";
        series.terms_field = "result";
        series.terms_order_by = "count";
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
        series.color = "#000000";
        series.id = "elapsed";
        series.label = "elapsed";
        series.split_mode = "terms";
        series.split_color_mode = "rainbow";
        series.terms_field = "action";
        series.terms_order_by = "sum";
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
