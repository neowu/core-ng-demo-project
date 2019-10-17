package core.framework.util;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Warmup;

import java.text.DecimalFormat;
import java.util.concurrent.TimeUnit;

/**
 * @author neo
 */
@State(Scope.Thread)
@BenchmarkMode(Mode.AverageTime)
@Warmup(iterations = 3, time = 3)
@Measurement(iterations = 10, time = 3)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
public class DecimalFormatBenchmark {
    private final double n1 = 4294967296d;
    private final double n2 = 0.4294967296d;

    @Benchmark
    public void stringFormat() {
        String.format("%.9f", n1);
        String.format("%.9f", n2);
    }

    @Benchmark
    public void decimalFormat() {
        DecimalFormat format = new DecimalFormat();
        format.format(n1);
        format.format(n2);
    }
}
