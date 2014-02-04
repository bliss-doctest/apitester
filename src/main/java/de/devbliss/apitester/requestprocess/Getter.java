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

package de.devbliss.apitester.requestprocess;

import java.io.IOException;
import java.net.URI;
import java.util.Map;

import org.apache.http.client.methods.HttpGet;

import de.devbliss.apitester.ApiTest;
import de.devbliss.apitester.ApiTesterModule;
import de.devbliss.apitester.Context;
import de.devbliss.apitester.TestState;

/**
 * Contains static methods to perform GET requests. If you want to make more requests in a series
 * sharing the same {@link TestState} and using the same {@link GetFactory}, consider using {@link ApiTest} which is
 * wrapping that stuff for you.
 * 
 * @author hschuetz
 * 
 */
public class Getter {

    public static Context get(URI uri) throws IOException {
        return get(uri, null, null);
    }

    public static Context get(URI uri, Map<String, String> additionalHeaders) throws IOException {
        return get(uri, null, additionalHeaders);
    }

    public static Context get(URI uri, TestState testState) throws IOException {
        return get(uri, testState, null);
    }

    public static Context get(URI uri, TestState testState, Map<String, String> additionalHeaders)
            throws IOException {

        if (testState == null) {
            testState = ApiTesterModule.createTestState();
        }

        HttpGet request = RequestCreator.createGet(uri, testState, additionalHeaders);
        return RequestCreator.makeTheCall(request, testState);
    }
}
