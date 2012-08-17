package de.devbliss.apitester;

import static junit.framework.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.net.URI;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpUriRequest;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import de.devbliss.apitester.factory.DeleteFactory;
import de.devbliss.apitester.factory.GetFactory;
import de.devbliss.apitester.factory.HttpDeleteWithBody;
import de.devbliss.apitester.factory.PostFactory;
import de.devbliss.apitester.factory.PutFactory;

public class ApiTestUnitTest {

    class MyGetFactory implements GetFactory {
        public HttpGet createGetRequest(URI uri) throws IOException {
            return null;
        }
    }

    class MyDeleteFactory implements DeleteFactory {
        public HttpDeleteWithBody createDeleteRequest(URI uri, Object payload) throws IOException {
            return null;
        }

        public HttpDelete createDeleteRequest(URI uri) throws IOException {
            return null;
        }
    }

    class MyPostFactory implements PostFactory {
        public HttpPost createPostRequest(URI uri, Object payload) throws IOException {
            return null;
        }
    }

    class MyPutFactory implements PutFactory {
        public HttpPut createPutRequest(URI uri, Object payload) throws IOException {
            return null;
        }
    }

    private static final String URI_STRING = "http://www.example.com";

    private static final int STATUS_CODE_TEAPOT = 418;

    private URI uri;

    private TestState testState;

    @Mock
    private GetFactory getFactory;
    @Mock
    private PostFactory postFactory;
    @Mock
    private DeleteFactory deleteFactory;
    @Mock
    private PutFactory putFactory;
    @Mock
    private HttpClient httpClient;
    @Mock
    private HttpResponse response;
    @Mock
    private StatusLine statusLine;

    @Mock
    private MyGetFactory myGetFactory;
    @Mock
    private MyPostFactory myPostFactory;
    @Mock
    private MyPutFactory myPutFactory;
    @Mock
    private MyDeleteFactory myDeleteFactory;

    @Before
    public void setUp() throws Exception {
        uri = new URI(URI_STRING);
        MockitoAnnotations.initMocks(this);
        when(response.getStatusLine()).thenReturn(statusLine);
        when(httpClient.execute(any(HttpUriRequest.class))).thenReturn(response);
        testState = new TestState(httpClient, null);
        when(response.getAllHeaders()).thenReturn(new Header[] {});
    }

    @Test
    public void testGetUsesGivenGetFactory() throws Exception {
        ApiTest apiTest = createApiTest();
        apiTest.get(uri);
        verify(getFactory).createGetRequest(eq(uri));
    }

    @Test
    public void testGetUsesSpecifiedGetFactory() throws Exception {
        ApiTest apiTest = createApiTest();
        apiTest.get(uri, myGetFactory);
        verify(myGetFactory).createGetRequest(eq(uri));
    }

    @Test
    public void testPostUsesGivenPostFactory() throws Exception {
        ApiTest apiTest = createApiTest();
        Object payload = new Object();
        apiTest.post(uri, payload);
        verify(postFactory).createPostRequest(eq(uri), eq(payload));
    }

    @Test
    public void testPostUsesSpecifiedPostFactory() throws Exception {
        ApiTest apiTest = createApiTest();
        Object payload = new Object();
        apiTest.post(uri, payload, myPostFactory);
        verify(myPostFactory).createPostRequest(eq(uri), eq(payload));
    }

    @Test
    public void testPutUsesGivenPutFactory() throws Exception {
        ApiTest apiTest = createApiTest();
        Object payload = new Object();
        apiTest.put(uri, payload);
        verify(putFactory).createPutRequest(eq(uri), eq(payload));
    }

    @Test
    public void testPutUsesSpecifiedPutFactory() throws Exception {
        ApiTest apiTest = createApiTest();
        Object payload = new Object();
        apiTest.put(uri, payload, myPutFactory);
        verify(myPutFactory).createPutRequest(eq(uri), eq(payload));
    }

    @Test
    public void testDeleteUsesGivenDeleteFactory() throws Exception {
        ApiTest apiTest = createApiTest();
        apiTest.delete(uri);
        verify(deleteFactory).createDeleteRequest(eq(uri));
    }

