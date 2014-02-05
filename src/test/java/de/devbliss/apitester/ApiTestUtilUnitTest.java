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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.RequestLine;
import org.apache.http.StatusLine;
import org.apache.http.message.BasicHeader;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import de.devbliss.apitester.entity.ApiRequest;
import de.devbliss.apitester.entity.ApiResponse;
import de.devbliss.apitester.entity.Cookie;

/**
 * 
 * Test for @ApiTestUtil
 * 
 * @author katharinairrgang, bmary
 * 
 */
@RunWith(MockitoJUnitRunner.class)
public class ApiTestUtilUnitTest {

    private static final String HEADER_NAME_1 = "CONTENT-TYPE";
    private static final String HEADER_NAME_2 = "cookie";
    private static final String HEADER_VALUE_1 = "application/json";
    private static final String HEADER_VALUE_2 = "application/html";
    private static final int STATUS_CODE = 200;
    private static final String COOKIE_NAME_1 = "cookie1";
    private static final String COOKIE_VALUE_1 = "cookie2";
    private static final String COOKIE_NAME_2 = "cookie_value_1";
    private static final String COOKIE_VALUE_2 = "cookie_value_2";
    private static final String HTTP_METHOD = "http_method";

    @Mock
    private StatusLine statusLine;
    @Mock
    private HttpRequest httpRequest;
    @Mock
    private HttpResponse httpResponse;
    @Mock
    private RequestLine requestLine;
    @Mock
    private HttpEntity httpEntity;

    private Header[] responseHeaders;
    private URI uri;
    private List<Cookie> requestCookies;

    @Before
    public void setUp() throws Exception {
        uri = new URI("www.rofl.de/lol");
        responseHeaders = new Header[2];
        responseHeaders[0] = new BasicHeader(HEADER_NAME_1, HEADER_VALUE_1);
        responseHeaders[1] = new BasicHeader(HEADER_NAME_2, HEADER_VALUE_2);

        requestCookies = new ArrayList<Cookie>();
        requestCookies.add(new Cookie(COOKIE_NAME_1, COOKIE_VALUE_1));
        requestCookies.add(new Cookie(COOKIE_NAME_2, COOKIE_VALUE_2));

        when(httpRequest.getRequestLine()).thenReturn(requestLine);
        when(requestLine.getMethod()).thenReturn(HTTP_METHOD);
        when(httpRequest.getAllHeaders()).thenReturn(responseHeaders);

        when(httpResponse.getStatusLine()).thenReturn(statusLine);
        when(httpResponse.getAllHeaders()).thenReturn(responseHeaders);
        when(statusLine.getStatusCode()).thenReturn(STATUS_CODE);

        when(httpResponse.getEntity()).thenReturn(null);
    }

    @Test
    public void testConvertToApiRequest() {
        ApiRequest apiRequest = ApiTestUtil.convertToApiRequest(uri, httpRequest, requestCookies);
        assertEquals(HEADER_VALUE_1, apiRequest.getHeader(HEADER_NAME_1));
        assertEquals(HEADER_VALUE_2, apiRequest.getHeader(HEADER_NAME_2));
        assertEquals(COOKIE_VALUE_1, apiRequest.getCookie(COOKIE_NAME_1));
        assertEquals(COOKIE_VALUE_2, apiRequest.getCookie(COOKIE_NAME_2));
        assertEquals(uri, apiRequest.uri);
        assertEquals(HTTP_METHOD, apiRequest.httpMethod);
    }

    @Test
    public void testConvertToApiResponse() throws IOException {
        ApiResponse apiResponse = ApiTestUtil.convertToApiResponse(httpResponse);
        assertTrue(apiResponse.payload.isEmpty());
        assertEquals(STATUS_CODE, apiResponse.httpStatus);
        assertEquals(HEADER_VALUE_1, apiResponse.getHeader(HEADER_NAME_1));
        assertEquals(HEADER_VALUE_2, apiResponse.getHeader(HEADER_NAME_2));
        assertFalse(apiResponse.headers.containsKey(HEADER_NAME_1));
    }
}
