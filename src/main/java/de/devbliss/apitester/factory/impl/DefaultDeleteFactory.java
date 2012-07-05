package de.devbliss.apitester.factory.impl;

import java.io.IOException;
import java.net.URI;

import org.apache.http.client.methods.HttpDelete;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;

import com.google.gson.Gson;

import de.devbliss.apitester.factory.DeleteFactory;
import de.devbliss.apitester.factory.HttpDeleteWithBody;

/**
 * Default implementation.
 * 
 * @author hschuetz
 * 
 */
public class DefaultDeleteFactory implements DeleteFactory {
    private static final String ENCODING = "UTF-8";
    private final Gson gson = new Gson();

    public HttpDelete createDeleteRequest(URI uri) throws IOException {
        return new HttpDelete(uri);
    }

    public HttpDeleteWithBody createDeleteRequest(URI uri, Object payload) throws IOException {
        HttpDeleteWithBody request = new HttpDeleteWithBody(uri);

        if (payload != null) {
            String json = gson.toJson(payload);
            StringEntity entity = new StringEntity(json, ENCODING);
            entity.setContentType(ContentType.APPLICATION_JSON.getMimeType());
            request.setEntity(entity);
        }

        return request;
    }
}