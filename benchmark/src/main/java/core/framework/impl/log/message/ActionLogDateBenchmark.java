package core.framework.impl.log.message;

import core.framework.json.JSON;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Warmup;

import java.time.Instant;
import java.time.ZonedDateTime;
import java.util.concurrent.TimeUnit;

/**
 * @author neo
 */
@State(Scope.Thread)
@BenchmarkMode(Mode.AverageTime)
@Warmup(iterations = 3, time = 3)
@Measurement(iterations = 10, time = 3)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
public class ActionLogDateBenchmark {
    @Benchmark
    public void zonedDateTimeJSON() {
        JSON.toJSON(ZonedDateTime.now());
    }

    @Benchmark
    public void instantJSON() {
        JSON.toJSON(Instant.now());
    }

    @Benchmark
    public void zonedDateTime() {
        ZonedDateTime.now();
    }

    @Benchmark
    public void instant() {
        Instant.now();
    }
}
