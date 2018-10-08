package core.framework.json;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.ObjectWriter;
import core.framework.api.json.Property;
import core.framework.util.Lists;
import core.framework.util.Maps;
import core.framework.util.Strings;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Warmup;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author neo
 */
@State(Scope.Thread)
@BenchmarkMode(Mode.AverageTime)
@Warmup(iterations = 3, time = 3)
@Measurement(iterations = 10, time = 3)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
public class JSONBenchmark {
    private ObjectReader reader;
    private ObjectWriter writer;
    private byte[] body;
    private TestBean bean;

    @Setup
    public void setup() throws JsonProcessingException {
        JavaType type = JSON.OBJECT_MAPPER.getTypeFactory().constructType(TestBean.class);
        reader = JSON.OBJECT_MAPPER.readerFor(type);
        writer = JSON.OBJECT_MAPPER.writerFor(type);

        bean = new TestBean();
        bean.stringField = "value1234567890value1234567890value1234567890value1234567890value1234567890value1234567890";
        bean.dateTimeField = LocalDateTime.now();
        bean.mapField.put("key", "value");
        bean.listField.add("value");
        bean.childrenField.add(new TestBean.Child());
        bean.childField = new TestBean.Child();
        bean.childField.booleanField = true;

        body = writer.writeValueAsBytes(bean);
    }

    @Benchmark
    public void withString() throws IOException {
//        Object bean = reader.readValue(new String(body, StandardCharsets.UTF_8));
//        byte[] json = writer.writeValueAsBytes(bean);
        byte[] bytes = Strings.bytes(writer.writeValueAsString(bean));
    }

    @Benchmark
    public void withBytes() throws IOException {
//        Object bean = reader.readValue(body);
        byte[] json = writer.writeValueAsBytes(bean);
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
}
