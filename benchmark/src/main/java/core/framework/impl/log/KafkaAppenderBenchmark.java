package core.framework.impl.log;

import core.framework.internal.log.message.ActionLogMessage;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Warmup;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * @author neo
 */
@State(Scope.Thread)
@BenchmarkMode(Mode.AverageTime)
@Warmup(iterations = 3, time = 3)
@Measurement(iterations = 10, time = 3)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
public class KafkaAppenderBenchmark {
    private final BlockingQueue<Object> array = new ArrayBlockingQueue<>(100);
    private final BlockingQueue<Object> linked = new LinkedBlockingQueue<>();

    @Benchmark
    public void array() throws InterruptedException {
        for (int i = 0; i < 30; i++) {
            array.offer(new ActionLogMessage());
        }
        for (int i = 0; i < 30; i++) {
            array.take();
        }
    }

    @Benchmark
    public void linked() throws InterruptedException {
        for (int i = 0; i < 30; i++) {
            linked.offer(new ActionLogMessage());
        }
        for (int i = 0; i < 30; i++) {
            linked.take();
        }
    }
}
