package de.devbliss.apitester;

import java.io.IOException;
import java.net.URI;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpRequestBase;

import de.devbliss.apitester.factory.DeleteFactory;
import de.devbliss.apitester.factory.impl.DefaultDeleteFactory;

/**
 * Contains static methods to perform DELETE requests. If you want to make more requests in a series
 * sharing the same {@link TestState} and using the same {@link DeleteFactory}, consider using
 * {@link ApiTest} which is wrapping that stuff for you.
 * 
 * @author hschuetz
 * 
 */
public class Deleter {

    public static DeleteFactory createDefaultDeleteFactory() {
        return new DefaultDeleteFactory(); // TODO use gin
    }

    public static ApiResponse delete(URI uri) throws IOException {
        return deleteWithPayload(uri, null, null, null);
    }

    public static ApiResponse delete(URI uri, DeleteFactory deleteFactory) throws IOException {
        return deleteWithPayload(uri, null, deleteFactory, null);
    }

    public static ApiResponse delete(URI uri, TestState testState) throws IOException {
        return deleteWithPayload(uri, testState, null, null);
    }

    public static ApiResponse delete(URI uri, TestState testState, DeleteFactory deleteFactory)
            throws IOException {
        return deleteWithPayload(uri, testState, deleteFactory, null);
    }

    public static ApiResponse deleteWithPayload(URI uri, Object payload) throws IOException {
        return deleteWithPayload(uri, null, null, payload);
    }

    public static ApiResponse deleteWithPayload(URI uri, Object payload, DeleteFactory deleteFactory)
            throws IOException {
        return deleteWithPayload(uri, null, deleteFactory, payload);
    }

    public static ApiResponse deleteWithPayload(URI uri, Object payload, TestState testState)
            throws IOException {
        return deleteWithPayload(uri, testState, null, payload);
    }

    public static ApiResponse deleteWithPayload(URI uri, TestState testState,
            DeleteFactory deleteFactory,
            Object payload)
            throws IOException {

        if (deleteFactory == null) {
            deleteFactory = createDefaultDeleteFactory();
        }

        if (testState == null) {
            testState = new TestState();
        }

        HttpRequestBase request = null;

        if (payload != null) {
            request = deleteFactory.createDeleteRequest(uri, payload);
        } else {
            request = deleteFactory.createDeleteRequest(uri);
        }

        HttpResponse response = testState.client.execute(request);
        return ApiTestUtil.convertToApiResponse(response);
    }
}
