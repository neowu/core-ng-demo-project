package core.framework.json;

import core.framework.api.json.Property;
import core.framework.internal.json.JSONAnnotationIntrospector;
import core.framework.internal.json.JSONMapper;
import core.framework.util.Lists;
import core.framework.util.Maps;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Warmup;
import tools.jackson.core.StreamReadFeature;
import tools.jackson.core.StreamWriteFeature;
import tools.jackson.databind.JavaType;
import tools.jackson.databind.MapperFeature;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.ObjectReader;
import tools.jackson.databind.ObjectWriter;
import tools.jackson.databind.cfg.EnumFeature;
import tools.jackson.databind.introspect.VisibilityChecker;
import tools.jackson.databind.json.JsonMapper;
import tools.jackson.databind.util.StdDateFormat;
import tools.jackson.module.afterburner.AfterburnerModule;
import tools.jackson.module.blackbird.BlackbirdModule;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * @author neo
 */
@State(Scope.Thread)
@BenchmarkMode(Mode.AverageTime)
@Warmup(iterations = 3, time = 3)
@Measurement(iterations = 10, time = 3)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
public class JSONBenchmark {
    private static ObjectMapper createObjectMapper(String mode) {
        JsonMapper.Builder builder = JsonMapper.builder();

        if ("blackbird".equals(mode)) {
            builder.addModule(new BlackbirdModule());
        } else if ("afterburner".equals(mode)) {
            // disable value class loader to avoid jdk illegal reflection warning, requires JSON class/fields must be public
            builder.addModule(new AfterburnerModule().setUseValueClassLoader(false));
        }

        return builder.addModule(JSONMapper.timeModule())
                .defaultDateFormat(new StdDateFormat())
                // only auto detect field, and default visibility is public_only, refer to com.fasterxml.jackson.databind.introspect.VisibilityChecker.Std
                // only detect public fields, refer to com.fasterxml.jackson.databind.introspect.VisibilityChecker.Std
                .changeDefaultVisibility(_ -> new VisibilityChecker(PUBLIC_ONLY, NONE, NONE, NONE, NONE, NONE))
                .enable(StreamWriteFeature.USE_FAST_DOUBLE_WRITER)
                .enable(StreamReadFeature.INCLUDE_SOURCE_IN_LOCATION)
                .disable(MapperFeature.SORT_PROPERTIES_ALPHABETICALLY)
                // e.g. disable convert empty string to Integer null
                .disable(MapperFeature.ALLOW_COERCION_OF_SCALARS)
                .enable(EnumFeature.FAIL_ON_NUMBERS_FOR_ENUMS)
                .disable(EnumFeature.WRITE_ENUMS_USING_TO_STRING)
                .annotationIntrospector(new JSONAnnotationIntrospector())
                .deactivateDefaultTyping()
                .build();
    }
    private ObjectReader noneReader;
    private ObjectWriter noneWriter;
    private ObjectReader afterburnerReader;
    private ObjectWriter afterburnerWriter;
    private ObjectReader blackbirdReader;
    private ObjectWriter blackbirdWriter;
    private byte[] body;
    private TestBean bean;

    static void main() {
        var bean = new TestBean();
        bean.stringField = "value1234567890value1234567890value1234567890value1234567890value1234567890value1234567890";
        bean.dateTimeField = LocalDateTime.now();
        bean.mapField.put("key", "value");
        bean.listField.add("value");
        bean.childrenField.add(new TestBean.Child());
        bean.childField = new TestBean.Child();
        bean.childField.booleanField = true;
        System.out.println(JSON.toJSON(bean));
    }

    @Benchmark
    public void none() throws IOException {
//        Object bean = reader.readValue(new String(body, StandardCharsets.UTF_8));
//        byte[] json = writer.writeValueAsBytes(bean);
//        byte[] bytes = Strings.bytes(writer.writeValueAsString(bean));

        Object bean = noneReader.readValue(body);
        noneWriter.writeValueAsString(bean);
    }

    // afterburner uses asm, blackbird uses LambdaMetafactory, so it doesn't support field access yet and LambdaMetafactory tends to be slower as well
    @Setup
    public void setup() {
        ObjectMapper mapper = createObjectMapper("none");
        JavaType type = mapper.getTypeFactory().constructType(TestBean.class);
        noneReader = mapper.readerFor(type);
        noneWriter = mapper.writerFor(type);

        mapper = createObjectMapper("afterburner");
        type = mapper.getTypeFactory().constructType(TestBean.class);
        afterburnerReader = mapper.readerFor(type);
        afterburnerWriter = mapper.writerFor(type);

        mapper = createObjectMapper("blackbird");
        type = mapper.getTypeFactory().constructType(TestBean.class);
        blackbirdReader = mapper.readerFor(type);
        blackbirdWriter = mapper.writerFor(type);

        bean = new TestBean();
        bean.stringField = "value1234567890value1234567890value1234567890value1234567890value1234567890value1234567890";
        bean.dateTimeField = LocalDateTime.now();
        bean.mapField.put("key", "value");
        bean.listField.add("value");
        bean.childrenField.add(new TestBean.Child());
        bean.childField = new TestBean.Child();
        bean.childField.booleanField = true;

        body = noneWriter.writeValueAsBytes(bean);
    }

    @Benchmark
    public void blackbird() {
        Object bean = blackbirdReader.readValue(body);
        blackbirdWriter.writeValueAsString(bean);
    }

    public static class TestBean {
        @Property(name = "map")
        public final Map<String, String> mapField = Maps.newHashMap();

        @Property(name = "list")
        public final List<String> listField = Lists.newArrayList();

        @Property(name = "children")
        public final List<Child> childrenField = Lists.newArrayList();

        @Property(name = "child")
        public Child childField;

        @Property(name = "string")
        public String stringField;

        @Property(name = "date")
        public LocalDate dateField;

        @Property(name = "dateTime")
        public LocalDateTime dateTimeField;

        @Property(name = "zonedDateTime")
        public ZonedDateTime zonedDateTimeField;

        @Property(name = "instant")
        public Instant instantField;

        public Integer notAnnotatedField;

        @Property(name = "empty")
        public Empty empty;

        @Property(name = "defaultValue")
        public String defaultValueField = "defaultValue";

        public enum TestEnum {
            @Property(name = "A1")
            A,
            @Property(name = "B1")
            B,
            C
        }

        public static class Child {
            @Property(name = "boolean")
            public Boolean booleanField;

            @Property(name = "long")
            public Long longField;
        }

        public static class Empty {

        }
    }

    @Benchmark
    public void afterburner() {
        Object bean = afterburnerReader.readValue(body);
        afterburnerWriter.writeValueAsString(bean);
    }
}
