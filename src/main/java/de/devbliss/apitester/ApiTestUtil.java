package de.devbliss.apitester;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;

import java.io.IOException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;

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
        assertTrue(response.isStatusOk());
    }

    /**
     * Shortcut for {@link #assertStatus(ApiResponse, int)} asserting status is "201 CREATED".
     * 
     * @param response
     */
    public static void assertCreated(ApiResponse response) {
        assertTrue(response.isStatusCreated());
    }

    /**
     * Shortcut for {@link #assertStatus(ApiResponse, int)} asserting status is "204 NO CONTENT".
     * 
     * @param response
     */
    public static void assertNoContent(ApiResponse response) {
        assertTrue(response.isStatusNoContent());
    }

    /**
     * Shortcut for {@link #assertStatus(ApiResponse, int)} asserting status is "400 BAD REQUEST".
     * 
     * @param response
     */
    public static void assertBadRequest(ApiResponse response) {
        assertTrue(response.isStatusBadRequest());
    }

    /**
     * Shortcut for {@link #assertStatus(ApiResponse, int)} asserting status is "401 UNAUTHORIZED".
     * 
     * @param response
     */
    public static void assertUnauthorized(ApiResponse response) {
        assertTrue(response.isStatusUnauthorized());
    }

    /**
     * Shortcut for {@link #assertStatus(ApiResponse, int)} asserting status is "403 FORBIDDEN".
     * 
     * @param response
     */
    public static void assertForbidden(ApiResponse response) {
        assertTrue(response.isStatusForbidden());
    }

    /**
     * Shortcut for {@link #assertStatus(ApiResponse, int)} asserting status is "404 NOT FOUND".
     * 
     * @param response
     */
    public static void assertNotFound(ApiResponse response) {
        assertTrue(response.isStatusNotFound());
    }

    /**
     * Transforms an {@link HttpResponse} object to an {@link ApiResponse}
     * 
     * @param httpResponse
     * @return
     * @throws IOException
     */
    public static ApiResponse convertToApiResponse(HttpResponse httpResponse) throws IOException {
        int httpStatus = httpResponse.getStatusLine().getStatusCode();
        HttpEntity entity = httpResponse.getEntity();
        String rawResponse = entity != null ? IOUtils.toString(entity.getContent()) : "";

        Map<String, String> headers = transformHeaders(httpResponse.getAllHeaders());

        return new ApiResponse(httpStatus, httpResponse.getStatusLine().getReasonPhrase(),
                rawResponse, headers);
    }

    /**
     * transform the original headers from the request or response to a map
     * and set the name of the headers to lower case
     * 
     * @param headers
     * @return transformedHeaders
     */
    private static Map<String, String> transformHeaders(Header[] headers) {
        Map<String, String> transformedHeaders = new HashMap<String, String>();
        for (Header header : headers) {
            transformedHeaders.put(header.getName().toLowerCase(Locale.ENGLISH), header.getValue());
        }
        return transformedHeaders;
    }

    /**
     * Transforms an {@link HttpRequest} object to an {@link ApiRequest}
     * 
     * @param httpRequest
     * @return
     */
    public static ApiRequest convertToApiRequest(HttpRequest httpRequest) {
        return new ApiRequest(httpRequest.getRequestLine().getUri(), transformHeaders(httpRequest
                .getAllHeaders()));
    }
}
