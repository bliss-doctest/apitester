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
import java.net.ServerSocket;
import java.net.URI;
import java.net.URISyntaxException;

import org.apache.http.client.utils.URIBuilder;
import org.eclipse.jetty.server.Server;

/**
 * Simple HTTP server using Jetty. Tries to find a free port on instanciation and is ready for
 * requests after {@link #start(boolean)} has been called. All requests are handled by
 * {@link DummyRequestHandler}, so read its documentation for information on how requests are
 * handled.
 * 
 * Use convenience methods <code>build...RequestUri(...)</code> to create URIs for frequently used
 * requests.
 * 
 * Don't forget to call {@link #stop()} after finishing your tests to free the port again.
 * 
 * @author hschuetz
 * 
 */
public class DummyApiServer {

    private static final int MIN_PORT = 3000;
    private static final int MAX_PORT = 10000;
    private final int port;
    private Server server;
    private final DelegateHandler delegateHandler = new DelegateHandler();

    public DummyApiServer() {
        port = findFreePort();
    }

    public void start(boolean mocked) throws Exception {
        server = new Server(port);
        if (mocked) {
            server.setHandler(delegateHandler);
        } else {
            server.setHandler(new DummyRequestHandler());
        }
        server.start();

        new Thread(new Runnable() {

            public void run() {
                try {
                    server.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public void stop() throws Exception {
        server.stop();
    }

    public void setHandler(Handler handler) {
        delegateHandler.setHandler(handler);
    }

    /**
     * Builds an URI for GET requests, see {@link DummyRequestHandler} for details.
     * 
     * @param desiredResponseCode
     * @return
     * @throws URISyntaxException
     */
    public URI buildGetRequestUri(int desiredResponseCode) throws URISyntaxException {
        return buildRequestUri(DummyRequestHandler.GET_PATH_PREFIX + desiredResponseCode);
    }

    /**
     * Builds an URI for POST requests, see {@link DummyRequestHandler} for details.
     * 
     * @param desiredResponseCode
     * @return
     * @throws URISyntaxException
     */
    public URI buildPostRequestUri(int desiredResponseCode) throws URISyntaxException {
        return buildRequestUri(DummyRequestHandler.POST_PATH_PREFIX + desiredResponseCode);
    }

    public URI buildRequestUri(String pathOnServer) throws URISyntaxException {
        URIBuilder uriBuilder = new URIBuilder();
        uriBuilder.setScheme("http");
        uriBuilder.setHost("localhost");
        uriBuilder.setPort(port);
        uriBuilder.setPath(pathOnServer);
        return uriBuilder.build();
    }

    private int findFreePort() {
        int port = MIN_PORT;
        for (; port < MAX_PORT; port++) {
            try {
                new ServerSocket(port).close();
                // it was free, use it
                break;
            } catch (IOException e) {
                // so what?
            }
        }
        return port;
    }
}
