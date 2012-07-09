package de.devbliss.apitester;

import java.net.URI;

import org.junit.Test;

/**
 * That's really a poor example of how to use ApiTester, and also a poor unittest. TODO: Start an
 * embedded jetty providing a test REST API we can talk to and test all the fancy things.
 * 
 * @author hschuetz
 * 
 */
public class GetterIntegrationTest {

    private static final String GET_URI = "http://www.google.de";

    @Test
    public void testGetStatic() throws Exception {
        ApiResponse response = Getter.get(new URI(GET_URI));
        ApiTestUtil.assertOk(response);
    }
}
