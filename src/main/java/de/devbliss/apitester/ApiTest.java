package de.devbliss.apitester;

import java.io.IOException;
import java.net.URI;

import de.devbliss.apitester.factory.DeleteFactory;
import de.devbliss.apitester.factory.GetFactory;
import de.devbliss.apitester.factory.PostFactory;
import de.devbliss.apitester.factory.PutFactory;

/**
 * Entrypoint class for performing api calls.
 * It keeps the teststate including http client instance so your client side session, cookies etc.
 * should be kept as long as you use the same {@link ApiTest} instance.
 * 
 * You may use this either as superclass for your own tests or instantiate it within your tests if
 * you need a different superclass for whatever reason.
 * 
 * @author hschuetz
 * 
 */
public class ApiTest {

    private final GetFactory getFactory;
    private final PostFactory postFactory;
    private final DeleteFactory deleteFactory;
    private final PutFactory putFactory;
    private final TestState testState;

    public enum HTTP_REQUEST {
        POST, GET, PUT, DELETE;
    }

    /**
     * Easy constructor if you do not want to provide own factory implementations. The default ones
     * of this framework will be used in this case.
     */
    public ApiTest() {
        postFactory = ApiTesterModule.createPostFactory();
        getFactory = ApiTesterModule.createGetFactory();
        deleteFactory = ApiTesterModule.createDeleteFactory();
        putFactory = ApiTesterModule.createPutFactory();
        testState = new TestState(ApiTesterModule.createHttpClient());
    }

    /**
     * Advanced constructor if you feel the desire to provide your own factory implementations.
     * 
     * @param postFactory
     * @param getFactory
     * @param deleteFactory
     * @param testState
     */
    public ApiTest(
            DeleteFactory deleteFactory,
            GetFactory getFactory,
            PostFactory postFactory,
            PutFactory putFactory,
            TestState testState) {
        this.postFactory = postFactory;
        this.getFactory = getFactory;
        this.deleteFactory = deleteFactory;
        this.putFactory = putFactory;
        this.testState = testState;
    }

    /**
     * Performs a post request using the {@link PostFactory} and the {@link TestState} of this
     * instance.
     * 
     * @param uri
     * @param payload
     * @return
     * @throws IOException
     */
    public ApiResponse post(URI uri, Object payload) throws IOException {
        return Poster.post(uri, payload, testState, postFactory);
    }

    /**
     * Performs a get request using the {@link GetFactory} and the {@link TestState} of this
     * instance.
     * 
     * @param uri
     * @return
     * @throws IOException
     */
    public ApiResponse get(URI uri) throws IOException {
        return Getter.get(uri, testState, getFactory);
    }

    /**
     * Performs a delete request using the {@link DeleteFactory} and the {@link TestState} of this
     * instance.
     * 
     * @param uri
     * @return
     * @throws IOException
     */
    public ApiResponse delete(URI uri) throws IOException {
        return Deleter.delete(uri, testState, deleteFactory);
    }

    public ApiResponse delete(URI uri, Object payload) throws IOException {
        return Deleter.delete(uri, testState, deleteFactory, payload);
    }

    public ApiResponse put(URI uri) throws IOException {
        return Putter.put(uri, testState, putFactory);
    }

    public ApiResponse put(URI uri, Object payload) throws IOException {
        return Putter.put(uri, testState, putFactory, payload);
    }
}
