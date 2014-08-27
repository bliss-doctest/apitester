/*
 * Copyright 2014, devbliss GmbH
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

import com.google.gson.Gson;
import com.google.inject.Inject;
import java.io.IOException;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;

/**
 * Helps building entities from payloads.
 * 
 * @author Henning Schütz <henning.schuetz@devbliss.com>
 *
 */
public class EntityBuilder {

    private static final String ENCODING = "UTF-8";
    private final Gson gson = new Gson();

    @Inject
    public EntityBuilder() {

    }

    /**
     * Builds an entity from a given raw payload. If the payload is a string, it is directly used as payload, and the
     * entity's content type is text/plain, otherwise, it is serialized to JSON and the content type is set to
     * application/json.
     * 
     * @param payload payload which must not be null
     * @return entity
     * @throws IOException
     */
    public StringEntity buildEntity(Object payload) throws IOException {
        boolean payloadIsString = payload instanceof String;
        String payloadAsString;

        if (payloadIsString) {
            payloadAsString = (String) payload;
        } else {
            payloadAsString = gson.toJson(payload);
        }

        StringEntity entity = new StringEntity(payloadAsString, ENCODING);

        if (payloadIsString) {
            entity.setContentType(ContentType.TEXT_PLAIN.getMimeType());
        } else {
            entity.setContentType(ContentType.APPLICATION_JSON.getMimeType());
        }

        return entity;
    }
}
