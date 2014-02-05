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

package de.devbliss.apitester.dummyserver;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.net.URLDecoder;
import java.net.URLEncoder;

import javax.servlet.http.HttpServletResponse;

import de.devbliss.apitester.entity.ApiResponse;

/**
 * Handles sending assertions that were thrown on the server back to the client properly
 */
public class HandlerUtils {
    @SuppressWarnings("unchecked")
    public static ApiResponse handleErrors(ApiResponse response) {
        if (response.httpStatus == 500) {
            if (response.reasonPhrase.contains(":")) {
                String[] error = response.reasonPhrase.split(":", 2);
                Throwable exception;
                try {
                    Class<? extends Throwable> exceptionClass = (Class<? extends Throwable>) Class.forName(error[0]);
                    Constructor<? extends Throwable> constructor = exceptionClass.getDeclaredConstructor(String.class);
                    constructor.setAccessible(true);
                    exception = constructor.newInstance(URLDecoder.decode(error[1], "UTF-8"));
                } catch (Exception e) {
                    // It didn't work, just throw the reason as an exception
                    throw new AssertionError(e);
                }
                if (exception instanceof Error) {
                    throw (Error) exception;
                } else if (exception instanceof RuntimeException) {
                    throw (RuntimeException) exception;
                } else {
                    throw new AssertionError(exception);
                }
            }
        }
        return response;
    }

    public static void sendError(Throwable t, HttpServletResponse response) throws IOException {
        t.printStackTrace();
        response.sendError(500, t.getClass().getName() + ":" + URLEncoder.encode(t.getMessage(), "UTF-8"));
    }
}
