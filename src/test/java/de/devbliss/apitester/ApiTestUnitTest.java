package de.devbliss.apitester;

import static junit.framework.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.net.URI;

import org.apache.http.Header;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.RequestLine;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.message.BasicHeader;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import de.devbliss.apitester.factory.DeleteFactory;
import de.devbliss.apitester.factory.GetFactory;
import de.devbliss.apitester.factory.HttpDeleteWithBody;
import de.devbliss.apitester.factory.PostFactory;
import de.devbliss.apitester.factory.PutFactory;

@RunWith(MockitoJUnitRunner.class)
public class ApiTestUnitTest {

    private static final String URI_STRING = "http://www.example.com";

    private static final int STATUS_CODE_TEAPOT = 418;

    @Mock
    private GetFactory defaultGetFactory;
    @Mock
    private PostFactory defaultPostFactory;
    @Mock
    private DeleteFactory defaultDeleteFactory;
    @Mock
    private PutFactory defaultPutFactory;
    @Mock
    private HttpClient httpClient;
    @Mock
    private HttpResponse response;
    @Mock
    private HttpRequest request;
    @Mock
    private StatusLine statusLine;
    @Mock
    private RequestLine requestLine;
    @Mock
    private GetFactory myGetFactory;
    @Mock
    private PostFactory myPostFactory;
    @Mock
    private PutFactory myPutFactory;
    @Mock
    private DeleteFactory myDeleteFactory;
    @Mock
    private HttpGet httpGet;
    @Mock
    private HttpPost httpPost;
    @Mock
    private HttpPut httpPut;
    @Mock
    private HttpDelete httpDelete;
    @Mock
    private HttpDeleteWithBody httpDeleteWithBody;

    private URI uri;
    private TestState testState;
    private Header[] headers;

    @Before
    public void setUp() throws Exception {
        uri = new URI(URI_STRING);
        headers = new Header[1];
        headers[0] = new BasicHeader("name", "value");
        when(response.getStatusLine()).thenReturn(statusLine);
        when(httpClient.execute(any(HttpUriRequest.class))).thenReturn(response);
        testState = new TestState(httpClient, null);

        when(httpGet.getRequestLine()).thenReturn(requestLine);
        when(httpGet.getAllHeaders()).thenReturn(headers);

        when(httpPost.getRequestLine()).thenReturn(requestLine);
        when(httpPost.getAllHeaders()).thenReturn(headers);

        when(httpPut.getRequestLine()).thenReturn(requestLine);
        when(httpPut.getAllHeaders()).thenReturn(headers);

        when(httpDelete.getRequestLine()).thenReturn(requestLine);
        when(httpDelete.getAllHeaders()).thenReturn(headers);
        when(httpDeleteWithBody.getRequestLine()).thenReturn(requestLine);
        when(httpDeleteWithBody.getAllHeaders()).thenReturn(headers);

        when(response.getAllHeaders()).thenReturn(headers);
    }

    @Test
    public void testGetUsesDefaultGetFactory() throws Exception {
        when(defaultGetFactory.createGetRequest(uri)).thenReturn(httpGet);

        ApiTest apiTest = createApiTest();
        apiTest.get(uri);
        verify(defaultGetFactory).createGetRequest(eq(uri));
    }

    @Test
    public void testGetUsesSpecifiedGetFactory() throws Exception {
        when(myGetFactory.createGetRequest(uri)).thenReturn(httpGet);

        ApiTest apiTest = createApiTest();
        apiTest.get(uri, myGetFactory);
        verify(myGetFactory).createGetRequest(eq(uri));
    }

    @Test
    public void testPostUsesDefaultPostFactory() throws Exception {
        when(defaultPostFactory.createPostRequest(eq(uri), any(Object.class))).thenReturn(httpPost);

        ApiTest apiTest = createApiTest();
        Object payload = new Object();
        apiTest.post(uri, payload);
        verify(defaultPostFactory).createPostRequest(eq(uri), eq(payload));
    }

    @Test
    public void testPostUsesSpecifiedPostFactory() throws Exception {
        when(myPostFactory.createPostRequest(eq(uri), any(Object.class))).thenReturn(httpPost);

        ApiTest apiTest = createApiTest();
        Object payload = new Object();
        apiTest.post(uri, payload, myPostFactory);
        verify(myPostFactory).createPostRequest(eq(uri), eq(payload));
    }

    @Test
    public void testPutUsesDefaultPutFactory() throws Exception {
        when(defaultPutFactory.createPutRequest(eq(uri), any(Object.class))).thenReturn(httpPut);

        ApiTest apiTest = createApiTest();
        Object payload = new Object();
        apiTest.put(uri, payload);
        verify(defaultPutFactory).createPutRequest(eq(uri), eq(payload));
    }

    @Test
    public void testPutUsesSpecifiedPutFactory() throws Exception {
        when(myPutFactory.createPutRequest(eq(uri), any(Object.class))).thenReturn(httpPut);

        ApiTest apiTest = createApiTest();
        Object payload = new Object();
        apiTest.put(uri, payload, myPutFactory);
        verify(myPutFactory).createPutRequest(eq(uri), eq(payload));
    }

    @Test
    public void testDeleteUsesDefaultDeleteFactory() throws Exception {
        when(defaultDeleteFactory.createDeleteRequest(uri)).thenReturn(httpDelete);

        ApiTest apiTest = createApiTest();
        apiTest.delete(uri);
        verify(defaultDeleteFactory).createDeleteRequest(eq(uri));
    }

