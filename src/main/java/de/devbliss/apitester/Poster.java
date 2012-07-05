package de.devbliss.apitester;

import java.io.IOException;
import java.net.URI;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;

import de.devbliss.apitester.factory.PostFactory;
import de.devbliss.apitester.factory.impl.DefaultPostFactory;

/**
 * Contains static methods to perform POST requests. If you want to make more requests in a series
 * sharing the same {@link TestState} and using the same {@link PostFactory}, consider using
 * {@link ApiTest} which is wrapping that stuff for you.
 * 
 * @author hschuetz
 * 
 */
public class Poster {

    public static PostFactory createDefaultPostFactory() {
        return new DefaultPostFactory(); // TODO use gin module
    }

    public static ApiResponse post(URI uri, Object payload) throws IOException {
        return post(uri, payload, null, null);
    }

    public static ApiResponse post(URI uri, Object payload, PostFactory postFactory)
            throws IOException {
        return post(uri, payload, null, postFactory);
    }

    public static ApiResponse post(URI uri, Object payload, TestState testState)
            throws IOException {
        return post(uri, payload, testState, null);
    }

    public static ApiResponse post(URI uri, Object payload, TestState testState,
            PostFactory postFactory) throws IOException {

        if (postFactory == null) {
            postFactory = createDefaultPostFactory();
        }

        if (testState == null) {
            testState = new TestState();
        }

        HttpPost request = postFactory.createPostRequest(uri, payload);
        HttpResponse response = testState.client.execute(request);
        return ApiTestUtil.convertToApiResponse(response);
    }
}
