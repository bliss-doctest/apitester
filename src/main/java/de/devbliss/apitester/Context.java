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


/**
 * Wrapper object containing the {@link ApiRequest} object and the corresponding {@link ApiResponse}
 * .
 * 
 * @author bmary
 * 
 */
public class Context {

    public final ApiResponse apiResponse;
    public final ApiRequest apiRequest;

    public Context(ApiResponse apiResponse, ApiRequest apiRequest) {
        this.apiResponse = apiResponse;
        this.apiRequest = apiRequest;
    }
}
