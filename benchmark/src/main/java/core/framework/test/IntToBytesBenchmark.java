package core.framework.test;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Warmup;

import java.nio.charset.StandardCharsets;

/**
 * @author neo
 */
@State(Scope.Thread)
@BenchmarkMode(Mode.Throughput)
@Warmup(iterations = 3)
@Measurement(iterations = 10)
public class IntToBytesBenchmark {
    @Benchmark
    public void jdk() {
        String.valueOf(100099).getBytes(StandardCharsets.US_ASCII);
    }

    @Benchmark
    public void custom() {
        IntToBytes.convert(100099);
    }
}
