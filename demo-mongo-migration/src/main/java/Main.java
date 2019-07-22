import com.mongodb.client.model.Indexes;
import core.framework.mongo.MongoMigration;

/**
 * @author neo
 */
public class Main {
    public static void main(String[] args) {
        var migration = new MongoMigration("sys.properties");
        migration.migrate(mongo -> {
            mongo.createIndex("test", Indexes.ascending("name"));
        });
    }
}
