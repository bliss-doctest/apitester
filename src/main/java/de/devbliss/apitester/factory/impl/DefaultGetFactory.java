package de.devbliss.apitester.factory.impl;

import java.io.IOException;
import java.net.URI;

import org.apache.http.client.methods.HttpGet;

import de.devbliss.apitester.factory.GetFactory;

/**
 * Default implementation.
 * 
 * @author hschuetz
 * 
 */
public class DefaultGetFactory implements GetFactory {

    public HttpGet createGetRequest(URI uri) throws IOException {
        return new HttpGet(uri);
    }
}
