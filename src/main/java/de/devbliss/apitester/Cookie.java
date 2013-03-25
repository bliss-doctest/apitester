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

import org.apache.http.impl.cookie.BasicClientCookie;

import java.util.Date;

/**
 * A cookie
 *
 * @author jroper
 */
public class Cookie {
    public final String name;
    public final String value;
    public final Date expires;
    public final String path;
    public final String domain;
    public final boolean httpOnly;
    public final boolean secure;

    public Cookie(String name, String value) {
        this(name, value, null);
    }

    public Cookie(String name, String value, Date expires) {
        this(name, value, expires, null, null, false, false);
    }

    public Cookie(String name, String value, Date expires, String path,
            String domain, boolean httpOnly, boolean secure) {
        this.name = name;
        this.value = value;
        this.expires = expires;
        this.path = path;
        this.domain = domain;
        this.httpOnly = httpOnly;
        this.secure = secure;
    }

    Cookie(org.apache.http.cookie.Cookie cookie) {
        this.name = cookie.getName();
        this.value = cookie.getValue();
        this.expires = cookie.getExpiryDate();
        this.path = cookie.getPath();
        this.domain = cookie.getDomain();
        this.httpOnly = false;
        this.secure = cookie.isSecure();
    }

    org.apache.http.cookie.Cookie toApacheCookie() {
        BasicClientCookie cookie = new BasicClientCookie(name, value);
        cookie.setExpiryDate(expires);
        cookie.setPath(path);
        cookie.setDomain(domain);
        cookie.setSecure(secure);
        return cookie;
    }

    public String getName() {
        return name;
    }

    public String getValue() {
        return value;
    }

    public Date getExpires() {
        return expires;
    }

    public String getPath() {
        return path;
    }

    public String getDomain() {
        return domain;
    }

    public boolean isHttpOnly() {
        return httpOnly;
    }

    public boolean isSecure() {
        return secure;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Cookie cookie = (Cookie) o;

        if (httpOnly != cookie.httpOnly) return false;
        if (secure != cookie.secure) return false;
        if (domain != null ? !domain.equals(cookie.domain) : cookie.domain != null) return false;
        if (expires != null ? !expires.equals(cookie.expires) : cookie.expires != null) return false;
        if (name != null ? !name.equals(cookie.name) : cookie.name != null) return false;
        if (path != null ? !path.equals(cookie.path) : cookie.path != null) return false;
        if (value != null ? !value.equals(cookie.value) : cookie.value != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (value != null ? value.hashCode() : 0);
        result = 31 * result + (expires != null ? expires.hashCode() : 0);
        result = 31 * result + (path != null ? path.hashCode() : 0);
        result = 31 * result + (domain != null ? domain.hashCode() : 0);
        result = 31 * result + (httpOnly ? 1 : 0);
        result = 31 * result + (secure ? 1 : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Cookie{" +
                "name='" + name + '\'' +
                ", value='" + value + '\'' +
                ", expires=" + expires +
                ", path='" + path + '\'' +
                ", domain='" + domain + '\'' +
                ", httpOnly=" + httpOnly +
                ", secure=" + secure +
                '}';
    }
}
