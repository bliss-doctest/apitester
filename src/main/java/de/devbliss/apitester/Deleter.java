package de.devbliss.apitester;

import java.io.IOException;
import java.net.URI;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpRequestBase;

import de.devbliss.apitester.factory.DeleteFactory;

/**
 * Contains static methods to perform DELETE requests. If you want to make more requests in a series
 * sharing the same {@link TestState} and using the same {@link DeleteFactory}, consider using
 * {@link ApiTest} which is wrapping that stuff for you.
 * 
 * @author hschuetz
 * 
 */
public class Deleter {

    public static Context delete(URI uri) throws IOException {
        return delete(uri, null, null, null);
    }

    public static Context delete(URI uri, DeleteFactory deleteFactory) throws IOException {
        return delete(uri, null, deleteFactory, null);
    }

    public static Context delete(URI uri, TestState testState) throws IOException {
        return delete(uri, testState, null, null);
    }

    public static Context delete(URI uri, TestState testState, DeleteFactory deleteFactory)
            throws IOException {
        return delete(uri, testState, deleteFactory, null);
    }

    public static Context delete(URI uri, Object payload) throws IOException {
        return delete(uri, null, null, payload);
    }

    public static Context delete(URI uri, Object payload, DeleteFactory deleteFactory)
            throws IOException {
        return delete(uri, null, deleteFactory, payload);
    }

    public static Context delete(URI uri, Object payload, TestState testState) throws IOException {
        return delete(uri, testState, null, payload);
    }

    public static Context delete(URI uri, TestState testState, DeleteFactory deleteFactory,
            Object payload) throws IOException {

        if (deleteFactory == null) {
            deleteFactory = ApiTesterModule.createDeleteFactory();
        }

        if (testState == null) {
            testState = ApiTesterModule.createTestState();
        }

        HttpRequestBase request = null;

        if (payload != null) {
            request = deleteFactory.createDeleteRequest(uri, payload);
        } else {
            request = deleteFactory.createDeleteRequest(uri);
        }

        HttpResponse response = testState.client.execute(request);
        ApiResponse apiResponse = ApiTestUtil.convertToApiResponse(response);
        return new Context(apiResponse, request);
    }
}
