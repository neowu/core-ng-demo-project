import core.framework.search.ElasticSearchMigration;
import core.framework.util.ClasspathResources;

/**
 * @author neo
 */
void main() {
    var migration = new ElasticSearchMigration("sys.properties");
    migration.migrate(search -> search.putIndex("product", ClasspathResources.text("product-index.json")));
}
