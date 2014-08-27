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
import de.devbliss.apitester.factory.PatchFactory;
import java.io.IOException;
import java.net.URI;
import org.apache.http.client.methods.HttpPatch;

/**
 * Default implementation that converts the payload to JSON (if there is one) and sends it as
 * request body (UTF8-encoded).
 *
 * @author hschuetz
 *
 */
public class DefaultPatchFactory implements PatchFactory {
    private final EntityBuilder entityBuilder;

    @Inject
    public DefaultPatchFactory(EntityBuilder entityBuilder) {
        this.entityBuilder = entityBuilder;
    }

    public HttpPatch createPatchRequest(URI uri, Object payload) throws IOException {
        HttpPatch request = new HttpPatch(uri);

        if (payload != null) {
            request.setEntity(entityBuilder.buildEntity(payload));
        }

        return request;
    }
}
