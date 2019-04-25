package app;

import app.demo.TestDocument;
import app.demo.TestMongoEntity;
import core.framework.module.Module;
import core.framework.mongo.module.MongoConfig;
import core.framework.search.module.SearchConfig;

import java.time.Duration;

/**
 * @author neo
 */
public class MongoWithESModule extends Module {
    @Override
    protected void initialize() {
        MongoConfig mongo = config(MongoConfig.class);
        mongo.uri("mongodb://localhost:27017/test");
        mongo.collection(TestMongoEntity.class);

        mongo = config(MongoConfig.class, "other");
        mongo.uri("mongodb://localhost:27018/test");
        mongo.collection(TestMongoEntity.class);

        SearchConfig search = config(SearchConfig.class);
        search.host("localhost");
        search.timeout(Duration.ofSeconds(5));
        search.slowOperationThreshold(Duration.ofSeconds(5));
        search.type(TestDocument.class);
    }
}
