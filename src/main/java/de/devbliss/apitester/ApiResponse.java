package de.devbliss.apitester;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.codehaus.jackson.type.TypeReference;

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
    public final String payload;

    public ApiResponse(int httpStatus, String payload) {
        this.httpStatus = httpStatus;
        this.payload = payload;
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
}
