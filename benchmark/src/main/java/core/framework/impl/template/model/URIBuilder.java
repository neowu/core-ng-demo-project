package core.framework.impl.template.model;

import core.framework.util.Encodings;

import static core.framework.util.Strings.format;

/**
 * @author neo
 */
public class URIBuilder {
    private final StringBuilder uri;
    private boolean queryStarted;

    public URIBuilder(String prefix) {
        uri = new StringBuilder(prefix);
        queryStarted = prefix.indexOf('?') > 0;
    }

    public URIBuilder addPath(String segment) {
        if (queryStarted) throw new Error(format("path segment must not be added after query, uri={}", uri.toString()));
        if (uri.length() > 0 && uri.charAt(uri.length() - 1) != '/') uri.append('/');
        uri.append(Encodings.uriComponent(segment));
        return this;
    }

    public URIBuilder addQueryParam(String name, String value) {
        uri.append(queryStarted ? '&' : '?').append(Encodings.uriComponent(name)).append('=').append(Encodings.uriComponent(value));
        queryStarted = true;
        return this;
    }

    public String toURI() {
        return uri.toString();
    }
}
