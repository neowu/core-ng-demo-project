package core.framework.util;

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
@Warmup(iterations = 3, time = 3)
@Measurement(iterations = 10, time = 3)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
public class HexEncodingBenchmark {
    private static final char[] HEX_CHARS = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};

    private static String hex2(byte[] bytes) {
        var values = new char[bytes.length * 2];
        int i = 0;
        for (byte value : bytes) {
            values[i++] = HEX_CHARS[value >> 4 & 0xF];
            values[i++] = HEX_CHARS[value & 0xF];
        }
        return new String(values);
    }

    private byte[] value;

    @Setup
    public void setup() {
        byte[] bytes = new byte[256];
        for (int i = 0; i < bytes.length; i++) {
            bytes[i] = (byte) i;
        }
        this.value = bytes;
    }

    @Benchmark
    public void hex() {
        Encodings.hex(value);
    }

    @Benchmark
    public void hex2() {
        hex2(value);
    }
}
