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

import java.io.IOException;
import java.net.URI;
import java.util.Map;

import org.apache.http.client.methods.HttpPatch;

import de.devbliss.apitester.requestprocess.RequestCreator;

/**
 * Contains static methods to perform PATCH requests. If you want to make more requests in a series
 * sharing the same {@link TestState} and using the same {@link PatchFactory}, consider using
 * {@link ApiTest} which is wrapping that stuff for you.
 *
 * @author mbankmann
 *
 */
public class Patcher {

    public static Context patch(URI uri) throws IOException {
        return patch(uri, null, null, null);
    }

	public static Context patch(URI uri, Map<String, String> additionalHeaders) throws IOException {
        return patch(uri, null, null, additionalHeaders);
    }

    public static Context patch(URI uri, TestState testState, Map<String, String> additionalHeaders) throws IOException {
        return patch(uri, null, testState, additionalHeaders);
    }

    public static Context patch(URI uri, Object payload, Map<String, String> additionalHeaders) throws IOException {
        return patch(uri, payload, null, additionalHeaders);
    }

    public static Context patch(URI uri, TestState testState) throws IOException {
        return patch(uri, null, testState, null);
    }

    public static Context patch(URI uri, Object payload) throws IOException {
        return patch(uri, payload, null, null);
    }

    public static Context patch(URI uri, Object payload, TestState testState) throws IOException {
        return patch(uri, payload, testState, null);
    }

    public static Context patch(URI uri, Object payload, TestState testState, Map<String, String> additionalHeaders)
            throws IOException {

        if (testState == null) {
            testState = ApiTesterModule.createTestState();
        }

        HttpPatch request = RequestCreator.createPatch(uri, payload, testState, additionalHeaders);
        return RequestCreator.makeTheCall(request, payload, testState, additionalHeaders);
    }
}
