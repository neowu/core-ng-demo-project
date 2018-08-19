package core.framework.impl.log;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Warmup;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author neo
 */
@State(Scope.Thread)
@BenchmarkMode(Mode.AverageTime)
@Warmup(iterations = 3, time = 3)
@Measurement(iterations = 10, time = 3)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
public class ActionLogBenchmark {
    @Benchmark
    public void arrayList() {
        LogEvent event = new LogEvent("logger", null, LogLevel.DEBUG, "message", null, null);
        List<LogEvent> events = new ArrayList<>(16);
        for (int i = 0; i < 3000; i++) {
            events.add(event);
        }
        for (LogEvent e : events) {
            events.size();
        }
    }

    @Benchmark
    public void linkedList() {
        LogEvent event = new LogEvent("logger", null, LogLevel.DEBUG, "message", null, null);
        List<LogEvent> events = new LinkedList<>();
        for (int i = 0; i < 3000; i++) {
            events.add(event);
        }
        for (LogEvent e : events) {
            events.size();
        }
    }
}
