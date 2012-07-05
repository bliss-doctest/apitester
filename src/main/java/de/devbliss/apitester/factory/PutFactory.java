package de.devbliss.apitester.factory;

import java.io.IOException;
import java.net.URI;

import org.apache.http.client.methods.HttpPut;

/**
 * Creates PUT requests. Override this if you need to add e.g. custom headers.
 * 
 * @author hschuetz
 * 
 */
public interface PutFactory {

    /**
     * 
     * @param uri
     * @param payload will end up as data in response body, may be <code>null</code> if there is no
     *            use for a payload
     * @return
     * @throws IOException
     */
    HttpPut createPutRequest(URI uri, Object payload) throws IOException;

}
