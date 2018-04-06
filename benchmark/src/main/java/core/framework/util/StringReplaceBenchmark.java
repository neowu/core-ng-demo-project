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
@Warmup(iterations = 3)
@Measurement(iterations = 10)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
public class StringReplaceBenchmark {
    private String message;

    @Setup
    public void setup() {
        StringBuilder builder = new StringBuilder(11000);
        for (int i = 0; i < 500; i++) {
            builder.append("0123456789");
        }
        builder.append("password");
        for (int i = 0; i < 500; i++) {
            builder.append("0123456789");
        }
        message = builder.toString();
    }

    @Benchmark
    public void append() {
        int index = message.indexOf("password");
        StringBuilder builder = new StringBuilder();
        builder.append(message.substring(0, index));
        builder.append("******");
        builder.append(message.substring(index, message.length()));
        String maskedMessage = builder.toString();
    }

    @Benchmark
    public void append1() {
        int index = message.indexOf("password");
        StringBuilder builder = new StringBuilder();
        builder.append(message, 0, index);
        builder.append("******");
        builder.append(message, index, message.length());
        String maskedMessage = builder.toString();
    }

    @Benchmark
    public void replace() {
        int index = message.indexOf("password");
        StringBuilder builder = new StringBuilder(message);
        builder.replace(index, index + 8, "******");
        String maskedMessage = builder.toString();
    }
}