    @Test
    public void testDeleteUsesSpecifiedDeleteFactory() throws Exception {
        when(myDeleteFactory.createDeleteRequest(uri)).thenReturn(httpDelete);

        ApiTest apiTest = createApiTest();
        apiTest.delete(uri, null, myDeleteFactory);
        verify(myDeleteFactory).createDeleteRequest(eq(uri));
    }

    @Test
    public void testDeleteWithPayloadUsesGivenDeleteFactory() throws Exception {
        when(defaultDeleteFactory.createDeleteRequest(eq(uri), any(Object.class))).thenReturn(
                httpDeleteWithBody);

        ApiTest apiTest = createApiTest();
        Object payload = new Object();
        apiTest.delete(uri, payload);
        verify(defaultDeleteFactory).createDeleteRequest(eq(uri), eq(payload));
    }

    @Test
    public void testDeleteWithPayloadUsesSpecifiedDeleteFactory() throws Exception {
        when(myDeleteFactory.createDeleteRequest(eq(uri), any(Object.class))).thenReturn(
                httpDeleteWithBody);

        ApiTest apiTest = createApiTest();
        Object payload = new Object();
        apiTest.delete(uri, payload, myDeleteFactory);
        verify(myDeleteFactory).createDeleteRequest(eq(uri), eq(payload));
    }

    @Test
    public void testGetStatusCode() throws Exception {
        when(defaultGetFactory.createGetRequest(uri)).thenReturn(httpGet);
        when(statusLine.getStatusCode()).thenReturn(STATUS_CODE_TEAPOT);

        ApiTest apiTest = createApiTest();
        ApiResponse response = apiTest.get(uri).apiResponse;
        assertEquals(STATUS_CODE_TEAPOT, response.httpStatus);
        ApiTestUtil.assertStatus(response, STATUS_CODE_TEAPOT);
    }

    @Test
    public void testPostStatusCode() throws Exception {
        when(defaultPostFactory.createPostRequest(eq(uri), any(Object.class))).thenReturn(httpPost);
        when(statusLine.getStatusCode()).thenReturn(STATUS_CODE_TEAPOT);

        ApiTest apiTest = createApiTest();
        Object payload = new Object();
        ApiResponse response = apiTest.post(uri, payload).apiResponse;
        assertEquals(STATUS_CODE_TEAPOT, response.httpStatus);
        ApiTestUtil.assertStatus(response, STATUS_CODE_TEAPOT);
    }

    @Test
    public void testPutStatusCode() throws Exception {
        when(defaultPutFactory.createPutRequest(eq(uri), any(Object.class))).thenReturn(httpPut);
        when(statusLine.getStatusCode()).thenReturn(STATUS_CODE_TEAPOT);

        ApiTest apiTest = createApiTest();
        ApiResponse response = apiTest.put(uri).apiResponse;
        assertEquals(STATUS_CODE_TEAPOT, response.httpStatus);
        ApiTestUtil.assertStatus(response, STATUS_CODE_TEAPOT);
    }

    @Test
    public void testPutWithPayloadStatusCode() throws Exception {
        when(defaultPutFactory.createPutRequest(eq(uri), any(Object.class))).thenReturn(httpPut);
        when(statusLine.getStatusCode()).thenReturn(STATUS_CODE_TEAPOT);

        ApiTest apiTest = createApiTest();
        Object payload = new Object();
        ApiResponse response = apiTest.put(uri, payload).apiResponse;
        assertEquals(STATUS_CODE_TEAPOT, response.httpStatus);
        ApiTestUtil.assertStatus(response, STATUS_CODE_TEAPOT);
    }

    @Test
    public void testDeleteStatusCode() throws Exception {
        when(defaultDeleteFactory.createDeleteRequest(uri)).thenReturn(httpDelete);
        when(statusLine.getStatusCode()).thenReturn(STATUS_CODE_TEAPOT);

        ApiTest apiTest = createApiTest();
        ApiResponse response = apiTest.delete(uri).apiResponse;
        assertEquals(STATUS_CODE_TEAPOT, response.httpStatus);
        ApiTestUtil.assertStatus(response, STATUS_CODE_TEAPOT);
    }

    @Test
    public void testDeleteWithPayloadStatusCode() throws Exception {
        when(defaultDeleteFactory.createDeleteRequest(eq(uri), any(Object.class))).thenReturn(
                httpDeleteWithBody);
        when(statusLine.getStatusCode()).thenReturn(STATUS_CODE_TEAPOT);

        ApiTest apiTest = createApiTest();
        Object payload = new Object();
        ApiResponse response = apiTest.delete(uri, payload).apiResponse;
        assertEquals(STATUS_CODE_TEAPOT, response.httpStatus);
        ApiTestUtil.assertStatus(response, STATUS_CODE_TEAPOT);
    }

    private ApiTest createApiTest() {
        ApiTest apiTest = new ApiTest();
        apiTest.setDefaultDeleteFactory(defaultDeleteFactory);
        apiTest.setDefaultGetFactory(defaultGetFactory);
        apiTest.setDefaultPutFactory(defaultPutFactory);
        apiTest.setDefaultPostFactory(defaultPostFactory);
        apiTest.setTestState(testState);
        return apiTest;
    }

}
