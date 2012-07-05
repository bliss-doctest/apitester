package de.devbliss.apitester.factory;

import java.net.URI;

import org.apache.http.annotation.NotThreadSafe;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;

@NotThreadSafe
public class HttpDeleteWithBody extends HttpEntityEnclosingRequestBase {
    private static final String METHOD_NAME = "DELETE";

    public HttpDeleteWithBody(final URI uri) {
        super();
        setURI(uri);
    }

    @Override
    public String getMethod() {
        return METHOD_NAME;
    }

}

