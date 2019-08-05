package core.framework.impl.web.http;

import core.framework.module.IPv4RangeFileParser;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Warmup;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @author neo
 */
@State(Scope.Thread)
@BenchmarkMode(Mode.AverageTime)
@Warmup(iterations = 3, time = 3)
@Measurement(iterations = 10, time = 3)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
public class IPRangeBenchmark {
    private List<CIDR> cidrs;
    private IPv4Ranges ranges;

    @Setup
    public void setup() {
        List<String> cidrs = new IPv4RangeFileParser("ip-range-test/cidrs.txt").parse();
        ranges = new IPv4Ranges(cidrs);
        this.cidrs = cidrs.stream().map(CIDR::new).collect(Collectors.toList());
    }

    @Benchmark
    public void cidr() {
        byte[] a1 = CIDR.address("119.137.52.1");
        byte[] a2 = CIDR.address("119.137.53.1");
        byte[] a3 = CIDR.address("119.137.53.254");
        byte[] a4 = CIDR.address("119.137.54.254");
        for (CIDR cidr : cidrs) {
            cidr.matches(a1);
            cidr.matches(a2);
            cidr.matches(a3);
            cidr.matches(a4);
        }
    }

    @Benchmark
    public void ranges() {
        ranges.matches(IPv4Ranges.address("119.137.52.1"));
        ranges.matches(IPv4Ranges.address("119.137.53.1"));
        ranges.matches(IPv4Ranges.address("119.137.53.254"));
        ranges.matches(IPv4Ranges.address("119.137.54.254"));
    }
}
