package de.devbliss.apitester.factory.impl;

import static org.junit.Assert.assertEquals;
import org.apache.commons.io.IOUtils;
import org.apache.http.entity.StringEntity;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

/**
 * Unit test for {@link EntityBuilder}.
 * 
 * @author Henning Schütz <henning.schuetz@devbliss.com>
 *
 */
@RunWith(MockitoJUnitRunner.class)
public class EntityBuilderTest {

    /**
     * Just some dummy class for testing JSONification of the payload.
     * 
     */
    private class SpittingCobra {
        private String name;

        @SuppressWarnings("unused")
        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

    private EntityBuilder entityBuilder = new EntityBuilder();

    @Test
    public void buildEntityFromStringPayload() throws Exception {
        final String payload = "spitting cobra";
        StringEntity result = entityBuilder.buildEntity(payload);
        assertEquals("text/plain", result.getContentType().getValue());
        assertEquals(payload, IOUtils.toString(result.getContent()));
    }

    @Test
    public void buildEntityFromObjectPayload() throws Exception {
        // prepare payload:
        SpittingCobra payload = new SpittingCobra();
        final String name = "Bob";
        payload.setName(name);

        // build entity:
        StringEntity result = entityBuilder.buildEntity(payload);
        assertEquals("application/json", result.getContentType().getValue());
        assertEquals("{\"name\":\"" + name + "\"}", IOUtils.toString(result.getContent()));
    }
}
