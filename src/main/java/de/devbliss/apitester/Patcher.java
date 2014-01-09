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

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPatch;

import de.devbliss.apitester.factory.DeleteFactory;
import de.devbliss.apitester.factory.PatchFactory;

/**
 * Contains static methods to perform PATCH requests. If you want to make more requests in a series
 * sharing the same {@link TestState} and using the same {@link PatchFactory}, consider using
 * {@link ApiTest} which is wrapping that stuff for you.
 *
 * @author mbankmann
 *
 */
public class Patcher {

	public static Context patch(URI uri, Map<String, String> additionalHeaders) throws IOException {
        return patch(uri, null, null, null, additionalHeaders);
    }

    public static Context patch(URI uri, TestState testState, Map<String, String> additionalHeaders) throws IOException {
        return patch(uri, testState, null, null, additionalHeaders);
    }

    public static Context patch(URI uri, Object payload, Map<String, String> additionalHeaders) throws IOException {
        return patch(uri, null, null, payload, additionalHeaders);
    }

    public static Context patch(URI uri, Object payload, TestState testState, Map<String, String> additionalHeaders) throws IOException {
        return patch(uri, testState, null, payload, additionalHeaders);
    }

    public static Context patch(URI uri) throws IOException {
        return patch(uri, null, null, null, null);
    }

    public static Context patch(URI uri, PatchFactory patchFactory) throws IOException {
        return patch(uri, null, patchFactory, null, null);
    }

    public static Context patch(URI uri, TestState testState) throws IOException {
        return patch(uri, testState, null, null, null);
    }

    public static Context patch(URI uri, TestState testState, PatchFactory patchFactory)
            throws IOException {
        return patch(uri, testState, patchFactory, null, null);
    }

    public static Context patch(URI uri, Object payload) throws IOException {
        return patch(uri, null, null, payload, null);
    }

    public static Context patch(URI uri, Object payload, PatchFactory patchFactory) throws IOException {
        return patch(uri, null, patchFactory, payload, null);
    }

    public static Context patch(URI uri, Object payload, TestState testState) throws IOException {
        return patch(uri, testState, null, payload, null);
    }

    public static Context patch(URI uri, TestState testState, PatchFactory patchFactory, Object payload, Map<String, String> additionalHeaders)
            throws IOException {

        if (patchFactory == null) {
        	patchFactory = ApiTesterModule.createPatchFactory();
        }

        if (testState == null) {
            testState = ApiTesterModule.createTestState();
        }

        HttpPatch request = patchFactory.createPatchRequest(uri, payload);

        if(additionalHeaders != null) {
        	for (String headerName : additionalHeaders.keySet()) {
        		request.addHeader(headerName, additionalHeaders.get(headerName));
			}
        }

        // IMPORTANT: we have to get the cookies from the testState before making the request
        // because this request could add some cookie to the testState (e.g: the response could have
        // a Set-Cookie header)
        ApiRequest apiRequest =
                ApiTestUtil.convertToApiRequest(uri, request, testState.getCookies());

        HttpResponse response = testState.client.execute(request);
        ApiResponse apiResponse = ApiTestUtil.convertToApiResponse(response);
        return new Context(apiResponse, apiRequest);
    }
}
