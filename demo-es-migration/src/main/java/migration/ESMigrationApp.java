package migration;

import app.DemoServiceApp;
import core.framework.module.App;

/**
 * @author neo
 */
public class ESMigrationApp extends App {
    @Override
    protected void initialize() {
        load(new DemoServiceApp());

        bind(ESMigration.class);
    }
}
