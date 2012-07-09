package de.devbliss.apitester;

import org.apache.http.client.HttpClient;

import com.google.inject.Inject;

/**
 * Currently, this only holds an instance of {@link HttpClient}. Use the same instance of this e.g.
 * if you need to make a sequence of HTTP calls and want the state (mostly cookies) to be remembered
 * for all calls.
 * 
 * @author hschuetz
 * 
 */
public class TestState {

    public final HttpClient client;

    @Inject
    public TestState(HttpClient httpClient) {
        this.client = httpClient;
    }
}
