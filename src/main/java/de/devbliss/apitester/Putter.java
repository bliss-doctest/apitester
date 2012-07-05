package de.devbliss.apitester;

import java.io.IOException;
import java.net.URI;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPut;

import de.devbliss.apitester.factory.DeleteFactory;
import de.devbliss.apitester.factory.PutFactory;
import de.devbliss.apitester.factory.impl.DefaultPutFactory;

/**
 * Contains static methods to perform PUT requests. If you want to make more requests in a series
 * sharing the same {@link TestState} and using the same {@link DeleteFactory}, consider using
 * {@link ApiTest} which is wrapping that stuff for you.
 * 
 * @author hschuetz
 * 
 */
public class Putter {

    public static PutFactory createDefaultPutFactory() {
        return new DefaultPutFactory(); // TODO use gin
    }

    public static ApiResponse put(URI uri) throws IOException {
        return put(uri, null, null, null);
    }

    public static ApiResponse put(URI uri, PutFactory putFactory) throws IOException {
        return put(uri, null, putFactory, null);
    }

    public static ApiResponse put(URI uri, TestState testState) throws IOException {
        return put(uri, testState, null, null);
    }

    public static ApiResponse put(URI uri, TestState testState, PutFactory putFactory)
            throws IOException {
        return put(uri, testState, putFactory, null);
    }

    public static ApiResponse put(URI uri, Object payload) throws IOException {
        return put(uri, null, null, payload);
    }

    public static ApiResponse put(URI uri, Object payload, PutFactory putFactory)
            throws IOException {
        return put(uri, null, putFactory, payload);
    }

    public static ApiResponse put(URI uri, Object payload, TestState testState)
            throws IOException {
        return put(uri, testState, null, payload);
    }

    public static ApiResponse put(URI uri, TestState testState, PutFactory putFactory,
            Object payload)
            throws IOException {

        if (putFactory == null) {
            putFactory = createDefaultPutFactory();
        }

        if (testState == null) {
            testState = new TestState();
        }

        HttpPut request = putFactory.createPutRequest(uri, payload);
        HttpResponse response = testState.client.execute(request);
        return ApiTestUtil.convertToApiResponse(response);
    }
}
