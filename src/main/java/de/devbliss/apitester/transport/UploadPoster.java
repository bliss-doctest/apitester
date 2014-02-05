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

import java.io.File;
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
public class UploadPoster {


    public static Context post(URI uri, File fileToUpload, String paramName, Map<String, String> additionalHeaders) throws IOException {
        return post(uri, null, fileToUpload, paramName, additionalHeaders);
    }

    public static Context post(URI uri, File fileToUpload, String paramName) throws IOException {
        return post(uri, null, fileToUpload, paramName, null);
    }

    public static Context post(URI uri, TestState testState, File fileToUpload, String paramName) throws IOException {
        return post(uri, testState, fileToUpload, paramName, null);
    }

    public static Context post(URI uri, TestState testState, File fileToUpload, String paramName, Map<String, String> additionalHeaders)
            throws IOException {

        if (testState == null) {
            testState = ApiTesterModule.createTestState();
        }

        HttpPost request = RequestUtil.createUploadPost(uri, fileToUpload, testState, paramName, additionalHeaders);
        return RequestUtil.call(request, testState);
    }
}
