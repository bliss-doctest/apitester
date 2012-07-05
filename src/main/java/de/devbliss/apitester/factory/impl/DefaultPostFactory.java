package de.devbliss.apitester.factory.impl;

import java.io.IOException;
import java.net.URI;

import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;

import com.google.gson.Gson;

import de.devbliss.apitester.factory.PostFactory;

/**
 * Default implementation that converts the payload to JSON (if there is one) and sends it as
 * request body (UTF8-encoded).
 *
 * @author hschuetz
 *
 */
public class DefaultPostFactory implements PostFactory {
    private static final String ENCODING = "UTF-8";
    private final Gson gson = new Gson();

    public HttpPost createPostRequest(URI uri, Object payload) throws IOException {
        HttpPost request = new HttpPost(uri);

        if (payload != null) {
            String json = gson.toJson(payload);
            StringEntity entity = new StringEntity(json, ENCODING);
            entity.setContentType(ContentType.APPLICATION_JSON.getMimeType());
            request.setEntity(entity);
        }

        return request;
    }
}
