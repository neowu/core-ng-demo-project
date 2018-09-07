package app;

import app.web.ajax.Bean;
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
        http().httpsPort(8443);

        site().security();
        log().maskFields("password");

        load(new WebModule());

        api().bean(Bean.class);
    }
}
