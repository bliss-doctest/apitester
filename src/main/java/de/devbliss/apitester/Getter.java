package de.devbliss.apitester;

import java.io.IOException;
import java.net.URI;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;

import de.devbliss.apitester.factory.GetFactory;

/**
 * Contains static methods to perform GET requests. If you want to make more requests in a series
 * sharing the same {@link TestState} and using the same {@link GetFactory}, consider using
 * {@link ApiTest} which is wrapping that stuff for you.
 * 
 * @author hschuetz
 * 
 */
public class Getter {

    public static ApiResponse get(URI uri) throws IOException {
        return get(uri, null, null);
    }

    public static ApiResponse get(URI uri, GetFactory getFactory) throws IOException {
        return get(uri, null, getFactory);
    }

    public static ApiResponse get(URI uri, TestState testState) throws IOException {
        return get(uri, testState, null);
    }

    public static ApiResponse get(URI uri, TestState testState, GetFactory getFactory)
            throws IOException {

        if (getFactory == null) {
            getFactory = ApiTesterModule.createGetFactory();
        }

        if (testState == null) {
            testState = new TestState(ApiTesterModule.createHttpClient());
        }

        HttpGet request = getFactory.createGetRequest(uri);
        HttpResponse response = testState.client.execute(request);
        return ApiTestUtil.convertToApiResponse(response);
    }
}
