package de.devbliss.apitester;

import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.codehaus.jackson.type.TypeReference;

import com.google.common.collect.ImmutableMap;
import com.google.gson.Gson;

/**
 * Data container for the most important parts of a HTTP response. Easier to use than
 * {@link HttpResponse}. Able to parse raw response body as json and then convert it to a given
 * type. Contains some isStatus...() convenience methods for the most frequently used HTTP status
 * codes.
 * 
 * @author hschuetz
 * 
 */
public class ApiResponse {

    public final int httpStatus;
    /**
     * Human readable reason for the status code, named as described in RFC2616
     */
    public final String reasonPhrase;
    public final String payload;
    public final Map<String, String> headers;

    public ApiResponse(
            int httpStatus,
            String reasonPhrase,
            String payload,
            Map<String, String> headers) {
        this.httpStatus = httpStatus;
        this.reasonPhrase = reasonPhrase;
        this.payload = payload;
        this.headers = ImmutableMap.copyOf(headers);
    }

    /**
     * Assumes that the payload of the response is valid JSON and tries to convert it to the given
     * type.
     * 
     * @param type
     * @return
     */
    public <DTO> DTO payloadJsonAs(Class<DTO> type) {
        return new Gson().fromJson(payload, type);
    }

    public <DTO> DTO payloadJsonAs(TypeReference<DTO> typeReference) {
        return new Gson().fromJson(payload, typeReference.getType());
    }

    /**
     * Tells whether HTTP status is "200 OK".
     * 
     * @return
     */
    public boolean isStatusOk() {
        return httpStatus == HttpStatus.SC_OK;
    }

    /**
     * Tells whether HTTP status is "204 NO CONTENT".
     * 
     * @return
     */
    public boolean isStatusNoContent() {
        return httpStatus == HttpStatus.SC_NO_CONTENT;
    }

    /**
     * Tells whether HTTP status is "201 CREATED".
     * 
     * @return
     */
    public boolean isStatusCreated() {
        return httpStatus == HttpStatus.SC_CREATED;
    }

    /**
     * Tells whether HTTP status is "400 BAD REQUEST".
     * 
     * @return
     */
    public boolean isStatusBadRequest() {
        return httpStatus == HttpStatus.SC_BAD_REQUEST;
    }

    /**
     * Tells whether HTTP status is "401 UNAUTHORIZED".
     * 
     * @return
     */
    public boolean isStatusUnauthorized() {
        return httpStatus == HttpStatus.SC_UNAUTHORIZED;
    }

    /**
     * Tells whether HTTP status is "403 FORBIDDEN".
     * 
     * @return
     */
    public boolean isStatusForbidden() {
        return httpStatus == HttpStatus.SC_FORBIDDEN;
    }

    /**
     * Tells whether HTTP status is "404 NOT FOUND".
     * 
     * @return
     */
    public boolean isStatusNotFound() {
        return httpStatus == HttpStatus.SC_NOT_FOUND;
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
