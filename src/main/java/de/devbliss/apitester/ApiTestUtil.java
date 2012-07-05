package de.devbliss.apitester;

import static junit.framework.Assert.assertEquals;

import java.io.IOException;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;

/**
 * Some helper methods for api testing.
 * 
 * @author hschuetz
 * 
 */
public class ApiTestUtil {

    /**
     * Shortcut to assert that a given response has a certain HTTP status.
     * 
     * @param response
     * @param status
     */
    public static void assertStatus(ApiResponse response, int status) {
        assertEquals(status, response.httpStatus);
    }

    /**
     * Shortcut for {@link #assertStatus(ApiResponse, int)} asserting status is "200 OK".
     * 
     * @param response
     */
    public static void assertOk(ApiResponse response) {
        assertEquals(HttpStatus.SC_OK, response.httpStatus);
    }

    /**
     * Shortcut for {@link #assertStatus(ApiResponse, int)} asserting status is "201 CREATED".
     * 
     * @param response
     */
    public static void assertCreated(ApiResponse response) {
        assertEquals(HttpStatus.SC_CREATED, response.httpStatus);
    }

    /**
     * Shortcut for {@link #assertStatus(ApiResponse, int)} asserting status is "204 NO CONTENT".
     * 
     * @param response
     */
    public static void assertNoContent(ApiResponse response) {
        assertEquals(HttpStatus.SC_NO_CONTENT, response.httpStatus);
    }

    /**
     * Shortcut for {@link #assertStatus(ApiResponse, int)} asserting status is "400 BAD REQUEST".
     * 
     * @param response
     */
    public static void assertBadRequest(ApiResponse response) {
        assertEquals(HttpStatus.SC_BAD_REQUEST, response.httpStatus);
    }

    /**
     * Shortcut for {@link #assertStatus(ApiResponse, int)} asserting status is "401 UNAUTHORIZED".
     * 
     * @param response
     */
    public static void assertAuthenticationRequired(ApiResponse response) {
        assertEquals(HttpStatus.SC_UNAUTHORIZED, response.httpStatus);
    }

    /**
     * Shortcut for {@link #assertStatus(ApiResponse, int)} asserting status is "403 FORBIDDEN".
     * 
     * @param response
     */
    public static void assertForbidden(ApiResponse response) {
        assertEquals(HttpStatus.SC_FORBIDDEN, response.httpStatus);
    }

    /**
     * Shortcut for {@link #assertStatus(ApiResponse, int)} asserting status is "404 NOT FOUND".
     * 
     * @param response
     */
    public static void assertNotFound(ApiResponse response) {
        assertEquals(HttpStatus.SC_NOT_FOUND, response.httpStatus);
    }


    public static ApiResponse convertToApiResponse(HttpResponse httpResponse) throws IOException {
        int httpStatus = httpResponse.getStatusLine().getStatusCode();
        HttpEntity entity = httpResponse.getEntity();
        String rawResponse = entity != null ? IOUtils.toString(entity.getContent()) : "";
        return new ApiResponse(httpStatus, rawResponse);
    }
}
