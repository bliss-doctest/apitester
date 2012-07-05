package de.devbliss.apitester.factory;

import java.io.IOException;
import java.net.URI;

import org.apache.http.client.methods.HttpDelete;

public interface DeleteFactory {

    HttpDelete createDeleteRequest(URI uri) throws IOException;

    HttpDeleteWithBody createDeleteRequest(URI uri, Object payload) throws IOException;
}
