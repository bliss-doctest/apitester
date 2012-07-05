package de.devbliss.apitester.factory;

import java.io.IOException;
import java.net.URI;

import org.apache.http.client.methods.HttpPost;

/**
 * Creates POST requests. Override this if you need to add e.g. custom headers.
 * 
 * @author hschuetz
 * 
 */
public interface PostFactory {

    /**
     * 
     * @param uri
     * @param payload will end up as data in response body, may be <code>null</code> if there is no
     *            use for a payload
     * @return
     * @throws IOException
     */
    HttpPost createPostRequest(URI uri, Object payload) throws IOException;

}
