package core.framework.impl.db;

import core.framework.util.ASCII;
import core.framework.util.Maps;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Warmup;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.TimeUnit;

/**
 * @author neo
 */
@State(Scope.Thread)
@BenchmarkMode(Mode.AverageTime)
@Warmup(iterations = 3, time = 3)
@Measurement(iterations = 10, time = 3)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
public class ResultSetIndexBenchmark {
    private final List<String> columns = List.of("id", "status", "email", "first_name", "last_name", "updated_time");

    @Benchmark
    public void hashMap() {
        int size = columns.size();
        Map<String, Integer> index = Maps.newHashMapWithExpectedSize(size);
        for (int i = 0; i < size; i++) {
            String column = columns.get(i);
            index.put(ASCII.toLowerCase(column), i);
        }
        index.get(ASCII.toLowerCase("ID"));
        index.get(ASCII.toLowerCase("status"));
        index.get(ASCII.toLowerCase("email"));
        index.get(ASCII.toLowerCase("FIRST_NAME"));
        index.get(ASCII.toLowerCase("last_name"));
        index.get(ASCII.toLowerCase("UPDATED_TIME"));
    }

    @Benchmark
    public void treeMap() {
        int size = columns.size();
        Map<String, Integer> index = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
        for (int i = 0; i < size; i++) {
            String column = columns.get(i);
            index.put(column, i);
        }
        index.get("ID");
        index.get("status");
        index.get("email");
        index.get("FIRST_NAME");
        index.get("last_name");
        index.get("UPDATED_TIME");
    }
}
