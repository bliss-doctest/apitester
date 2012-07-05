package de.devbliss.apitester.factory;

import java.io.IOException;
import java.net.URI;

import org.apache.http.client.methods.HttpGet;

/**
 * Creates GET requests. Override this if you need to add e.g. custom headers.
 * 
 * @author hschuetz
 * 
 */
public interface GetFactory {

    HttpGet createGetRequest(URI uri) throws IOException;

}
