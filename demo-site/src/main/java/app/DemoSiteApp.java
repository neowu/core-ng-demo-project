package app;

import core.framework.module.App;
import core.framework.module.SystemModule;
import core.framework.util.Sets;

/**
 * @author neo
 */
public class DemoSiteApp extends App {
    @Override
    protected void initialize() {
        load(new SystemModule("sys.properties"));

        http().enableGZip();
        http().httpsPort(8443);
        http().allowClientIP(Sets.newHashSet("123.123.123.123/32"));
        site().enableWebSecurity();
        log().maskFields("password");

        load(new WebModule());
    }
}
