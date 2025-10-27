package json;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.StreamReadFeature;
import com.fasterxml.jackson.core.StreamWriteFeature;
import com.fasterxml.jackson.core.util.JsonRecyclerPools;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
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
import com.fasterxml.jackson.module.blackbird.BlackbirdModule;
import core.framework.internal.json.JSONAnnotationIntrospector;
import core.framework.internal.json.JSONMapper;
import core.framework.internal.json.JSONReader;
import core.framework.util.ClasspathResources;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Warmup;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
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
@Fork(value = 1)
public class JSONDeserializeBenchmark {
    private ObjectReader noneReader;
    private static JavaTimeModule timeModule() {
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
    private ObjectReader blackbirdReader;

    private byte[] body;
    private JSONReader<TestJSON> afterburnerReader;

    // afterburner uses asm, blackbird uses LambdaMetafactory, so it doesn't support field access yet and LambdaMetafactory tends to be slower as well
    @Setup
    public void setup() throws JsonProcessingException {
        ObjectMapper mapper = createObjectMapper("none");
        noneReader = mapper.readerFor(mapper.getTypeFactory().constructType(TestJSON.class));

        ObjectMapper mapper1 = createObjectMapper("blackbird");
        blackbirdReader = mapper1.readerFor(mapper1.getTypeFactory().constructType(TestJSON.class));

        afterburnerReader = JSONMapper.reader(TestJSON.class);

        body = ClasspathResources.bytes("json-test/test.json");
    }

    @Benchmark
    public void none() throws IOException {
        TestJSON bean = noneReader.readValue(body);
    }

    @Benchmark
    public void blackbird() throws IOException {
        TestJSON bean = blackbirdReader.readValue(body);
    }

    @Benchmark
    public void afterburner() throws IOException {
        TestJSON bean = afterburnerReader.fromJSON(body);
    }

    private ObjectMapper createObjectMapper(String mode) {
        JsonFactory jsonFactory = JsonFactory.builder()
                .recyclerPool(JsonRecyclerPools.sharedConcurrentDequePool())
                .build();
        JsonMapper.Builder builder = JsonMapper.builder(jsonFactory);

        if ("blackbird".equals(mode)) {
            builder.addModule(new BlackbirdModule());
        }


        // refer to com.fasterxml.jackson.databind.ObjectMapper.DEFAULT_BASE for default settings, e.g. cacheProvider
        return builder
                .addModule(timeModule())
                .defaultDateFormat(new StdDateFormat())

                .visibility(new VisibilityChecker.Std(NONE, NONE, NONE, NONE, PUBLIC_ONLY))
                .enable(StreamReadFeature.USE_FAST_DOUBLE_PARSER)
                .enable(StreamReadFeature.USE_FAST_BIG_NUMBER_PARSER)
                .enable(StreamWriteFeature.USE_FAST_DOUBLE_WRITER)
                .enable(DeserializationFeature.FAIL_ON_NUMBERS_FOR_ENUMS)
                .disable(SerializationFeature.FAIL_ON_EMPTY_BEANS)
                .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
                // e.g. disable convert empty string to Integer null
                .disable(MapperFeature.ALLOW_COERCION_OF_SCALARS)
                .annotationIntrospector(new JSONAnnotationIntrospector())
                .deactivateDefaultTyping()
                .build();
    }
}
