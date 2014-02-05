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

import org.apache.http.client.methods.HttpRequestBase;

import de.devbliss.apitester.ApiTest;
import de.devbliss.apitester.ApiTesterModule;
import de.devbliss.apitester.entity.Context;
import de.devbliss.apitester.entity.TestState;


/**
 * Contains static methods to perform DELETE requests. If you want to make more requests in a series
 * sharing the same {@link TestState} and using the same {@link DeleteFactory}, consider using
 * {@link ApiTest} which is wrapping that stuff for you.
 *
 * @author hschuetz
 *
 */
public class Deleter {

    public static Context delete(URI uri) throws IOException {
        return delete(uri, null, null, null);
    }

    public static Context delete(URI uri, Map<String, String> additionalHeaders) throws IOException {
        return delete(uri, null, null, additionalHeaders);
    }

    public static Context delete(URI uri, TestState testState, Map<String, String> additionalHeaders) throws IOException {
        return delete(uri, testState, null, additionalHeaders);
    }

    public static Context delete(URI uri, TestState testState) throws IOException {
        return delete(uri, testState, null, null);
    }

    public static Context delete(URI uri, Object payload) throws IOException {
        return delete(uri, null, payload, null);
    }

    public static Context delete(URI uri, TestState testState, Object payload) throws IOException {
        return delete(uri, testState, payload, null);
    }

    public static Context delete(URI uri, TestState testState, Object payload, Map<String, String> additionalHeaders) throws IOException {

        if (testState == null) {
            testState = ApiTesterModule.createTestState();
        }

        HttpRequestBase request = null;

        if (payload != null) {
            request = RequestUtil.createDelete(uri, payload, testState, additionalHeaders);
        } else {
            request = RequestUtil.createDelete(uri, testState, additionalHeaders);
        }

        return RequestUtil.call(request, testState);
    }
}
