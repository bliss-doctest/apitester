package de.devbliss.apitester;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.net.URI;

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

/**
 * 
 * Test for @ApiTestUtil
 * 
 * @author katharinairrgang, bmary
 * 
 */
@RunWith(MockitoJUnitRunner.class)
public class ApiTestUtilUnitTest {

    private static final String HEADER1 = "CONTENT-TYPE";
    private static final String HEADER2 = "cookie";
    private static final String VALUE1 = "application/json";
    private static final String VALUE2 = "application/html";
    private static final int STATUS_CODE = 200;

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

    private Header[] headers;
    private URI uri;

    @Before
    public void setUp() throws Exception {
        uri = new URI("www.rofl.de/lol");
        headers = new Header[2];
        headers[0] = new BasicHeader(HEADER1, VALUE1);
        headers[1] = new BasicHeader(HEADER2, VALUE2);

        when(httpRequest.getRequestLine()).thenReturn(requestLine);
        when(httpRequest.getAllHeaders()).thenReturn(headers);

        when(httpResponse.getStatusLine()).thenReturn(statusLine);
        when(httpResponse.getAllHeaders()).thenReturn(headers);
        when(statusLine.getStatusCode()).thenReturn(STATUS_CODE);

        when(httpResponse.getEntity()).thenReturn(null);
    }

    @Test
    public void testConvertToApiRequest() {
        ApiRequest apiRequest = ApiTestUtil.convertToApiRequest(uri, httpRequest);
        assertEquals(VALUE1, apiRequest.getHeader(HEADER1));
        assertEquals(VALUE2, apiRequest.getHeader(HEADER2));
        assertFalse(apiRequest.headers.containsKey(HEADER1));
        assertEquals(uri, apiRequest.uri);
    }

    @Test
    public void testConvertToApiResponse() throws IOException {
        ApiResponse apiResponse = ApiTestUtil.convertToApiResponse(httpResponse);
        assertTrue(apiResponse.payload.isEmpty());
        assertEquals(STATUS_CODE, apiResponse.httpStatus);
        assertEquals(VALUE1, apiResponse.getHeader(HEADER1));
        assertEquals(VALUE2, apiResponse.getHeader(HEADER2));
        assertFalse(apiResponse.headers.containsKey(HEADER1));
    }
}
