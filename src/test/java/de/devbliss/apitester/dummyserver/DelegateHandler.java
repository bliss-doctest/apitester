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

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;

/**
 * Request handler that delegates to a mock handler
 */
public class DelegateHandler extends AbstractHandler {
    private Handler handler;

    public void setHandler(Handler handler) {
        this.handler = handler;
    }

    public void handle(String target, Request baseRequest, HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException {
        if (handler != null) {
            try {
                handler.handle(request, response);
            } catch (Exception e) {
                HandlerUtils.sendError(e, response);
            } catch (AssertionError e) {
                HandlerUtils.sendError(e, response);
            }
        } else {
            response.sendError(404, "No mock handler set");
        }
    }
}
