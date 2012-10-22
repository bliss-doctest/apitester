package de.devbliss.apitester;

import static junit.framework.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpStatus;
import org.apache.http.client.CookieStore;
import org.apache.http.client.methods.HttpPost;
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
import de.devbliss.apitester.factory.PostFactory;
import de.devbliss.apitester.factory.impl.DefaultPostFactory;

/**
 * Tests the methods of {@link Poster} and its delegates against an embedded local instance of
 * {@link DummyApiServer} with "real" HTTP requests.
 * 
 * @author hschuetz
 * 
 */
@RunWith(MockitoJUnitRunner.class)
public class PosterIntegrationTest {

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
        URI uri = server.buildGetRequestUri(HttpStatus.SC_OK);
        Context wrapper = Poster.post(uri);
        ApiRequest request = wrapper.apiRequest;
        ApiResponse response = wrapper.apiResponse;
        ApiTestUtil.assertOk(response);
        assertEquals(uri, request.uri);
        assertEquals("POST", request.httpMethod);
    }

    @Test
    public void testPostOkWithPayload() throws Exception {
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
    }

    @Test
    public void testPostOkWithOwnPostFactory() throws Exception {
        URI uri = server.buildGetRequestUri(HttpStatus.SC_OK);
        Context wrapper = Poster.post(uri, new DefaultPostFactory());
        ApiRequest request = wrapper.apiRequest;
        ApiResponse response = wrapper.apiResponse;
        ApiTestUtil.assertOk(response);
        assertEquals(uri, request.uri);
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
    public void testPostOkWithOwnPostFactoryAndTestState() throws Exception {
        URI uri = server.buildGetRequestUri(HttpStatus.SC_OK);
        TestState testState = ApiTesterModule.createTestState();
        Context wrapper = Poster.post(uri, new DefaultPostFactory(), testState);
        ApiResponse response = wrapper.apiResponse;
        ApiRequest request = wrapper.apiRequest;
        ApiTestUtil.assertOk(response);
        assertEquals(uri, request.uri);
        assertEquals("POST", request.httpMethod);
    }

    @Test
    public void testPostOkWithPayloadAndOwnPostFactory() throws Exception {
        DummyDto payload = createPayload();
        URI uri = server.buildGetRequestUri(HttpStatus.SC_OK);
        Context wrapper = Poster.post(uri, payload, new DefaultPostFactory());
        ApiResponse response = wrapper.apiResponse;
        ApiRequest request = wrapper.apiRequest;
        ApiTestUtil.assertOk(response);
        DummyDto result = response.payloadJsonAs(DummyDto.class);
        assertEquals(payload, result);
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
    public void testPostOkWithPayloadAndOwnPostFactoryAndTestState() throws Exception {
        DummyDto payload = createPayload();
        URI uri = server.buildGetRequestUri(HttpStatus.SC_OK);
        TestState testState = ApiTesterModule.createTestState();
        Context wrapper = Poster.post(uri, payload, testState, new DefaultPostFactory());
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
        Context wrapper = Poster.post(uri, payload, testState, getPostFactoryWithHeaders());
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
        Context wrapper = Poster.post(uri, payload, testState, getPostFactoryWithHeaders());
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
