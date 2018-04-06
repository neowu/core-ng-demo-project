package core.framework.impl.log.filter;

import core.framework.util.Lists;

import java.nio.charset.Charset;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

/**
 * @author neo
 */
public class JSONParamV1 implements FilterParam {   // old version of JSONParam impl
    private final byte[] bytes;
    private final Charset charset;

    public JSONParamV1(byte[] bytes, Charset charset) {
        this.bytes = bytes;
        this.charset = charset;
    }

    @Override
    public String filter(Set<String> maskedFields) {
        String value = new String(bytes, charset);
        List<int[]> ranges = maskRanges(value, maskedFields);
        if (ranges.isEmpty()) return value;

        ranges.sort(Comparator.comparingInt((int[] range) -> range[0]));
        StringBuilder builder = new StringBuilder(value.length());
        int current = 0;
        for (int[] range : ranges) {
            builder.append(value.substring(current, range[0]));
            builder.append("******");
            current = range[1];
        }
        builder.append(value.substring(current));
        return builder.toString();
    }

    private List<int[]> maskRanges(String value, Set<String> maskedFields) {
        List<int[]> ranges = Lists.newArrayList();

        for (String maskedField : maskedFields) {
            int current = -1;
            while (true) {
                int start = start(value, maskedField, current);
                if (start < 0) break;
                int[] range = maskRange(value, start);
                if (range == null) break;
                ranges.add(range);
                current = range[1];
            }
        }

        return ranges;
    }

    private int start(String value, String field, int from) {
        int index = value.indexOf('\"' + field + '\"', from);
        if (index < 0) return index;
        return index + field.length() + 3;   // start after last double quote
    }

    private int[] maskRange(String value, int start) {
        boolean masked = false;
        boolean escaped = false;
        int maskStart = -1;
        int length = value.length();
        for (int index = start; index < length; index++) {
            char ch = value.charAt(index);
            if (ch == '\\') {
                escaped = true;
            } else if (!escaped && !masked && ch == '\"') {
                masked = true;
                maskStart = index + 1;
            } else if (!escaped && masked && ch == '\"') {
                return new int[]{maskStart, index};
            } else {
                escaped = false;
            }
        }
        return null;
    }
}
