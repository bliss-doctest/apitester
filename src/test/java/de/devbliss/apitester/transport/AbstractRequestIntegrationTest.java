package de.devbliss.apitester.transport;

import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.client.CookieStore;
import org.apache.http.cookie.Cookie;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.mockito.Mock;

import de.devbliss.apitester.dummyserver.DummyApiServer;
import de.devbliss.apitester.dummyserver.DummyDto;

@Ignore
public abstract class AbstractRequestIntegrationTest {

    protected static final String HEADER_VALUE1 = "header_value1";
    protected static final String HEADER_NAME1 = "header_name1";
    protected static final String HEADER_VALUE2 = "header_value2";
    protected static final String HEADER_NAME2 = "header_name2";
    protected static final String COOKIE_VALUE_1 = "cookie_value_1";
    protected static final String COOKIE_NAME_1 = "cookie_name_1";
    protected static final String COOKIE_VALUE_2 = "cookie_value_2";
    protected static final String COOKIE_NAME_2 = "cookie_name_2";

    protected static final String HEADER_NAME_CONTENTTYPE = "content-type";
    protected static final String HEADER_VALUE_CONTENTTYPE_JSON = "application/json; charset=UTF-8";
    protected static final String HEADER_VALUE_CONTENTTYPE_TEXT = "text/plain; charset=UTF-8";

    @Mock
    protected CookieStore cookieStore;
    @Mock
    protected Cookie cookie1;
    @Mock
    protected Cookie cookie2;

    protected DummyApiServer server;
    protected List<Cookie> cookies;

    public AbstractRequestIntegrationTest() {
        super();
    }

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

    protected Map<String,String> createCustomHeaders() {
    	Map<String, String> returnValue = new HashMap<String, String>();
    	returnValue.put(HEADER_NAME1, HEADER_VALUE1);
    	returnValue.put(HEADER_NAME2, HEADER_VALUE2);
    	return returnValue;
    }

    protected DummyDto createPayload() {
        return new DummyDto("Don't care, just some text", 1981, Boolean.FALSE);
    }

}