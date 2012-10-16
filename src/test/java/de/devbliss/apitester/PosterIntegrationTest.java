package de.devbliss.apitester;

import static junit.framework.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import java.net.URI;

import org.apache.http.HttpRequest;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpPost;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import de.devbliss.apitester.dummyserver.DummyApiServer;
import de.devbliss.apitester.dummyserver.DummyDto;
import de.devbliss.apitester.factory.PostFactory;
import de.devbliss.apitester.factory.impl.DefaultPostFactory;

/**
 * Tests the methods of {@link Poster} and its delegates against an embedded local instance of
 * {@link DummyApiServer} with "real" HTTP requests.
 * 
 * @author hschuetz
 * 
 */
public class PosterIntegrationTest {

    protected static final String HEADER_VALUE1 = "header_value1";
    protected static final String HEADER_NAME1 = "header_name1";
    protected static final String HEADER_VALUE2 = "header_value2";
    protected static final String HEADER_NAME2 = "header_name2";
    private DummyApiServer server;

    @Before
    public void setUp() throws Exception {
        server = new DummyApiServer();
        server.start(false);
    }

    @After
    public void shutDown() throws Exception {
        server.stop();
    }

    @Test
    public void testPostOk() throws Exception {
        URI uri = server.buildGetRequestUri(HttpStatus.SC_OK);
        RequestResponseWrapper wrapper = Poster.post(uri);
        HttpRequest request = wrapper.httpRequest;
        ApiResponse response = wrapper.apiResponse;
        ApiTestUtil.assertOk(response);
        assertNotNull(request.getAllHeaders());
    }

    @Test
    public void testPostOkWithPayload() throws Exception {
        DummyDto payload = createPayload();
        URI uri = server.buildGetRequestUri(HttpStatus.SC_OK);
        RequestResponseWrapper wrapper = Poster.post(uri, payload);
        HttpRequest request = wrapper.httpRequest;
        ApiResponse response = wrapper.apiResponse;
        ApiTestUtil.assertOk(response);
        DummyDto result = response.payloadJsonAs(DummyDto.class);
        assertEquals(payload, result);
        assertNotNull(request.getAllHeaders());
    }

    @Test
    public void testPostOkWithOwnPostFactory() throws Exception {
        URI uri = server.buildGetRequestUri(HttpStatus.SC_OK);
        RequestResponseWrapper wrapper = Poster.post(uri, new DefaultPostFactory());
        ApiResponse response = wrapper.apiResponse;
        HttpRequest request = wrapper.httpRequest;
        ApiTestUtil.assertOk(response);
        assertNotNull(request.getAllHeaders());
    }

    @Test
    public void testPostOkWithOwnTestState() throws Exception {
        URI uri = server.buildGetRequestUri(HttpStatus.SC_OK);
        TestState testState = ApiTesterModule.createTestState();
        RequestResponseWrapper wrapper = Poster.post(uri, testState);
        ApiResponse response = wrapper.apiResponse;
        HttpRequest request = wrapper.httpRequest;
        ApiTestUtil.assertOk(response);
        assertNotNull(request.getAllHeaders());
    }

    @Test
    public void testPostOkWithOwnPostFactoryAndTestState() throws Exception {
        URI uri = server.buildGetRequestUri(HttpStatus.SC_OK);
        TestState testState = ApiTesterModule.createTestState();
        RequestResponseWrapper wrapper = Poster.post(uri, new DefaultPostFactory(), testState);
        ApiResponse response = wrapper.apiResponse;
        HttpRequest request = wrapper.httpRequest;
        ApiTestUtil.assertOk(response);
        assertNotNull(request.getAllHeaders());
    }

    @Test
    public void testPostOkWithPayloadAndOwnPostFactory() throws Exception {
        DummyDto payload = createPayload();
        URI uri = server.buildGetRequestUri(HttpStatus.SC_OK);
        RequestResponseWrapper wrapper = Poster.post(uri, payload, new DefaultPostFactory());
        ApiResponse response = wrapper.apiResponse;
        HttpRequest request = wrapper.httpRequest;
        ApiTestUtil.assertOk(response);
        DummyDto result = response.payloadJsonAs(DummyDto.class);
        assertEquals(payload, result);
        assertNotNull(request.getAllHeaders());
    }

    @Test
    public void testPostOkWithPayloadAndOwnTestState() throws Exception {
        DummyDto payload = createPayload();
        URI uri = server.buildGetRequestUri(HttpStatus.SC_OK);
        TestState testState = ApiTesterModule.createTestState();
        RequestResponseWrapper wrapper = Poster.post(uri, payload, testState);
        ApiResponse response = wrapper.apiResponse;
        HttpRequest request = wrapper.httpRequest;
        ApiTestUtil.assertOk(response);
        DummyDto result = response.payloadJsonAs(DummyDto.class);
        assertEquals(payload, result);
        assertNotNull(request.getAllHeaders());
    }

    @Test
    public void testPostOkWithPayloadAndOwnPostFactoryAndTestState() throws Exception {
        DummyDto payload = createPayload();
        URI uri = server.buildGetRequestUri(HttpStatus.SC_OK);
        TestState testState = ApiTesterModule.createTestState();
        RequestResponseWrapper wrapper = Poster.post(uri, payload, testState, new DefaultPostFactory());
        ApiResponse response = wrapper.apiResponse;
        HttpRequest request = wrapper.httpRequest;
        ApiTestUtil.assertOk(response);
        DummyDto result = response.payloadJsonAs(DummyDto.class);
        assertEquals(payload, result);
        assertNotNull(request.getAllHeaders());
    }

    @Test
    public void testPostWithHeaders() throws Exception {
        DummyDto payload = createPayload();
        URI uri = server.buildGetRequestUri(HttpStatus.SC_OK);
        TestState testState = ApiTesterModule.createTestState();
        RequestResponseWrapper wrapper = Poster.post(uri, payload, testState, getPostFactoryWithHeaders());
        ApiResponse response = wrapper.apiResponse;
        HttpRequest request = wrapper.httpRequest;
        ApiTestUtil.assertOk(response);
        assertEquals(2, request.getAllHeaders().length);
        assertEquals(HEADER_VALUE1, request.getHeaders(HEADER_NAME1)[0].getValue());
        assertEquals(HEADER_VALUE2, request.getHeaders(HEADER_NAME2)[0].getValue());
    }

    private PostFactory getPostFactoryWithHeaders() {
        return new PostFactory() {

            public HttpPost createPostRequest(URI uri, Object payload) throws IOException {
                HttpPost request = new HttpPost(uri);
                request.setHeader(HEADER_NAME1, HEADER_VALUE1);
                request.setHeader(HEADER_NAME2, HEADER_VALUE2);
                return request;
            }
        };
    }

    private DummyDto createPayload() {
        return new DummyDto("Don't care, just some text", 1981, Boolean.FALSE);
    }
}
