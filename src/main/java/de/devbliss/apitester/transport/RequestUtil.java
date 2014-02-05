package de.devbliss.apitester.transport;

import static de.devbliss.apitester.Constants.HEADER_NAME_CONTENT_TYPE;

import java.io.IOException;
import java.net.URI;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPatch;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;

import com.google.gson.Gson;

import de.devbliss.apitester.ApiTestUtil;
import de.devbliss.apitester.entity.ApiRequest;
import de.devbliss.apitester.entity.ApiResponse;
import de.devbliss.apitester.entity.Context;
import de.devbliss.apitester.entity.TestState;

/**
 * Knows how to build requests of different types (GET, POST, ...) and how to send them.
 * 
 * @author henningschuetz
 * 
 */
public class RequestUtil {

    private static final Gson gson = new Gson();
    private static final String ENCODING = "UTF-8";
    private static final ContentType TEXT_PLAIN_UTF8 = ContentType.create(ContentType.TEXT_PLAIN.getMimeType(), ENCODING);

    public static HttpPost createPost(URI uri, Object payload, TestState testState,
            Map<String, String> additionalHeaders) throws IOException {
        HttpPost request = new HttpPost(uri);
        request = enhanceRequest(request, payload, testState, additionalHeaders);
        return request;
    }

    public static HttpPut createPut(URI uri, Object payload, TestState testState,
            Map<String, String> additionalHeaders) throws IOException {
        HttpPut request = new HttpPut(uri);
        request = enhanceRequest(request, payload, testState, additionalHeaders);
        return request;
    }

    public static HttpPatch createPatch(URI uri, Object payload, TestState testState,
            Map<String, String> additionalHeaders) throws IOException {
        HttpPatch request = new HttpPatch(uri);
        request = enhanceRequest(request, payload, testState, additionalHeaders);
        return request;
    }

    public static HttpGet createGet(URI uri, TestState testState, Map<String, String> additionalHeaders) {
        HttpGet request = new HttpGet(uri);
        return handleHeaders(request, TEXT_PLAIN_UTF8, additionalHeaders);
    }

    public static HttpDelete createDelete(URI uri, TestState testState, Map<String, String> additionalHeaders) {
        HttpDelete request = new HttpDelete(uri);
        return handleHeaders(request, TEXT_PLAIN_UTF8, additionalHeaders);
    }

    public static HttpDeleteWithBody createDelete(URI uri, Object payload, TestState testState, Map<String, String> additionalHeaders)
            throws IOException {
        HttpDeleteWithBody request = new HttpDeleteWithBody(uri);
        return enhanceRequest(request, payload, testState, additionalHeaders);
    }

    /**
     * Makes the actual call to the backend.
     * 
     * @param request request as created by the create...() methods in this class.
     * @param testState contains cookies, testState etc.
     * @return context containing the actual request and response
     * 
     * @throws IOException
     */
    public static <T extends HttpRequestBase> Context call(T request, TestState testState) throws IOException {

        // IMPORTANT: we have to get the cookies from the testState before making the request
        // because this request could add some cookie to the testState (e.g: the response could have
        // a Set-Cookie header)
        ApiRequest apiRequest = ApiTestUtil.convertToApiRequest(request.getURI(), request, testState.getCookies());
        HttpResponse response = testState.client.execute(request);
        ApiResponse apiResponse = ApiTestUtil.convertToApiResponse(response);
        return new Context(apiResponse, apiRequest);
    }

    /**
     * Sets the appropriate content type and encoding in the payload of a given request (in case there is a payload),
     * and calls {@link #handleHeaders(HttpRequestBase, ContentType, Map)} afterwards.
     * 
     * @param request
     * @param payload
     * @param testState
     * @param additionalHeaders
     * @return the enhanced request (for chaining)
     * @throws IOException
     */
    private static <T extends HttpEntityEnclosingRequestBase> T enhanceRequest(T request, Object payload, TestState testState,
            Map<String, String> additionalHeaders) throws IOException {

        ContentType contentType = null;

        if (payload != null) {
            String payloadAsString = null;

            if (payload instanceof String) {
                contentType = TEXT_PLAIN_UTF8;
                payloadAsString = (String) payload;
            } else {
                contentType = ContentType.APPLICATION_JSON;
                payloadAsString = gson.toJson(payload);
            }

            StringEntity entity = new StringEntity(payloadAsString, ENCODING);
            entity.setContentType(contentType.getMimeType());
            request.setEntity(entity);
        } else {
            contentType = ContentType.TEXT_PLAIN;
        }

        return handleHeaders(request, contentType, additionalHeaders);
    }

    /**
     * Sets additional headers to a given request and creates appropriate content type header in the request in case it
     * does not contain such headers yet.
     * 
     * @param request
     * @param contentType
     * @param additionalHeaders
     * @return the enhanced request (for chaining)
     */
    private static <T extends HttpRequestBase> T handleHeaders(T request, ContentType contentType, Map<String, String> additionalHeaders) {

        if (additionalHeaders != null) {
            for (String headerName : additionalHeaders.keySet()) {
                request.addHeader(headerName, additionalHeaders.get(headerName));
            }
        }

        if (additionalHeaders == null || !additionalHeaders.keySet().contains(HEADER_NAME_CONTENT_TYPE)) {
            request.addHeader(HEADER_NAME_CONTENT_TYPE, contentType.toString());
        }

        return request;
    }

}
