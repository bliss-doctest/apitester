package de.devbliss.apitester.factory.impl;

import java.io.IOException;
import java.net.URI;

import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;

import com.google.gson.Gson;

import de.devbliss.apitester.factory.PutFactory;

/**
 * Default implementation that converts the payload to JSON (if there is one) and sends it as
 * request body (UTF8-encoded).
 *
 * @author hschuetz
 *
 */
public class DefaultPutFactory implements PutFactory {
    private static final String ENCODING = "UTF-8";
    private final Gson gson = new Gson();

    public HttpPut createPutRequest(URI uri, Object payload) throws IOException {
        HttpPut request = new HttpPut(uri);

        if (payload != null) {
            String json = gson.toJson(payload);
            StringEntity entity = new StringEntity(json, ENCODING);
            entity.setContentType(ContentType.APPLICATION_JSON.getMimeType());
            request.setEntity(entity);
        }

        return request;
    }
}
