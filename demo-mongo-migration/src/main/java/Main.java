import com.mongodb.client.model.Indexes;
import core.framework.mongo.MongoMigration;
import org.bson.Document;

/**
 * @author neo
 */
public class Main {
    public static void main(String[] args) {
        var migration = new MongoMigration("sys.properties");
        migration.migrate(mongo -> {
            mongo.runAdminCommand(new Document().append("setParameter", 1).append("notablescan", 1));

            mongo.createIndex("test", Indexes.ascending("name"));
        });
    }
}
