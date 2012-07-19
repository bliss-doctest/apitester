package de.devbliss.apitester;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpClient;

import com.google.inject.Inject;

import javax.annotation.Nullable;
import java.util.List;

/**
 * Currently, this only holds an instance of {@link HttpClient}. Use the same instance of this e.g.
 * if you need to make a sequence of HTTP calls and want the state (mostly cookies) to be remembered
 * for all calls.
 * 
 * @author hschuetz
 * 
 */
// Not singleton. ever.
public class TestState {

    public final HttpClient client;
    public final CookieStore cookieStore;

    @Inject
    public TestState(HttpClient httpClient, CookieStore cookieStore) {
        this.client = httpClient;
        this.cookieStore = cookieStore;
    }

    /**
     * Get the cookie with the given name, if one can be found.  Note, it is possible that there
     * might be multiple cookies with the same name, this will just return the first one found.
     *
     * @param name The name of the cookie
     * @return The cookie, or null if no cookie is found
     */
    public Cookie getCookie(String name) {
        for (org.apache.http.cookie.Cookie cookie : cookieStore.getCookies()) {
            if (cookie.getName().equals(name)) {
                return new Cookie(cookie);
            }
        }
        return null;
    }

    /**
     * Get the value of the cookie with the given name, if one can be found.  Note, it is
     * possible that there might be multiple cookies with the same name, this will just
     * return the first one found.
     *
     * @param name The name of the cookie
     * @return The value of the cookie, or null if no cookie with that name was found
     */
    public String getCookieValue(String name) {
        for (org.apache.http.cookie.Cookie cookie : cookieStore.getCookies()) {
            if (cookie.getName().equals(name)) {
                return cookie.getValue();
            }
        }
        return null;
    }

    /**
     * Get all the cookies in the cookie store.
     *
     * @return All of the cookies in the cookie store.
     */
    public List<Cookie> getCookies() {
        return Lists.transform(cookieStore.getCookies(), new Function<org.apache.http.cookie.Cookie, Cookie>() {
            public Cookie apply(@Nullable org.apache.http.cookie.Cookie cookie) {
                return new Cookie(cookie);
            }
        });
    }

    /**
     * Add a cookie to the cookie store.
     *
     * @param cookie The cookie to add.
     */
    public void addCookie(Cookie cookie) {
        cookieStore.addCookie(cookie.toApacheCookie());
    }

    /**
     * Clear all the cookies from the store.
     */
    public void clearCookies() {
        cookieStore.clear();
    }

}
