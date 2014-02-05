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

package de.devbliss.apitester.transport;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.when;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

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

import de.devbliss.apitester.ApiTestUtil;
import de.devbliss.apitester.ApiTesterModule;
import de.devbliss.apitester.dummyserver.DummyApiServer;
import de.devbliss.apitester.dummyserver.DummyDto;
import de.devbliss.apitester.entity.ApiRequest;
import de.devbliss.apitester.entity.ApiResponse;
import de.devbliss.apitester.entity.Context;
import de.devbliss.apitester.entity.TestState;
import de.devbliss.apitester.transport.Getter;

/**
 * Tests the methods of {@link Getter} and its delegates against an embedded local instance of
 * {@link DummyApiServer} with "real" HTTP requests.
 *
 * @author hschuetz
 *
 */
@RunWith(MockitoJUnitRunner.class)
public class GetterIntegrationTest {

    private static final String HEADER_VALUE1 = "header_value1";
    private static final String HEADER_NAME1 = "header_name1";
    private static final String HEADER_VALUE2 = "header_value2";
    private static final String HEADER_NAME2 = "header_name2";
    private static final String COOKIE_VALUE_1 = "cookie_value_1";
    private static final String COOKIE_NAME_1 = "cookie_name_1";
    private static final String COOKIE_VALUE_2 = "cookie_value_2";
    private static final String COOKIE_NAME_2 = "cookie_name_2";

    @Mock
    private CookieStore cookieStore;
    @Mock
    private Cookie cookie1;
    @Mock
    private Cookie cookie2;

    private DummyApiServer server;
    private ArrayList<Cookie> cookies;

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
        Context context = Getter.get(uri);
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
        Context context = Getter.get(uri, testState);
        ApiResponse response = context.apiResponse;
        ApiTestUtil.assertOk(response);
        DummyDto result = response.payloadJsonAs(DummyDto.class);
        assertEquals(DummyDto.createSampleInstance(), result);
    }

    @Test
    public void testGetWithHeaders() throws Exception {
        URI uri = server.buildGetRequestUri(HttpStatus.SC_OK);
        TestState testState = ApiTesterModule.createTestState();
        Context context = Getter.get(uri, testState, createCustomHeaders());
        ApiResponse response = context.apiResponse;
        ApiRequest request = context.apiRequest;
        ApiTestUtil.assertOk(response);
        DummyDto result = response.payloadJsonAs(DummyDto.class);
        assertEquals(DummyDto.createSampleInstance(), result);
        assertEquals(HEADER_VALUE1, request.getHeader(HEADER_NAME1));
        assertEquals(HEADER_VALUE2, request.getHeader(HEADER_NAME2));
    }

    @Test
    public void testGetWithCookiesAndHeaders() throws Exception {
        URI uri = server.buildGetRequestUri(HttpStatus.SC_OK);
        TestState testState = new TestState(new DefaultHttpClient(), cookieStore);
        Context context = Getter.get(uri, testState, createCustomHeaders());
        ApiResponse response = context.apiResponse;
        ApiRequest request = context.apiRequest;
        ApiTestUtil.assertOk(response);
        DummyDto result = response.payloadJsonAs(DummyDto.class);
        assertEquals(DummyDto.createSampleInstance(), result);
        assertEquals(HEADER_VALUE1, request.getHeader(HEADER_NAME1));
        assertEquals(HEADER_VALUE2, request.getHeader(HEADER_NAME2));
        assertEquals(COOKIE_VALUE_1, request.getCookie(COOKIE_NAME_1));
        assertEquals(COOKIE_VALUE_2, request.getCookie(COOKIE_NAME_2));

        assertNull(request.getCookie(HEADER_NAME1));
        assertNull(request.getHeader(COOKIE_NAME_1));
    }

    private Map<String,String> createCustomHeaders() {
    	Map<String, String> returnValue = new HashMap<String, String>();
    	returnValue.put(HEADER_NAME1, HEADER_VALUE1);
    	returnValue.put(HEADER_NAME2, HEADER_VALUE2);
    	return returnValue;
    }
}
