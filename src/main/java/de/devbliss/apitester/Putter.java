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
import org.apache.http.client.methods.HttpPut;

import de.devbliss.apitester.factory.DeleteFactory;
import de.devbliss.apitester.factory.PutFactory;

/**
 * Contains static methods to perform PUT requests. If you want to make more requests in a series
 * sharing the same {@link TestState} and using the same {@link DeleteFactory}, consider using
 * {@link ApiTest} which is wrapping that stuff for you.
 *
 * @author hschuetz
 *
 */
public class Putter {

	public static Context put(URI uri, Map<String, String> additionalHeaders) throws IOException {
        return put(uri, null, null, null, additionalHeaders);
    }

    public static Context put(URI uri, TestState testState, Map<String, String> additionalHeaders) throws IOException {
        return put(uri, testState, null, null, additionalHeaders);
    }

    public static Context put(URI uri, Object payload, Map<String, String> additionalHeaders) throws IOException {
        return put(uri, null, null, payload, additionalHeaders);
    }

    public static Context put(URI uri, Object payload, TestState testState, Map<String, String> additionalHeaders) throws IOException {
        return put(uri, testState, null, payload, additionalHeaders);
    }

    public static Context put(URI uri) throws IOException {
        return put(uri, null, null, null, null);
    }

    public static Context put(URI uri, PutFactory putFactory) throws IOException {
        return put(uri, null, putFactory, null, null);
    }

    public static Context put(URI uri, TestState testState) throws IOException {
        return put(uri, testState, null, null, null);
    }

    public static Context put(URI uri, TestState testState, PutFactory putFactory)
            throws IOException {
        return put(uri, testState, putFactory, null, null);
    }

    public static Context put(URI uri, Object payload) throws IOException {
        return put(uri, null, null, payload, null);
    }

    public static Context put(URI uri, Object payload, PutFactory putFactory) throws IOException {
        return put(uri, null, putFactory, payload, null);
    }

    public static Context put(URI uri, Object payload, TestState testState) throws IOException {
        return put(uri, testState, null, payload, null);
    }

    public static Context put(URI uri, TestState testState, PutFactory putFactory, Object payload, Map<String, String> additionalHeaders)
            throws IOException {

        if (putFactory == null) {
            putFactory = ApiTesterModule.createPutFactory();
        }

        if (testState == null) {
            testState = ApiTesterModule.createTestState();
        }

        HttpPut request = putFactory.createPutRequest(uri, payload);

        if(additionalHeaders!=null && additionalHeaders.size() > 0) {
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
