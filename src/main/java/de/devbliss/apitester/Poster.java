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

import static de.devbliss.apitester.Constants.HEADER_NAME_CONTENT_TYPE;

import java.io.IOException;
import java.net.URI;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;

import com.google.gson.Gson;


/**
 * Contains static methods to perform POST requests. If you want to make more requests in a series
 * sharing the same {@link TestState} and using the same {@link PostFactory}, consider using {@link ApiTest} which is
 * wrapping that stuff for you.
 * 
 * @author hschuetz
 * 
 */
public class Poster {

    private static final String ENCODING = "UTF-8";
    private static final Gson gson = new Gson();

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

        HttpPost request = new HttpPost(uri);
        ContentType contentType = null;

        if (payload != null) {
            String payloadAsString = null;

            if (payload instanceof String) {
                contentType = ContentType.create(ContentType.TEXT_PLAIN.getMimeType(), ENCODING);
                payloadAsString = (String) payload;
            } else {
                contentType = ContentType.APPLICATION_JSON;
                payloadAsString = gson.toJson(payload);
            }

            StringEntity entity = new StringEntity(payloadAsString, ENCODING);
            entity.setContentType(contentType.getMimeType());
            request.setEntity(entity);
        } else {
            contentType = ContentType.TEXT_PLAIN;
        }

        if (testState == null) {
            testState = ApiTesterModule.createTestState();
        }

        if (additionalHeaders != null) {
            for (String headerName : additionalHeaders.keySet()) {
                request.addHeader(headerName, additionalHeaders.get(headerName));
            }
        }

        if (additionalHeaders == null || !additionalHeaders.keySet().contains(HEADER_NAME_CONTENT_TYPE)) {
            request.addHeader(HEADER_NAME_CONTENT_TYPE, contentType.toString());
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
