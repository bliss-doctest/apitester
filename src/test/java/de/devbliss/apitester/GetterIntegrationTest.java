package de.devbliss.apitester;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;

import java.net.URI;

import org.apache.http.HttpStatus;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import de.devbliss.apitester.dummyserver.DummyApiServer;
import de.devbliss.apitester.dummyserver.DummyDto;
import de.devbliss.apitester.factory.impl.DefaultGetFactory;

/**
 * Tests the methods of {@link Getter} and its delegates against an embedded local instance of
 * {@link DummyApiServer} with "real" HTTP requests.
 * 
 * @author hschuetz
 * 
 */
public class GetterIntegrationTest {

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
    public void testGetOk() throws Exception {
        URI uri = server.buildGetRequestUri(HttpStatus.SC_OK);
        ApiResponse response = Getter.get(uri);
        ApiTestUtil.assertOk(response);
        DummyDto result = response.payloadJsonAs(DummyDto.class);
        assertEquals(DummyDto.createSampleInstance(), result);
    }

    @Test
    public void testGetNoContent() throws Exception {
        URI uri = server.buildGetRequestUri(HttpStatus.SC_NO_CONTENT);
        ApiResponse response = Getter.get(uri);
        ApiTestUtil.assertNoContent(response);
        DummyDto result = response.payloadJsonAs(DummyDto.class);
        assertFalse(DummyDto.createSampleInstance().equals(result));
    }

    @Test
    public void testGetOkWithOwnGetFactory() throws Exception {
        URI uri = server.buildGetRequestUri(HttpStatus.SC_OK);
        ApiResponse response = Getter.get(uri, new DefaultGetFactory());
        ApiTestUtil.assertOk(response);
        DummyDto result = response.payloadJsonAs(DummyDto.class);
        assertEquals(DummyDto.createSampleInstance(), result);
    }

    @Test
    public void testGetOkWithOwnTestState() throws Exception {
        URI uri = server.buildGetRequestUri(HttpStatus.SC_OK);
        ApiResponse response = Getter.get(uri, new TestState(ApiTesterModule.createHttpClient()));
        ApiTestUtil.assertOk(response);
        DummyDto result = response.payloadJsonAs(DummyDto.class);
        assertEquals(DummyDto.createSampleInstance(), result);
    }

    @Test
    public void testGetOkWithOwnGetFactoryAndTestState() throws Exception {
        URI uri = server.buildGetRequestUri(HttpStatus.SC_OK);
        TestState testState = new TestState(ApiTesterModule.createHttpClient());
        ApiResponse response = Getter.get(uri, testState, new DefaultGetFactory());
        ApiTestUtil.assertOk(response);
        DummyDto result = response.payloadJsonAs(DummyDto.class);
        assertEquals(DummyDto.createSampleInstance(), result);
    }
}