    @Test
    public void testDeleteUsesSpecifiedDeleteFactory() throws Exception {
        ApiTest apiTest = createApiTest();
        apiTest.delete(uri, null, myDeleteFactory);
        verify(myDeleteFactory).createDeleteRequest(eq(uri));
    }

    @Test
    public void testDeleteWithPayloadUsesGivenDeleteFactory() throws Exception {
        ApiTest apiTest = createApiTest();
        Object payload = new Object();
        apiTest.delete(uri, payload);
        verify(deleteFactory).createDeleteRequest(eq(uri), eq(payload));
    }

    @Test
    public void testDeleteWithPayloadUsesSpecifiedDeleteFactory() throws Exception {
        ApiTest apiTest = createApiTest();
        Object payload = new Object();
        apiTest.delete(uri, payload, myDeleteFactory);
        verify(myDeleteFactory).createDeleteRequest(eq(uri), eq(payload));
    }

    @Test
    public void testGetStatusCode() throws Exception {
        when(statusLine.getStatusCode()).thenReturn(STATUS_CODE_TEAPOT);
        ApiTest apiTest = createApiTest();
        ApiResponse response = apiTest.get(uri);
        assertEquals(STATUS_CODE_TEAPOT, response.httpStatus);
        ApiTestUtil.assertStatus(response, STATUS_CODE_TEAPOT);
    }

    @Test
    public void testPostStatusCode() throws Exception {
        when(statusLine.getStatusCode()).thenReturn(STATUS_CODE_TEAPOT);
        ApiTest apiTest = createApiTest();
        Object payload = new Object();
        ApiResponse response = apiTest.post(uri, payload);
        assertEquals(STATUS_CODE_TEAPOT, response.httpStatus);
        ApiTestUtil.assertStatus(response, STATUS_CODE_TEAPOT);
    }

    @Test
    public void testPutStatusCode() throws Exception {
        when(statusLine.getStatusCode()).thenReturn(STATUS_CODE_TEAPOT);
        ApiTest apiTest = createApiTest();
        ApiResponse response = apiTest.put(uri);
        assertEquals(STATUS_CODE_TEAPOT, response.httpStatus);
        ApiTestUtil.assertStatus(response, STATUS_CODE_TEAPOT);
    }

    @Test
    public void testPutWithPayloadStatusCode() throws Exception {
        when(statusLine.getStatusCode()).thenReturn(STATUS_CODE_TEAPOT);
        ApiTest apiTest = createApiTest();
        Object payload = new Object();
        ApiResponse response = apiTest.put(uri, payload);
        assertEquals(STATUS_CODE_TEAPOT, response.httpStatus);
        ApiTestUtil.assertStatus(response, STATUS_CODE_TEAPOT);
    }

    @Test
    public void testDeleteStatusCode() throws Exception {
        when(statusLine.getStatusCode()).thenReturn(STATUS_CODE_TEAPOT);
        ApiTest apiTest = createApiTest();
        ApiResponse response = apiTest.delete(uri);
        assertEquals(STATUS_CODE_TEAPOT, response.httpStatus);
        ApiTestUtil.assertStatus(response, STATUS_CODE_TEAPOT);
    }

    @Test
    public void testDeleteWithPayloadStatusCode() throws Exception {
        when(statusLine.getStatusCode()).thenReturn(STATUS_CODE_TEAPOT);
        ApiTest apiTest = createApiTest();
        Object payload = new Object();
        ApiResponse response = apiTest.delete(uri, payload);
        assertEquals(STATUS_CODE_TEAPOT, response.httpStatus);
        ApiTestUtil.assertStatus(response, STATUS_CODE_TEAPOT);
    }

    private ApiTest createApiTest() {
        ApiTest apiTest = new ApiTest();
        apiTest.setDefaultDeleteFactory(deleteFactory);
        apiTest.setDefaultGetFactory(getFactory);
        apiTest.setDefaultPutFactory(putFactory);
        apiTest.setDefaultPostFactory(postFactory);
        apiTest.setTestState(testState);
        return apiTest;
    }

}
