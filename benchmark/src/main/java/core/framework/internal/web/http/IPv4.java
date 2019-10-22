package core.framework.internal.web.http;

/**
 * @author neo
 */
public class IPv4 {
    public final byte[] addresses;

    public IPv4(String ip) {
        addresses = parse(ip);
    }

    private byte[] parse(String ip) {
        int index1 = ip.indexOf('.');
        int index2 = ip.indexOf('.', index1 + 1);
        int index3 = ip.indexOf('.', index2 + 1);
        if (index1 < 0 || index2 < 0 || index3 < 0) throw new Error("invalid ip, ip=" + ip);

        int segment1 = Integer.parseInt(ip.substring(0, index1));
        int segment2 = Integer.parseInt(ip.substring(index1 + 1, index2));
        int segment3 = Integer.parseInt(ip.substring(index2 + 1, index3));
        int segment4 = Integer.parseInt(ip.substring(index3 + 1));
        if (!(segment1 >= 0 && segment1 <= 255
                && segment2 >= 0 && segment2 <= 255
                && segment3 >= 0 && segment3 <= 255
                && segment4 >= 0 && segment4 <= 255)) {
            throw new Error("invalid ip, ip=" + ip);
        }

        return new byte[]{(byte) segment1, (byte) segment2, (byte) segment3, (byte) segment4};
    }
}
