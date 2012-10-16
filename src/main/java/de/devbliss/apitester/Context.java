package de.devbliss.apitester;

import org.apache.http.HttpRequest;

/**
 * Wrapper object containing the {@link HttpRequest} object and the corresponding
 * {@link ApiResponse}.
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
