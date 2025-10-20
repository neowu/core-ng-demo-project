package core.framework.json;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.introspect.VisibilityChecker;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.databind.util.StdDateFormat;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalTimeSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.ZonedDateTimeSerializer;
import com.fasterxml.jackson.module.afterburner.AfterburnerModule;
import com.fasterxml.jackson.module.blackbird.BlackbirdModule;
import core.framework.api.json.Property;
import core.framework.internal.json.JSONAnnotationIntrospector;
import core.framework.internal.json.JSONMapper;
import core.framework.internal.json.JSONReader;
import core.framework.util.ClasspathResources;
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

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;
import static java.time.format.DateTimeFormatter.ISO_INSTANT;
import static java.time.format.DateTimeFormatter.ISO_LOCAL_DATE;
import static java.time.temporal.ChronoField.HOUR_OF_DAY;
import static java.time.temporal.ChronoField.MINUTE_OF_HOUR;
import static java.time.temporal.ChronoField.NANO_OF_SECOND;
import static java.time.temporal.ChronoField.SECOND_OF_MINUTE;

/**
 * @author neo
 */
@State(Scope.Thread)
@BenchmarkMode(Mode.AverageTime)
@Warmup(iterations = 3, time = 3)
@Measurement(iterations = 10, time = 3)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
public class JSONDeserializeBenchmark {
    private ObjectReader noneReader;
    private JSONReader<TestBean> afterburnerReader;

    private byte[] body;

    // afterburner uses asm, blackbird uses LambdaMetafactory, so it doesn't support field access yet and LambdaMetafactory tends to be slower as well
    @Setup
    public void setup() throws JsonProcessingException {
        ObjectMapper mapper = createObjectMapper("none");
        JavaType type = mapper.getTypeFactory().constructType(TestBean.class);
        noneReader = mapper.readerFor(type);
        afterburnerReader = JSONMapper.reader(TestBean.class);

        body = ClasspathResources.bytes("json-test/test.json");
    }

    @Benchmark
    public void none() throws IOException {
        Object bean = noneReader.readValue(body);
    }

    @Benchmark
    public void afterburner() throws IOException {
        TestBean bean = afterburnerReader.fromJSON(body);
    }

    private ObjectMapper createObjectMapper(String mode) {
        JsonMapper.Builder builder = JsonMapper.builder();

        if ("blackbird".equals(mode)) {
            builder.addModule(new BlackbirdModule());
        } else if ("afterburner".equals(mode)) {
            // disable value class loader to avoid jdk illegal reflection warning, requires JSON class/fields must be public
            builder.addModule(new AfterburnerModule().setUseValueClassLoader(false));
        }

        return builder.addModule(timeModule())
                .defaultDateFormat(new StdDateFormat())
                // only auto detect field, and default visibility is public_only, refer to com.fasterxml.jackson.databind.introspect.VisibilityChecker.Std
                .visibility(new VisibilityChecker.Std(NONE, NONE, NONE, NONE, PUBLIC_ONLY))
                .enable(DeserializationFeature.FAIL_ON_NUMBERS_FOR_ENUMS)
                .disable(SerializationFeature.FAIL_ON_EMPTY_BEANS)
                .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
                .annotationIntrospector(new JSONAnnotationIntrospector())
                .deactivateDefaultTyping()
                .build();
    }

    private JavaTimeModule timeModule() {
        var module = new JavaTimeModule();

        // redefine date time formatter to output nano seconds in at least 3 digits, which inline with ISO standard and ES standard
        DateTimeFormatter localTimeFormatter = new DateTimeFormatterBuilder()
                .parseStrict()
                .appendValue(HOUR_OF_DAY, 2)
                .appendLiteral(':')
                .appendValue(MINUTE_OF_HOUR, 2)
                .appendLiteral(':')
                .appendValue(SECOND_OF_MINUTE, 2)
                .appendFraction(NANO_OF_SECOND, 3, 9, true) // always output 3 digits of nano seconds (iso date format doesn't specify how many digits it should present, here always keep 3)
                .toFormatter();

        module.addSerializer(ZonedDateTime.class, new ZonedDateTimeSerializer(ISO_INSTANT));
        module.addSerializer(LocalDateTime.class, new LocalDateTimeSerializer(new DateTimeFormatterBuilder()
                .parseStrict()
                .append(ISO_LOCAL_DATE)
                .appendLiteral('T')
                .append(localTimeFormatter)
                .toFormatter()));
        module.addSerializer(LocalTime.class, new LocalTimeSerializer(new DateTimeFormatterBuilder()
                .parseStrict()
                .append(localTimeFormatter)
                .toFormatter()));
        return module;
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
