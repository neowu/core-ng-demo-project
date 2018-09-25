package core.framework.impl.web;

import io.undertow.util.HeaderMap;
import io.undertow.util.Headers;
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
public class HttpStringBenchmark {
    private HeaderMap headers;

    @Setup
    public void setup() {
        headers = new HeaderMap();
        headers.put(Headers.CONTENT_TYPE, "application/json");
        headers.put(Headers.CONTENT_LENGTH, 1000);
        headers.put(HTTPHandler.HEADER_CLIENT, "customer-service");
        headers.put(HTTPHandler.HEADER_REF_ID, "123");
        headers.put(HTTPHandler.HEADER_TRACE, "true");
    }

    @Benchmark
    public void string() {
        headers.getFirst("client");
        headers.getFirst("content-type");
    }

    @Benchmark
    public void httpString() {
        headers.getFirst(HTTPHandler.HEADER_CLIENT);
        headers.getFirst(Headers.CONTENT_TYPE);
    }
}
