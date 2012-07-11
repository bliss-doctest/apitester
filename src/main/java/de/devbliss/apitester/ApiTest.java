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
 * <h1>Instantiation</h1>
 * <ul>
 * <li>
 * If you want to instantiate this class and use the <b>default factories</b>, just make:
 * <code><br/>ApiTest api = new ApiTest();</code></li><br/>
 * <li>
 * If you want to instantiate this class and use <b>specific factories</b> (like mocks for example),
 * just make: <code>
 * <br/>ApiTest api = new ApiTest();
 * <br/>api.setGetFactory(new SpecificGetFactory());
 * </code></li><br/>
 * <li>
 * If you use <b>Guice</b> and want to inject this class, there are two use cases:
 * <ul>
 * <li>if you need the <b>default factories</b>, you do not have to do anything since Guice is going
 * to use the default constructor of {@link ApiTest}.</li>
 * <li>if you need some <b>specifics factories</b>, you just have to bind these factories in your
 * Guice module like this: <code>
 * <br/>bind(DeleteFactory.class).annotatedWith(Names.named(ApiTest.{@link #DELETE_FACTORY})).to(
                DeleteImpl.class);
 * </code> <br/>
 * Guice will then find this binding and call the corresponding setter function (annotated with
 * <i>@Injected(optional=true)</i>).</li>
 * </ul>
 * </li>
 * </ul>
 * 
 * @author hschuetz, mreinwarth, bmary
 * 
 */
public class ApiTest {

    public static final String GET_FACTORY = "getFactory";
    public static final String DELETE_FACTORY = "deleteFactory";
    public static final String POST_FACTORY = "postFactory";
    public static final String PUT_FACTORY = "putFactory";
    public static final String TEST_STATE = "testState";

    private GetFactory getFactory;
    private PostFactory postFactory;
    private DeleteFactory deleteFactory;
    private PutFactory putFactory;
    private TestState testState;

    public enum HTTP_REQUEST {
        POST, GET, PUT, DELETE;
    }

    @Inject(optional = true)
    public void setDeleteFactory(@Named(DELETE_FACTORY) DeleteFactory deleteFactory) {
        this.deleteFactory = deleteFactory;
    }

    @Inject(optional = true)
    public void setGetFactory(@Named(GET_FACTORY) GetFactory getFactory) {
        this.getFactory = getFactory;
    }

    @Inject(optional = true)
    public void setPostFactory(@Named(POST_FACTORY) PostFactory postFactory) {
        this.postFactory = postFactory;
    }

    @Inject(optional = true)
    public void setPutFactory(@Named(PUT_FACTORY) PutFactory putFactory) {
        this.putFactory = putFactory;
    }

    @Inject(optional = true)
    public void setTestState(@Named(TEST_STATE) TestState testState) {
        this.testState = testState;
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
