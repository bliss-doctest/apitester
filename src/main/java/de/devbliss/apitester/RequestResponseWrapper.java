package de.devbliss.apitester;

import org.apache.http.HttpRequest;

/**
 * Wrapper object containing the {@link HttpRequest} object and the corresponding
 * {@link ApiResponse}.
 * 
 * @author bmary
 * 
 */
public class RequestResponseWrapper {

    public final ApiResponse apiResponse;
    public final HttpRequest httpRequest;

    public RequestResponseWrapper(ApiResponse apiResponse, HttpRequest httpRequest) {
        this.apiResponse = apiResponse;
        this.httpRequest = httpRequest;
    }
}
