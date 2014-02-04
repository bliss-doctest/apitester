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
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.when;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpStatus;
import org.apache.http.client.CookieStore;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.DefaultHttpClient;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import de.devbliss.apitester.dummyserver.DummyApiServer;
import de.devbliss.apitester.dummyserver.DummyDto;
import de.devbliss.apitester.requestprocess.Poster;

/**
 * Tests the methods of {@link Poster} and its delegates against an embedded local instance of
 * {@link DummyApiServer} with "real" HTTP requests.
 *
 * @author hschuetz
 *
 */
@RunWith(MockitoJUnitRunner.class)
public class PosterIntegrationTest extends AbstractRequestIntegrationTest {

    @Mock
    private CookieStore cookieStore;
    @Mock
    private Cookie cookie1;
    @Mock
    private Cookie cookie2;

    private DummyApiServer server;
    private List<Cookie> cookies;

    @Before
    public void setUp() throws Exception {
        server = new DummyApiServer();
        server.start(false);

        when(cookie1.getName()).thenReturn(COOKIE_NAME_1);
        when(cookie1.getValue()).thenReturn(COOKIE_VALUE_1);
        when(cookie2.getName()).thenReturn(COOKIE_NAME_2);
        when(cookie2.getValue()).thenReturn(COOKIE_VALUE_2);

        cookies = new ArrayList<Cookie>();
        cookies.add(cookie1);
        cookies.add(cookie2);
        when(cookieStore.getCookies()).thenReturn(cookies);
    }

    @After
    public void shutDown() throws Exception {
        server.stop();
    }

    @Test
    public void testPostOk() throws Exception {
        URI uri = server.buildPostRequestUri(HttpStatus.SC_OK);
        Context wrapper = Poster.post(uri);
        ApiRequest request = wrapper.apiRequest;
        ApiResponse response = wrapper.apiResponse;
        ApiTestUtil.assertOk(response);
        assertEquals(uri, request.uri);
        assertEquals("POST", request.httpMethod);
    }

    @Test
    public void testPostOkWithJsonPayload() throws Exception {
        DummyDto payload = createPayload();
        URI uri = server.buildGetRequestUri(HttpStatus.SC_OK);
        Context wrapper = Poster.post(uri, payload);
        ApiRequest request = wrapper.apiRequest;
        ApiResponse response = wrapper.apiResponse;
        ApiTestUtil.assertOk(response);
        DummyDto result = response.payloadJsonAs(DummyDto.class);
        assertEquals(payload, result);
        assertEquals(uri, request.uri);
        assertEquals("POST", request.httpMethod);
        assertEquals(HEADER_VALUE_CONTENTTYPE_JSON, response.getHeader(HEADER_NAME_CONTENTTYPE));
    }

    @Test
    public void testPostOkWithStringPayload() throws Exception {
        final String payload = "just some string";
        URI uri = server.buildGetRequestUri(HttpStatus.SC_OK);
        Context wrapper = Poster.post(uri, payload);
        ApiRequest request = wrapper.apiRequest;
        ApiResponse response = wrapper.apiResponse;
        ApiTestUtil.assertOk(response);
        assertEquals(HEADER_VALUE_CONTENTTYPE_TEXT, response.getHeader(HEADER_NAME_CONTENTTYPE));
        assertEquals(payload, response.payload);
        assertEquals("POST", request.httpMethod);
    }

    @Test
    public void testPostOkWithOwnTestState() throws Exception {
        URI uri = server.buildGetRequestUri(HttpStatus.SC_OK);
        TestState testState = ApiTesterModule.createTestState();
        Context wrapper = Poster.post(uri, testState);
        ApiResponse response = wrapper.apiResponse;
        ApiRequest request = wrapper.apiRequest;
        ApiTestUtil.assertOk(response);
        assertEquals(uri, request.uri);
        assertEquals("POST", request.httpMethod);
    }

    @Test
    public void testPostOkWithPayloadAndOwnTestState() throws Exception {
        DummyDto payload = createPayload();
        URI uri = server.buildGetRequestUri(HttpStatus.SC_OK);
        TestState testState = ApiTesterModule.createTestState();
        Context wrapper = Poster.post(uri, payload, testState);
        ApiResponse response = wrapper.apiResponse;
        ApiRequest request = wrapper.apiRequest;
        ApiTestUtil.assertOk(response);
        DummyDto result = response.payloadJsonAs(DummyDto.class);
        assertEquals(payload, result);
        assertEquals(uri, request.uri);
        assertEquals("POST", request.httpMethod);
    }


    @Test
    public void testPostWithHeaders() throws Exception {
        DummyDto payload = createPayload();
        URI uri = server.buildGetRequestUri(HttpStatus.SC_OK);
        TestState testState = ApiTesterModule.createTestState();
        Context wrapper = Poster.post(uri,payload,testState,createCustomHeaders());
        ApiResponse response = wrapper.apiResponse;
        ApiRequest request = wrapper.apiRequest;
        ApiTestUtil.assertOk(response);
        assertEquals(HEADER_VALUE1, request.getHeader(HEADER_NAME1));
        assertEquals(HEADER_VALUE2, request.getHeader(HEADER_NAME2));
        assertEquals(uri, request.uri);
        assertEquals("POST", request.httpMethod);
    }

    @Test
    public void testPostWithCookiesAndHeaders() throws Exception {
        DummyDto payload = createPayload();
        URI uri = server.buildGetRequestUri(HttpStatus.SC_OK);
        TestState testState = new TestState(new DefaultHttpClient(), cookieStore);
        Context wrapper = Poster.post(uri, payload, testState, createCustomHeaders());
        ApiResponse response = wrapper.apiResponse;
        ApiRequest request = wrapper.apiRequest;
        ApiTestUtil.assertOk(response);
        assertEquals(uri, request.uri);
        assertEquals("POST", request.httpMethod);
        assertEquals(HEADER_VALUE1, request.getHeader(HEADER_NAME1));
        assertEquals(HEADER_VALUE2, request.getHeader(HEADER_NAME2));
        assertEquals(COOKIE_VALUE_1, request.getCookie(COOKIE_NAME_1));
        assertEquals(COOKIE_VALUE_2, request.getCookie(COOKIE_NAME_2));

        assertNull(request.getCookie(HEADER_NAME1));
        assertNull(request.getHeader(COOKIE_NAME_1));

    }
}
