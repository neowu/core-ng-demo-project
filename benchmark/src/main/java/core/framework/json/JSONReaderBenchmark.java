package core.framework.json;

import core.framework.internal.json.JSONMapper;
import core.framework.internal.json.JSONReader;
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

import java.util.concurrent.TimeUnit;

@State(Scope.Thread)
@BenchmarkMode(Mode.AverageTime)
@Warmup(iterations = 3, time = 3)
@Measurement(iterations = 10, time = 3)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
public class JSONReaderBenchmark {
    private JSONReader<JSONBenchmark.TestBean> reader;
    private String value;
    private byte[] bytes;

    @Setup
    public void setup() {
        reader = JSONMapper.reader(JSONBenchmark.TestBean.class);
        value = """
                {"notAnnotatedField":null,"map":{"key":"value"},"list":["value"],"children":[{"boolean":null,"long":null}],"child":{"boolean":true,"long":null},"string":"value1234567890value1234567890value1234567890value1234567890value1234567890value1234567890","date":null,"dateTime":"2026-04-06T21:59:00.765373","zonedDateTime":null,"instant":null,"empty":null,"defaultValue":"defaultValue"}
                """;
        bytes = Strings.bytes(value);
    }

    @Benchmark
    public void readString() {
        reader.fromJSON(value);
    }

    @Benchmark
    public void readStringConvertBytes() {
        byte[] bytes = Strings.bytes(value);
        reader.fromJSON(bytes);
    }

    @Benchmark
    public void readDirectlyBytes() {
        reader.fromJSON(bytes);
    }
}
