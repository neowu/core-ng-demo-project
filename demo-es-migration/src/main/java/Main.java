import migration.ESMigration;
import migration.ESMigrationApp;

/**
 * @author neo
 */
public class Main {
    public static void main(String[] args) {
        var app = new ESMigrationApp();
        app.configure();

        app.bean(ESMigration.class).migrate();
    }
}
