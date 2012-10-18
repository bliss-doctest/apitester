package de.devbliss.apitester;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;

import java.io.IOException;
import java.net.URI;

import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import de.devbliss.apitester.dummyserver.DummyApiServer;
import de.devbliss.apitester.dummyserver.DummyDto;
import de.devbliss.apitester.factory.GetFactory;
import de.devbliss.apitester.factory.impl.DefaultGetFactory;

/**
 * Tests the methods of {@link Getter} and its delegates against an embedded local instance of
 * {@link DummyApiServer} with "real" HTTP requests.
 * 
 * @author hschuetz
 * 
 */
public class GetterIntegrationTest {

    private static final String HEADER_VALUE1 = "header_value1";
    private static final String HEADER_NAME1 = "header_name1";
    private static final String HEADER_VALUE2 = "header_value2";
    private static final String HEADER_NAME2 = "header_name2";
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
    public void testGetOk() throws Exception {
        URI uri = server.buildGetRequestUri(HttpStatus.SC_OK);
        Context context = Getter.get(uri);
        ApiResponse response = context.apiResponse;
        ApiTestUtil.assertOk(response);
        DummyDto result = response.payloadJsonAs(DummyDto.class);
        assertEquals(DummyDto.createSampleInstance(), result);
    }

    @Test
    public void testGetNoContent() throws Exception {
        URI uri = server.buildGetRequestUri(HttpStatus.SC_NO_CONTENT);
        Context context = Getter.get(uri);
        ApiResponse response = context.apiResponse;
        ApiTestUtil.assertNoContent(response);
        DummyDto result = response.payloadJsonAs(DummyDto.class);
        assertFalse(DummyDto.createSampleInstance().equals(result));
    }

    @Test
    public void testGetOkWithOwnGetFactory() throws Exception {
        URI uri = server.buildGetRequestUri(HttpStatus.SC_OK);
        Context context = Getter.get(uri, new DefaultGetFactory());
        ApiResponse response = context.apiResponse;
        ApiTestUtil.assertOk(response);
        DummyDto result = response.payloadJsonAs(DummyDto.class);
        assertEquals(DummyDto.createSampleInstance(), result);
    }

    @Test
    public void testGetOkWithOwnTestState() throws Exception {
        URI uri = server.buildGetRequestUri(HttpStatus.SC_OK);
        Context context = Getter.get(uri, ApiTesterModule.createTestState());
        ApiResponse response = context.apiResponse;
        ApiTestUtil.assertOk(response);
        DummyDto result = response.payloadJsonAs(DummyDto.class);
        assertEquals(DummyDto.createSampleInstance(), result);
    }

    @Test
    public void testGetOkWithOwnGetFactoryAndTestState() throws Exception {
        URI uri = server.buildGetRequestUri(HttpStatus.SC_OK);
        TestState testState = ApiTesterModule.createTestState();
        Context context = Getter.get(uri, testState, new DefaultGetFactory());
        ApiResponse response = context.apiResponse;
        ApiTestUtil.assertOk(response);
        DummyDto result = response.payloadJsonAs(DummyDto.class);
        assertEquals(DummyDto.createSampleInstance(), result);
    }

    @Test
    public void testGetWithHeaders() throws Exception {
        URI uri = server.buildGetRequestUri(HttpStatus.SC_OK);
        TestState testState = ApiTesterModule.createTestState();
        Context context = Getter.get(uri, testState, getGetFactoryWithHeaders());
        ApiResponse response = context.apiResponse;
        ApiRequest request = context.apiRequest;
        ApiTestUtil.assertOk(response);
        DummyDto result = response.payloadJsonAs(DummyDto.class);
        assertEquals(DummyDto.createSampleInstance(), result);
        assertEquals(HEADER_VALUE1, request.getHeader(HEADER_NAME1));
        assertEquals(HEADER_VALUE2, request.getHeader(HEADER_NAME2));
    }

    private GetFactory getGetFactoryWithHeaders() {
        return new GetFactory() {

            public HttpGet createGetRequest(URI uri) throws IOException {
                HttpGet request = new HttpGet(uri);
                request.setHeader(HEADER_NAME1, HEADER_VALUE1);
                request.setHeader(HEADER_NAME2, HEADER_VALUE2);
                return request;
            }
        };
    }
}
