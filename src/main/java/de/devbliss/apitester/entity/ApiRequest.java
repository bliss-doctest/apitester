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

package de.devbliss.apitester.entity;

import java.net.URI;
import java.util.Locale;
import java.util.Map;

import org.apache.http.HttpRequest;

import com.google.common.collect.ImmutableMap;

/**
 * Data container for the most important parts of a HTTP request. Easier to use than
 * {@link HttpRequest}.
 * 
 * @author bmary
 * 
 */
public class ApiRequest {

    public final Map<String, String> headers;
    public final Map<String, String> cookies;
    public final URI uri;
    public final String httpMethod;

    public ApiRequest(
            URI uri,
            String httpMethod,
            Map<String, String> headers,
            Map<String, String> cookies) {
        this.uri = uri;
        this.httpMethod = httpMethod;
        this.headers = ImmutableMap.copyOf(headers);
        this.cookies = ImmutableMap.copyOf(cookies);
    }

    /**
     * Get the header value with the given name
     * 
     * @param name The name of the header. As per the RFC, header names are case
     *            insensitive
     * @return The value, or null if no header with that name was found
     */
    public String getHeader(String name) {
        return headers.get(name.toLowerCase(Locale.ENGLISH));
    }

    /**
     * Get the cookie value with the given name
     * 
     * @param name The name of the cookie.
     * @return The value, or null if no cookie with that name was found
     */
    public String getCookie(String name) {
        return cookies.get(name.toLowerCase(Locale.ENGLISH));
    }
}
