package core.framework.internal.template;

import core.framework.internal.template.model.FilterUIView;
import core.framework.internal.template.source.StringTemplateSource;
import core.framework.json.JSON;
import core.framework.util.ClasspathResources;
import core.framework.util.Properties;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Warmup;

import java.util.concurrent.TimeUnit;

/**
 * @author neo
 */
@State(Scope.Thread)
@BenchmarkMode(Mode.AverageTime)
@Warmup(iterations = 3)
@Measurement(iterations = 20)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
public class HTMLTemplateBenchmark {
    HTMLTemplate template;
    FilterUIView model;

    @Setup
    public void setup() {
        model = JSON.fromJSON(FilterUIView.class, ClasspathResources.text("template-test/filter.json"));

        Properties messages = new Properties();
        messages.load("template-test/plp_es_GT.properties");
        HTMLTemplateBuilder builder = new HTMLTemplateBuilder(new StringTemplateSource("filter", ClasspathResources.text("template-test/filter.html")), FilterUIView.class);
        builder.message = messages::get;
        template = builder.build();
    }

    @Benchmark
    public void current() {
        template.process(new TemplateContext(model, new CDNManager()));
    }
}
