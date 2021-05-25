package core.kibana;

import core.util.JSON;

/**
 * @author neo
 */
public class KibanaObject {
    static KibanaObject visualization(TSVB tsvb) {
        var visualization = new KibanaObject();
        visualization.id = tsvb.title;
        visualization.type = "visualization";
        visualization.attributes.title = tsvb.title;
        visualization.attributes.visState = JSON.toJSON(tsvb);
        return visualization;
    }

    public String id;
    public String type;
    public Attributes attributes = new Attributes();

    public static class Attributes {
        public String title;
        public ObjectMeta kibanaSavedObjectMeta = new ObjectMeta();
        public String visState;
    }

    public static class ObjectMeta {
        public String searchSourceJSON = "{}";
    }
}
