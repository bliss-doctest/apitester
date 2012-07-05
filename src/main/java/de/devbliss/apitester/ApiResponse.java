package de.devbliss.apitester;

import org.apache.http.HttpResponse;

import com.google.gson.Gson;

/**
 * Data container for the most important parts of a HTTP response. Easier to use than
 * {@link HttpResponse}.
 * 
 * @author hschuetz
 * 
 */
public class ApiResponse {

    public final int httpStatus;
    public final String payload;

    public ApiResponse(int httpStatus, String payload) {
        this.httpStatus = httpStatus;
        this.payload = payload;
    }

    /**
     * Assumes that the payload of the response is valid JSON and tries to convert it to the given
     * type.
     * 
     * @param type
     * @return
     */
    public <DTO> DTO payloadJsonAs(Class<DTO> type) {
        return new Gson().fromJson(payload, type);
    }
}
