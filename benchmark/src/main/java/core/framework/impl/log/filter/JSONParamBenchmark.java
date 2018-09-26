package core.framework.impl.log.filter;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Warmup;

import java.nio.charset.StandardCharsets;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * @author neo
 */
@State(Scope.Thread)
@BenchmarkMode(Mode.AverageTime)
@Warmup(iterations = 3)
@Measurement(iterations = 10)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
public class JSONParamBenchmark {
    private byte[] message = "\"{\"field1\": \"value1\",\\n  \"password\": \"pass12356789\",\\n  \"passwordConfirm\": \"pass1234\",\\n  \"field2\": \"value2\",\\n  \"nested\": {\\n    \"password\": \"pass\\\"1234\",\\n    \"passwordConfirm\": \"pa34\"}}\"".getBytes();
    private Set<String> maskedFields = Set.of("passwordConfirm", "password");

    @Benchmark
    public void current() {
        JSONLogParam param = new JSONLogParam(message, StandardCharsets.UTF_8);
        param.append(new StringBuilder(), maskedFields, 1000);
    }

    @Benchmark
    public void oldV0() {
        JSONParamV0 param = new JSONParamV0(message, StandardCharsets.UTF_8);
        String masked = param.filter(maskedFields);
    }

    @Benchmark
    public void oldV1() {
        JSONParamV1 param = new JSONParamV1(message, StandardCharsets.UTF_8);
        String masked = param.filter(maskedFields);
    }
}
