package de.devbliss.apitester.requestprocess;

import static de.devbliss.apitester.Constants.HEADER_NAME_CONTENT_TYPE;

import java.io.IOException;
import java.net.URI;
import java.util.Map;

import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;

import com.google.gson.Gson;

import de.devbliss.apitester.TestState;

public class RequestCreator {

    private static final Gson gson = new Gson();
    private static final String ENCODING = "UTF-8";

    public static HttpPost createPost(URI uri, Object payload, TestState testState,
            Map<String, String> additionalHeaders) throws IOException {
        HttpPost request = new HttpPost(uri);
        request = enhanceRequest(request, payload, testState, additionalHeaders);
        return request;
    }

    private static <T extends HttpEntityEnclosingRequestBase> T enhanceRequest(T request, Object payload, TestState testState,
            Map<String, String> additionalHeaders) throws IOException {

        ContentType contentType = null;

        if (payload != null) {
            String payloadAsString = null;

            if (payload instanceof String) {
                contentType = ContentType.create(ContentType.TEXT_PLAIN.getMimeType(), ENCODING);
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
