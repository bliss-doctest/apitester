package de.devbliss.apitester;

import java.io.IOException;
import java.net.URI;

import com.google.inject.Inject;
import com.google.inject.name.Named;

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

    private GetFactory getFactory;
    private PostFactory postFactory;
    private DeleteFactory deleteFactory;
    private PutFactory putFactory;
    private TestState testState;

    public enum HTTP_REQUEST {
        POST, GET, PUT, DELETE;
    }

    private TestState getTestState() {
        if (testState == null) {
            setTestState(new TestState(ApiTesterModule.createHttpClient()));
        }
        return testState;
    }

    private DeleteFactory getDeleteFactory() {
        if (deleteFactory == null) {
            setDeleteFactory(ApiTesterModule.createDeleteFactory());
        }

        return deleteFactory;
    }

    private GetFactory getGetFactory() {
        if (getFactory == null) {
            setGetFactory(ApiTesterModule.createGetFactory());
        }
        return getFactory;
    }

    private PostFactory getPostFactory() {
        if (postFactory == null) {
            setPostFactory(ApiTesterModule.createPostFactory());
        }
        return postFactory;
    }

    private PutFactory getPutFactory() {
        if (putFactory == null) {
            setPutFactory(ApiTesterModule.createPutFactory());
        }
        return putFactory;
    }

    @Inject(optional = true)
    public void setDeleteFactory(@Named("deleteFactory") DeleteFactory deleteFactory) {
        System.out.println("--- --- setDeleteFactory");
        this.deleteFactory = deleteFactory;
    }

    @Inject(optional = true)
    public void setGetFactory(@Named("getFactory") GetFactory getFactory) {
        System.out.println("--- --- setGetFactory");
        this.getFactory = getFactory;
    }

    @Inject(optional = true)
    public void setPostFactory(@Named("postFactory") PostFactory postFactory) {
        System.out.println("--- --- setPostFactory");
        this.postFactory = postFactory;
    }

    @Inject(optional = true)
    public void setPutFactory(@Named("putFactory") PutFactory putFactory) {
        System.out.println("--- --- setPutFactory");
        this.putFactory = putFactory;
    }

    @Inject(optional = true)
    public void setTestState(@Named("testState") TestState testState) {
        System.out.println("--- --- setTestState");
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
        return Poster.post(uri, payload, getTestState(), getPostFactory());
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
        return Getter.get(uri, getTestState(), getGetFactory());
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
        return Deleter.delete(uri, getTestState(), getDeleteFactory());
    }

    public ApiResponse delete(URI uri, Object payload) throws IOException {
        return Deleter.delete(uri, getTestState(), getDeleteFactory(), payload);
    }

    public ApiResponse put(URI uri) throws IOException {
        return Putter.put(uri, getTestState(), getPutFactory());
    }

    public ApiResponse put(URI uri, Object payload) throws IOException {
        return Putter.put(uri, getTestState(), getPutFactory(), payload);
    }
}
