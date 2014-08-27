/*
 * Copyright 2013, devbliss GmbH
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

package de.devbliss.apitester.factory.impl;

import com.google.inject.Inject;
import de.devbliss.apitester.factory.PutFactory;
import java.io.IOException;
import java.net.URI;
import org.apache.http.client.methods.HttpPut;

/**
 * Default implementation that converts the payload to JSON (if there is one) and sends it as
 * request body (UTF8-encoded).
 *
 * @author hschuetz
 *
 */
public class DefaultPutFactory implements PutFactory {

    private final EntityBuilder entityBuilder;

    @Inject
    public DefaultPutFactory(EntityBuilder entityBuilder) {
        this.entityBuilder = entityBuilder;
    }

    public HttpPut createPutRequest(URI uri, Object payload) throws IOException {
        HttpPut request = new HttpPut(uri);

        if (payload != null) {
            request.setEntity(entityBuilder.buildEntity(payload));
        }

        return request;
    }
}
