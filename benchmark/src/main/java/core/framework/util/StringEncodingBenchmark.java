package core.framework.util;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Warmup;

import java.util.concurrent.TimeUnit;

/**
 * @author neo
 */
@State(Scope.Thread)
@BenchmarkMode(Mode.AverageTime)
@Warmup(iterations = 3, time = 3)
@Measurement(iterations = 10, time = 3)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
public class StringEncodingBenchmark {
    private final long[] numbers = new long[]{123456789, -1, 345678, 20, 1, 2, 3};

    @Benchmark
    public void jdkWithUTF8() {
        for (long number : numbers) {
            Strings.bytes(Long.toString(number));
        }
    }

    @Benchmark
    public void custom() {
        for (long number : numbers) {
            bytes(Long.toString(number));
        }
    }

//    @Benchmark
//    public void encodingWithCache() {
//        for (long number : numbers) {
//            RedisEncodings.encode(number);
//        }
//    }

    private byte[] bytes(String text) {
        char[] chars = text.toCharArray();
        byte[] bytes = new byte[chars.length];
        for (int i = 0; i < chars.length; i++) {
            bytes[i] = (byte) chars[i];
        }
        return bytes;
    }
}
