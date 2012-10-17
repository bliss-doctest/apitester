package de.devbliss.apitester;

import java.util.Map;

import org.apache.http.HttpRequest;

import com.google.common.collect.ImmutableMap;

/**
 * Data container for the most important parts of a HTTP request. Easier to use than
 * {@link HttpRequest}.
 * 
 * @author bmary
 * 
 */
public class ApiRequest {

    public final Map<String, String> headers;
    public final String uri;

    public ApiRequest(String uri, Map<String, String> headers) {
        this.uri = uri;
        this.headers = ImmutableMap.copyOf(headers);
    }

    /**
     * Get the header value with the given name
     * 
     * @param name The name of the header. As per the RFC, header names are case
     *            insensitive
     * @return The value, or null if no header with that name was found
     */
    public String getHeader(String name) {
        return headers.get(name.toLowerCase());
    }
}
