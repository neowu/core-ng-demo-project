import core.framework.json.JSONDeserializeBenchmark;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.ChainedOptionsBuilder;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

/**
 * @author neo
 */
void main() throws RunnerException {
    ChainedOptionsBuilder builder = new OptionsBuilder()
            .include(JSONDeserializeBenchmark.class.getSimpleName())
            .forks(1);

//        builder.addProfiler(GCProfiler.class);
//        builder.addProfiler(StackProfiler.class)
//            .jvmArgsAppend("-Djmh.stack.lines=3");

    Options options = builder.build();
    new Runner(options).run();
}
