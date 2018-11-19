package migration;

import core.framework.inject.Inject;
import core.framework.search.ClusterStateResponse;
import core.framework.search.ElasticSearch;
import core.framework.util.ClasspathResources;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * @author neo
 */
public class ESMigration {
    private final Logger logger = LoggerFactory.getLogger(ESMigration.class);
    @Inject
    ElasticSearch elasticSearch;

    public void migrate() {
        try {
            Map<String, ClusterStateResponse.Index> indices = elasticSearch.state().metadata.indices;
            if (!indices.containsKey("product")) {
                elasticSearch.createIndex("product", ClasspathResources.text("product-index.json"));
            } else {
                logger.info("product index exists, skip");
            }
            System.exit(0);
        } catch (Throwable e) {
            logger.error("failed to run migration", e);
            System.exit(1);
        }
    }
}
