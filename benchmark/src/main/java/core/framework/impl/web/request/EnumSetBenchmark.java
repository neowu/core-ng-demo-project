package core.framework.impl.web.request;

import core.framework.http.HTTPMethod;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Warmup;

import java.util.EnumSet;
import java.util.concurrent.TimeUnit;

/**
 * @author neo
 */
@State(Scope.Thread)
@BenchmarkMode(Mode.AverageTime)
@Warmup(iterations = 3, time = 3)
@Measurement(iterations = 10, time = 3)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
public class EnumSetBenchmark {
    private final EnumSet<HTTPMethod> bodyMethods = EnumSet.of(HTTPMethod.POST, HTTPMethod.PUT, HTTPMethod.PATCH);
    private final HTTPMethod[] methods = new HTTPMethod[]{HTTPMethod.POST, HTTPMethod.PUT, HTTPMethod.GET};

    @Benchmark
    public void enumSet() {
        boolean result;
        for (HTTPMethod method : methods) {
            result = bodyMethods.contains(method);
        }
    }

    @Benchmark
    public void condition() {
        boolean result;
        for (HTTPMethod method : methods) {
            result = method == HTTPMethod.POST || method == HTTPMethod.PUT || method == HTTPMethod.PATCH;
        }
    }
}
