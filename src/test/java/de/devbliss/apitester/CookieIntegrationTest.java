package de.devbliss.apitester;

import static de.devbliss.apitester.dummyserver.HandlerUtils.handleErrors;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import de.devbliss.apitester.dummyserver.DummyApiServer;
import de.devbliss.apitester.dummyserver.Handler;

public class CookieIntegrationTest {
    private DummyApiServer server;
    private ApiTest apiTest;

    @Before
    public void setUp() throws Exception {
        server = new DummyApiServer();
        server.start(true);
        apiTest = new ApiTest();
    }

    @After
    public void shutDown() throws Exception {
        apiTest.shutdown();
        server.stop();
    }

    @Test
    public void testGetCookie() throws Exception {
        server.setHandler(new Handler() {
            public void handle(HttpServletRequest request, HttpServletResponse response)
                    throws Exception {
                response.addCookie(new javax.servlet.http.Cookie("name", "value"));
            }
        });
        apiTest.get(server.buildRequestUri("blah"));
        assertThat(apiTest.getTestState().getCookieValue("name"), equalTo("value"));
        Cookie cookie = apiTest.getTestState().getCookie("name");
        assertThat(cookie, notNullValue());
        assertThat(cookie.getDomain(), equalTo("localhost"));
        assertThat(cookie.getPath(), equalTo("/"));
        assertThat(cookie.getExpires(), nullValue());
    }

    @Test
    public void testAddCookie() throws Exception {
        apiTest.getTestState().addCookie(
                new Cookie("foo", "bar", null, "/", "localhost", false, false));
        server.setHandler(new Handler() {
            public void handle(HttpServletRequest request, HttpServletResponse response)
                    throws Exception {
                assertThat(request.getCookies(), notNullValue());
                assertThat(request.getCookies().length, equalTo(1));
                javax.servlet.http.Cookie cookie = request.getCookies()[0];
                assertThat(cookie.getName(), equalTo("foo"));
                assertThat(cookie.getValue(), equalTo("bar"));
            }
        });
        handleErrors(apiTest.get(server.buildRequestUri("blah")).apiResponse);
    }

    @Test
    public void testCookiesPersistedBetweenRequests() throws Exception {
        server.setHandler(new Handler() {
            public void handle(HttpServletRequest request, HttpServletResponse response)
                    throws Exception {
                response.addCookie(new javax.servlet.http.Cookie("batman", "robin"));
            }
        });
        apiTest.get(server.buildRequestUri("blah"));
        server.setHandler(new Handler() {
            public void handle(HttpServletRequest request, HttpServletResponse response)
                    throws Exception {
                assertThat(request.getCookies(), notNullValue());
                assertThat(request.getCookies().length, equalTo(1));
                javax.servlet.http.Cookie cookie = request.getCookies()[0];
                assertThat(cookie.getName(), equalTo("batman"));
                assertThat(cookie.getValue(), equalTo("robin"));
            }
        });
        handleErrors(apiTest.get(server.buildRequestUri("blah")).apiResponse);
    }
}
