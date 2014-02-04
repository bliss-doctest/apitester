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

import org.apache.commons.io.IOUtils;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;

import com.google.gson.Gson;

import de.devbliss.apitester.ApiTest;

/**
 * Dummy request handler that handles all requests for {@link DummyApiServer}.
 * It is limited to the following actions:
 * <ul>
 * <li><strong>GET</strong>-requests: expects an int representing a desired HTTP response code to be
 * the last part of the path, e.g. http://localhost:3333/gettest/200 => sends a response with given
 * http response code and a JSON representation of the {@link DummyDto#createSampleInstance()}
 * instance in the response body if the desired response code is 200 OK</li>
 * <li><strong>POST</strong>-requests: same like GET, but simply returns the content of the request
 * body in case desired response code is 200 OK</li>
 * </ul>
 *
 *
 * @author hschuetz
 *
 */
public class DummyRequestHandler extends AbstractHandler {
    static final String GET_PATH_PREFIX = "/gettest/";
    static final String POST_PATH_PREFIX = "/posttest/";
    private static final String CONTENT_TYPE = "application/json;charset=utf-8";
    private static final String CONTENT_TYPE_ERROR = "text/html;charset=utf-8";
    private final Gson gson = new Gson();

    public void handle(String target, Request baseRequest, HttpServletRequest request,
            HttpServletResponse response) throws IOException, ServletException {
        ApiTest.HTTP_REQUEST method = ApiTest.HTTP_REQUEST.valueOf(request.getMethod());

        switch (method) {
            case GET:
                handleGet(target, response);
                break;
            case DELETE:
            case POST:
            case PATCH:
            case PUT:
            	handlePostPatchPut(target, request, response);
                break;
        }

        baseRequest.setHandled(true);
    }

    private void handleGet(String path, HttpServletResponse response) throws IOException {

        try {
            int desiredResponseCode = parseDesiredResponseCode(path);
            response.setStatus(desiredResponseCode);
            response.setContentType(CONTENT_TYPE);

            if (desiredResponseCode == HttpServletResponse.SC_OK) {
                response.getWriter().write(gson.toJson(DummyDto.createSampleInstance()));
            }
        } catch (Exception e) {
            handleException(e, response);
        }
    }

    private void handlePostPatchPut(String target, HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        try {
            int desiredResponseCode = parseDesiredResponseCode(target);
            response.setStatus(desiredResponseCode);
            response.setContentType(request.getContentType());

            if (desiredResponseCode == HttpServletResponse.SC_OK) {
                String requestBody = IOUtils.toString(request.getInputStream());

                if (requestBody != null) {
                    response.getWriter().write(requestBody);
                }
            }
        } catch (Exception e) {
            handleException(e, response);
        }
    }



    private void handleException(Exception e, HttpServletResponse response) throws IOException {
        response.setContentType(CONTENT_TYPE_ERROR);
        response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        response.getWriter().write("exception type: " + e.getClass());
        response.getWriter().write(e.getMessage());
    }

    private int parseDesiredResponseCode(String path) {
        String[] parts = path.split("/");
        return Integer.parseInt(parts[parts.length - 1]);
    }

}
