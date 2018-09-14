package core.framework.impl.web.route;

/**
 * @author neo
 */
public final class PathV0 {
    public static PathV0 parse(String path) {
        PathV0 root = new PathV0("/");
        if ("/".equals(path)) return root;

        PathV0 current = root;
        var builder = new StringBuilder();
        for (int i = 1; i < path.length(); i++) {
            char ch = path.charAt(i);
            if (ch != '/') {
                builder.append(ch);
            } else {
                current.next = new PathV0(builder.toString());
                current = current.next;
                builder = new StringBuilder();
            }
        }

        String lastPath = builder.length() == 0 ? "/" : builder.toString();
        current.next = new PathV0(lastPath);

        return root;
    }

    public final String value;
    public PathV0 next;

    private PathV0(String value) {
        this.value = value;
    }

    String subPath() {
        var builder = new StringBuilder();
        PathV0 current = this;

        while (current != null) {
            if (builder.length() > 1 && !"/".equals(current.value)) builder.append('/');
            builder.append(current.value);
            current = current.next;
        }
        return builder.toString();
    }

    @Override
    public String toString() {
        return value;
    }
}
