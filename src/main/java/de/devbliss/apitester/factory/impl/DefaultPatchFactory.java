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

import java.io.IOException;
import java.net.URI;

import org.apache.http.client.methods.HttpPatch;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;

import com.google.gson.Gson;

import de.devbliss.apitester.factory.PatchFactory;
import de.devbliss.apitester.factory.PutFactory;

/**
 * Default implementation that converts the payload to JSON (if there is one) and sends it as
 * request body (UTF8-encoded).
 *
 * @author hschuetz
 *
 */
public class DefaultPatchFactory implements PatchFactory {
    private static final String ENCODING = "UTF-8";
    private final Gson gson = new Gson();

    public HttpPatch createPatchRequest(URI uri, Object payload) throws IOException {
    	HttpPatch request = new HttpPatch(uri);

        if (payload != null) {
            String json = gson.toJson(payload);
            StringEntity entity = new StringEntity(json, ENCODING);
            entity.setContentType(ContentType.APPLICATION_JSON.getMimeType());
            request.setEntity(entity);
        }

        return request;
    }
}
