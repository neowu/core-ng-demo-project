package core.framework.internal.web.http;

import java.util.Arrays;

public class IPTrie {
    Node root;

    public IPTrie() {
        this.root = new Node();
    }

    public void insertCidr(String cidr) {
        int index = cidr.indexOf('/');
        if (index <= 0 || index >= cidr.length() - 1) throw new Error("invalid cidr, value=" + cidr);
        int maskBits = Integer.parseInt(cidr.substring(index + 1));
        int address = toInt(IPRanges.address(cidr.substring(0, index)));

        var node = root;
        for (int i = 0; i < maskBits; i++) {
            int bit = (address >> (31 - i)) & 1;
            if (node.children[bit] == null) {
                node.children[bit] = new Node();
            }
            node = node.children[bit];
        }
        node.match = true;
    }

    public boolean matches(byte[] address) {
        if (address.length != 4) throw new Error("not ipv4 address, address=" + Arrays.toString(address));
        int value = toInt(address);
        Node node = root;
        for (int i = 0; i < 32; i++) {
            int bit = (value >> (31 - i)) & 1;
            node = node.children[bit];
            if (node == null) return false;
            else if (node.match) return true;
        }
        return false;
    }

    private int toInt(byte[] address) {
        if (address.length != 4) throw new Error("not ipv4 address, address=" + Arrays.toString(address));
        int result = 0;
        result |= (address[0] & 0xFF) << 24;
        result |= (address[1] & 0xFF) << 16;
        result |= (address[2] & 0xFF) << 8;
        result |= address[3] & 0xFF;
        return result;
    }

    static class Node {
        Node[] children = new Node[2];
        boolean match;

        Node() {
            this.match = false;
        }
    }
}
