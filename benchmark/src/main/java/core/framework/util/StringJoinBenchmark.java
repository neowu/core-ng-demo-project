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
@Warmup(iterations = 3)
@Measurement(iterations = 10)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
public class StringJoinBenchmark {
    private String[] items = new String[]{"item1", "item2", "item3", "item4"};

    @Benchmark
    public void stringJoin() {
        String.join(", ", items);
    }

    @Benchmark
    public void stringBuilderAppend() {
        StringBuilder builder = new StringBuilder();
        int index = 0;
        for (String item : items) {
            if (index > 0) builder.append(", ");
            builder.append(item);
            index++;
        }
        builder.toString();
    }

    @Benchmark
    public void stringBuilderRemove() {
        StringBuilder builder = new StringBuilder();
        for (String item : items) {
            builder.append(", ").append(item);
        }
        builder.deleteCharAt(builder.length() - 1);
        builder.toString();
    }
}
