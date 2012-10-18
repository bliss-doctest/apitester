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
