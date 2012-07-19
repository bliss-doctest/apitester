package de.devbliss.apitester.dummyserver;

import de.devbliss.apitester.ApiResponse;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.net.URLDecoder;
import java.net.URLEncoder;

/**
 * Handles sending assertions that were thrown on the server back to the client properly
 */
public class HandlerUtils {
    public static ApiResponse handleErrors(ApiResponse response) {
        if (response.httpStatus == 500) {
            if (response.error.contains(":")) {
                String[] error = response.error.split(":", 2);
                Throwable exception;
                try {
                    Class<? extends Throwable> exceptionClass = (Class) Class.forName(error[0]);
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
