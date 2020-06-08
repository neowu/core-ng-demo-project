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

        http().gzip();
        http().access().denyFromFile("deny-ip-list.txt");

        site().security();
        log().maskFields("password");

        load(new WebModule());
    }
}
