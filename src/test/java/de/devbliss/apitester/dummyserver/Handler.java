package de.devbliss.apitester.dummyserver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Handler that can be mocked
 *
 * @author jroper
 */
public interface Handler {
    void handle(HttpServletRequest request, HttpServletResponse response) throws Exception;
}
