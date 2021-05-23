package core.kibana;

/**
 * @author neo
 */
public class KibanaObject {
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
