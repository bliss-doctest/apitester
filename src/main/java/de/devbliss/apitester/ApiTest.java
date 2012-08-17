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
 * @author hschuetz, mreinwarth, bmary, mbankmann
 * 
 */
public class ApiTest {

    public static final String GET_FACTORY = "getFactory";
    public static final String DELETE_FACTORY = "deleteFactory";
    public static final String POST_FACTORY = "postFactory";
    public static final String PUT_FACTORY = "putFactory";
    public static final String TEST_STATE = "testState";

    private GetFactory getDefaultFactory;
    private PostFactory postDefaultFactory;
    private DeleteFactory deleteDefaultFactory;
    private PutFactory putDefaultFactory;
    private TestState testState;

    public enum HTTP_REQUEST {
        POST, GET, PUT, DELETE;
    }

    @Inject(optional = true)
    public void setDefaultDeleteFactory(@Named(DELETE_FACTORY) DeleteFactory deleteFactory) {
        this.deleteDefaultFactory = deleteFactory;
    }

    @Inject(optional = true)
    public void setDefaultGetFactory(@Named(GET_FACTORY) GetFactory getFactory) {
        this.getDefaultFactory = getFactory;
    }

    @Inject(optional = true)
    public void setDefaultPostFactory(@Named(POST_FACTORY) PostFactory postFactory) {
        this.postDefaultFactory = postFactory;
    }

    @Inject(optional = true)
    public void setDefaultPutFactory(@Named(PUT_FACTORY) PutFactory putFactory) {
        this.putDefaultFactory = putFactory;
    }

    @Inject(optional = true)
    public void setTestState(@Named(TEST_STATE) TestState testState) {
        this.testState = testState;
    }

    public TestState getTestState() {
        if (testState == null) {
            setTestState(ApiTesterModule.createTestState());
        }
        return testState;
    }

    private DeleteFactory getDefaultDeleteFactory() {
        if (deleteDefaultFactory == null) {
            setDefaultDeleteFactory(ApiTesterModule.createDeleteFactory());
        }

        return deleteDefaultFactory;
    }

    private GetFactory getDefaultGetFactory() {
        if (getDefaultFactory == null) {
            setDefaultGetFactory(ApiTesterModule.createGetFactory());
        }
        return getDefaultFactory;
    }

    private PostFactory getDefaultPostFactory() {
        if (postDefaultFactory == null) {
            setDefaultPostFactory(ApiTesterModule.createPostFactory());
        }
        return postDefaultFactory;
    }

    private PutFactory getDefaultPutFactory() {
        if (putDefaultFactory == null) {
            setDefaultPutFactory(ApiTesterModule.createPutFactory());
        }
        return putDefaultFactory;
    }

    /**
     * Performs a post request using the default {@link PostFactory} and the {@link TestState} of
     * this
     * instance.
     * 
     * @param uri
     * @param payload
     * @return
     * @throws IOException
     */
    public ApiResponse post(URI uri, Object payload) throws IOException {
        return post(uri, payload, getDefaultPostFactory());
    }
    
    /**
     * Performs a post request using the given {@link PostFactory} and the {@link TestState} of
     * this instance. The {@link PostFactory} will be used for this call only. If you want to
     * configure a new default one use {@link #setDefaultPostFactory(PostFactory)}
     * 
     * @param uri
     * @param postFactory
     * @param payload
     * @return
     * @throws IOException
     */
    public ApiResponse post(URI uri, Object payload, PostFactory postFactory) throws IOException {
        return Poster.post(uri, payload, getTestState(), postFactory);
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
        return get(uri, getDefaultGetFactory());
    }

    /**
     * Performs a get request using the given {@link GetFactory} and the {@link TestState} of this
     * instance. The {@link GetFactory} will be used for this call only. If you want to
     * configure a new default one use {@link #setDefaultGetFactory(GetFactory)}.
     * 
     * @param uri
     * @return
     * @throws IOException
     */
    public ApiResponse get(URI uri, GetFactory getFactory) throws IOException {
        return Getter.get(uri, getTestState(), getFactory);
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
        return delete(uri, null, getDefaultDeleteFactory());
    }

    /**
     * Performs a delete request using the given {@link DeleteFactory} and the {@link TestState} of
     * this instance. The payload is not forbidden in the HTTP specification and so its supported
     * here.
     * 
     * @param uri
     * @param payload
     * @return
     * @throws IOException
     */
    public ApiResponse delete(URI uri, Object payload) throws IOException {
        return delete(uri, payload, getDefaultDeleteFactory());
    }

    /**
     * Performs a delete request using the given {@link DeleteFactory} and the {@link TestState} of
     * this instance. The payload is not forbidden in the HTTP specification and so its supported
     * here. The {@link DeleteFactory} will be used for this call only. If you want to
     * configure a new default one use {@link #setDefaultDeleteFactory(DeleteFactory)}.
     * 
     * @param uri
     * @param payload
     * @param deleteFactory
     * @return
     * @throws IOException
     */
    public ApiResponse delete(URI uri, Object payload, DeleteFactory deleteFactory)
            throws IOException {
        return Deleter.delete(uri, getTestState(), deleteFactory, payload);
    }

    /**
     * Performs a put request using the {@link PutFactory} and the {@link TestState} of this
     * instance.
     * 
     * @param uri
     * @return
     * @throws IOException
     */
    public ApiResponse put(URI uri) throws IOException {
        return put(uri, null, getDefaultPutFactory());
    }

    /**
     * Performs a put request using the {@link PutFactory} and the {@link TestState} of this
     * instance with the given payload.
     * 
     * @param uri
     * @param payload
     * @return
     * @throws IOException
     */
    public ApiResponse put(URI uri, Object payload) throws IOException {
        return put(uri, payload, getDefaultPutFactory());
    }

    /**
     * Performs a put request using the {@link PutFactory} and the {@link TestState} of this
     * instance with the given payload. The {@link PutFactory} will be used for this call only. If
     * you want to configure a new default one use {@link #setDefaultDeleteFactory(PutFactory)}.
     * 
     * @param uri
     * @param putFactory
     * @param payload
     * @return
     * @throws IOException
     */
    public ApiResponse put(URI uri, Object payload, PutFactory putFactory) throws IOException {
        return Putter.put(uri, getTestState(), putFactory, payload);
    }

    /**
     * Shutdown, closing any open HTTP connections
     */
    public void shutdown() {
        if (testState != null) {
            testState.shutdown();
        }
    }
}
