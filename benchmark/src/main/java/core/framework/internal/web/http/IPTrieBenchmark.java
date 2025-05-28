package core.framework.internal.web.http;

import core.framework.module.IPRangeFileParser;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Warmup;

import java.util.ArrayList;
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
public class IPTrieBenchmark {
    private IPTrie trie;
    private IPv4Ranges ranges;
    private List<byte[]> ips;

    @Setup
    public void setup() {
        List<String> cidrs = new IPRangeFileParser("ip-range-test/cidrs.txt").parse();
        ranges = new IPv4Ranges(cidrs);
        trie = new IPTrie();
        for (String cidr : cidrs) {
            trie.insertCidr(cidr);
        }
        ips = new ArrayList<>();
        ips.add(IPRanges.address("119.137.54.254"));
        ips.add(IPRanges.address("119.137.52.1"));
        ips.add(IPRanges.address("119.137.53.1"));
        ips.add(IPRanges.address("119.137.53.254"));
        ips.add(IPRanges.address("103.126.188.1"));
        ips.add(IPRanges.address("103.126.188.2"));
        ips.add(IPRanges.address("103.126.188.3"));
        ips.add(IPRanges.address("103.126.189.3"));
    }

    @Benchmark
    public void ranges() {
        for (byte[] ip : ips) {
            ranges.matches(ip);
        }
    }

    @Benchmark
    public void trie() {
        for (byte[] ip : ips) {
            trie.matches(ip);
        }
    }
}
