package de.devbliss.apitester;

import static junit.framework.Assert.assertEquals;

import java.net.URI;

import org.apache.http.HttpStatus;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import de.devbliss.apitester.dummyserver.DummyApiServer;
import de.devbliss.apitester.dummyserver.DummyDto;
import de.devbliss.apitester.factory.impl.DefaultPostFactory;

/**
 * Tests the methods of {@link Poster} and its delegates against an embedded local instance of
 * {@link DummyApiServer} with "real" HTTP requests.
 * 
 * @author hschuetz
 * 
 */
public class PosterIntegrationTest {

    private DummyApiServer server;

    @Before
    public void setUp() throws Exception {
        server = new DummyApiServer();
        server.start();
    }

    @After
    public void shutDown() throws Exception {
        server.stop();
    }

    @Test
    public void testPostOk() throws Exception {
        URI uri = server.buildGetRequestUri(HttpStatus.SC_OK);
        ApiResponse response = Poster.post(uri);
        ApiTestUtil.assertOk(response);
    }

    @Test
    public void testPostOkWithPayload() throws Exception {
        DummyDto payload = createPayload();
        URI uri = server.buildGetRequestUri(HttpStatus.SC_OK);
        ApiResponse response = Poster.post(uri, payload);
        ApiTestUtil.assertOk(response);
        DummyDto result = response.payloadJsonAs(DummyDto.class);
        assertEquals(payload, result);
    }

    @Test
    public void testPostOkWithOwnPostFactory() throws Exception {
        URI uri = server.buildGetRequestUri(HttpStatus.SC_OK);
        ApiResponse response = Poster.post(uri, new DefaultPostFactory());
        ApiTestUtil.assertOk(response);
    }

    @Test
    public void testPostOkWithOwnTestState() throws Exception {
        URI uri = server.buildGetRequestUri(HttpStatus.SC_OK);
        TestState testState = new TestState(ApiTesterModule.createHttpClient());
        ApiResponse response = Poster.post(uri, testState);
        ApiTestUtil.assertOk(response);
    }

    @Test
    public void testPostOkWithOwnPostFactoryAndTestState() throws Exception {
        URI uri = server.buildGetRequestUri(HttpStatus.SC_OK);
        TestState testState = new TestState(ApiTesterModule.createHttpClient());
        ApiResponse response = Poster.post(uri, new DefaultPostFactory(), testState);
        ApiTestUtil.assertOk(response);
    }

    @Test
    public void testPostOkWithPayloadAndOwnPostFactory() throws Exception {
        DummyDto payload = createPayload();
        URI uri = server.buildGetRequestUri(HttpStatus.SC_OK);
        ApiResponse response = Poster.post(uri, payload, new DefaultPostFactory());
        ApiTestUtil.assertOk(response);
        DummyDto result = response.payloadJsonAs(DummyDto.class);
        assertEquals(payload, result);
    }

    @Test
    public void testPostOkWithPayloadAndOwnTestState() throws Exception {
        DummyDto payload = createPayload();
        URI uri = server.buildGetRequestUri(HttpStatus.SC_OK);
        TestState testState = new TestState(ApiTesterModule.createHttpClient());
        ApiResponse response = Poster.post(uri, payload, testState);
        ApiTestUtil.assertOk(response);
        DummyDto result = response.payloadJsonAs(DummyDto.class);
        assertEquals(payload, result);
    }

    @Test
    public void testPostOkWithPayloadAndOwnPostFactoryAndTestState() throws Exception {
        DummyDto payload = createPayload();
        URI uri = server.buildGetRequestUri(HttpStatus.SC_OK);
        TestState testState = new TestState(ApiTesterModule.createHttpClient());
        ApiResponse response = Poster.post(uri, payload, testState, new DefaultPostFactory());
        ApiTestUtil.assertOk(response);
        DummyDto result = response.payloadJsonAs(DummyDto.class);
        assertEquals(payload, result);
    }

    private DummyDto createPayload() {
        return new DummyDto("Don't care, just some text", 1981, Boolean.FALSE);
    }
}
