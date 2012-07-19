package de.devbliss.apitester.dummyserver;

import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;

/**
 * Request handler that delegates to a mock handler
 */
public class DelegateHandler extends AbstractHandler {
    private Handler handler;

    public void setHandler(Handler handler) {
        this.handler = handler;
    }

    public void handle(String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
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
