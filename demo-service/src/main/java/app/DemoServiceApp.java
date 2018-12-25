package app;

import core.framework.module.App;
import core.framework.module.SystemModule;

/**
 * @author neo
 */
public class DemoServiceApp extends App {
    @Override
    protected void initialize() {
        http().httpsPort(8443);
        load(new SystemModule("sys.properties"));
        load(new ProductModule());
        load(new JobModule());
//        load(new CustomerModule());
    }
}
