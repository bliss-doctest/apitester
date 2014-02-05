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

import java.io.IOException;
import java.net.URI;
import java.util.Map;

import org.apache.http.client.methods.HttpPost;

import de.devbliss.apitester.ApiTest;
import de.devbliss.apitester.ApiTesterModule;
import de.devbliss.apitester.entity.Context;
import de.devbliss.apitester.entity.TestState;

/**
 * Contains static methods to perform POST requests. If you want to make more requests in a series
 * sharing the same {@link TestState} and using the same {@link PostFactory}, consider using {@link ApiTest} which is
 * wrapping that stuff for you.
 * 
 * @author hschuetz
 * 
 */
public class Poster {

    public static Context post(URI uri) throws IOException {
        return post(uri, null, null, null);
    }

    public static Context post(URI uri, Map<String, String> additionalHeaders) throws IOException {
        return post(uri, null, null, additionalHeaders);
    }

    public static Context post(URI uri, TestState testState, Map<String, String> additionalHeaders) throws IOException {
        return post(uri, null, testState, additionalHeaders);
    }

    public static Context post(URI uri, Object payload, Map<String, String> additionalHeaders) throws IOException {
        return post(uri, payload, null, additionalHeaders);
    }

    public static Context post(URI uri, TestState testState) throws IOException {
        return post(uri, null, testState, null);
    }

    public static Context post(URI uri, Object payload) throws IOException {
        return post(uri, payload, null, null);
    }

    public static Context post(URI uri, Object payload, TestState testState) throws IOException {
        return post(uri, payload, testState, null);
    }

    public static Context post(URI uri, Object payload, TestState testState, Map<String, String> additionalHeaders)
            throws IOException {

        if (testState == null) {
            testState = ApiTesterModule.createTestState();
        }

        HttpPost request = RequestUtil.createPost(uri, payload, testState, additionalHeaders);
        return RequestUtil.call(request, testState);
    }
}
