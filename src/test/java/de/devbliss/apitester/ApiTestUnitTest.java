/*
 * Copyright 2013, devbliss GmbH
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

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
import org.apache.http.client.CookieStore;
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

@RunWith(MockitoJUnitRunner.class)
public class ApiTestUnitTest {

    private static final String URI_STRING = "http://www.example.com";

    private static final int STATUS_CODE_TEAPOT = 418;

    @Mock
    private GetFactory defaultGetFactory;
    @Mock
    private DeleteFactory defaultDeleteFactory;
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
    @Mock
    private CookieStore cookieStore;

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
        testState = new TestState(httpClient, cookieStore);

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
        apiTest.get(uri, myGetFactory, null);
        verify(myGetFactory).createGetRequest(eq(uri));
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
        apiTest.delete(uri, null, myDeleteFactory, null);
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
        apiTest.delete(uri, payload, myDeleteFactory, null);
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
        when(statusLine.getStatusCode()).thenReturn(STATUS_CODE_TEAPOT);

        ApiTest apiTest = createApiTest();
        Object payload = new Object();
        ApiResponse response = apiTest.post(uri, payload).apiResponse;
        assertEquals(STATUS_CODE_TEAPOT, response.httpStatus);
        ApiTestUtil.assertStatus(response, STATUS_CODE_TEAPOT);
    }

    @Test
    public void testPutStatusCode() throws Exception {
        when(statusLine.getStatusCode()).thenReturn(STATUS_CODE_TEAPOT);

        ApiTest apiTest = createApiTest();
        ApiResponse response = apiTest.put(uri).apiResponse;
        assertEquals(STATUS_CODE_TEAPOT, response.httpStatus);
        ApiTestUtil.assertStatus(response, STATUS_CODE_TEAPOT);
    }

    @Test
    public void testPutWithPayloadStatusCode() throws Exception {
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

    // TODO: to setUp()
    private ApiTest createApiTest() {
        ApiTest apiTest = new ApiTest();
        apiTest.setDefaultDeleteFactory(defaultDeleteFactory);
        apiTest.setDefaultGetFactory(defaultGetFactory);
        apiTest.setTestState(testState);
        return apiTest;
    }
}
