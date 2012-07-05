package de.devbliss.apitester;

import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultHttpClient;

public class TestState {

    public final HttpClient client = new DefaultHttpClient();
}
