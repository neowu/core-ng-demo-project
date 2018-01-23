package app;

import core.framework.module.App;
import core.framework.module.SystemModule;

/**
 * @author neo
 */
public class DemoSiteApp extends App {
    @Override
    protected void initialize() {
        load(new SystemModule("sys.properties"));

        http().enableGZip();
        http().httpsPort(8443);
//        http().allowSourceIPs(Sets.newHashSet("123.123.444.333"));
        site().enableWebSecurity();

        load(new WebModule());
    }
}
