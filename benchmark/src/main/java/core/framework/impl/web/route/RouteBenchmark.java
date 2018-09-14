package core.framework.impl.web.route;

import core.framework.http.HTTPMethod;
import core.framework.impl.web.request.PathParams;
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
public class RouteBenchmark {
    private PathNodeV0 v0;
    private PathNode current;

    @Setup
    public void setup() {
        v0 = new PathNodeV0();
        v0.register("/customer/:customerId").put(HTTPMethod.GET, null);
        v0.register("/customer/:customerId/status").put(HTTPMethod.GET, null);
        v0.register("/customer/:customerId/password").put(HTTPMethod.GET, null);
        v0.register("/customer").put(HTTPMethod.GET, null);
        v0.register("/product/:productId").put(HTTPMethod.GET, null);
        v0.register("/product/search").put(HTTPMethod.GET, null);
        v0.register("/customer/order").put(HTTPMethod.GET, null);
        v0.register("/customer/:customerId/order/:orderId").put(HTTPMethod.GET, null);
        v0.register("/customer/:customerId/order/:orderId/status").put(HTTPMethod.GET, null);
        v0.register("/:all(*)").put(HTTPMethod.GET, null);

        current = new PathNode(null);
        current.register("/customer/:customerId").put(HTTPMethod.GET, null);
        current.register("/customer/:customerId/status").put(HTTPMethod.GET, null);
        current.register("/customer/:customerId/password").put(HTTPMethod.GET, null);
        current.register("/customer").put(HTTPMethod.GET, null);
        current.register("/product/:productId").put(HTTPMethod.GET, null);
        current.register("/product/search").put(HTTPMethod.GET, null);
        current.register("/customer/order").put(HTTPMethod.GET, null);
        current.register("/customer/:customerId/order/:orderId").put(HTTPMethod.GET, null);
        current.register("/customer/:customerId/order/:orderId/status").put(HTTPMethod.GET, null);
        current.register("/:all(*)").put(HTTPMethod.GET, null);
    }

    @Benchmark
    public void v0() {
        PathParams params = new PathParams();
        v0.find("/customer", params);
        v0.find("/customer/1", params);
        v0.find("/customer/2/status", params);
        v0.find("/customer/2/password", params);
        v0.find("/product/1", params);
        v0.find("/customer/1/order/1", params);
        v0.find("/customer/1/order/1/status", params);
        v0.find("/some", params);
    }

    @Benchmark
    public void current() {
        PathParams params = new PathParams();
        current.find("/customer", params);
        current.find("/customer/1", params);
        current.find("/customer/2/status", params);
        current.find("/customer/2/password", params);
        current.find("/product/1", params);
        current.find("/customer/1/order/1", params);
        current.find("/customer/1/order/1/status", params);
        current.find("/some", params);
    }
}
