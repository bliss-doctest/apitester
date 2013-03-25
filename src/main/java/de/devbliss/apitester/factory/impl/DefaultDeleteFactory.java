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

import org.apache.http.client.methods.HttpDelete;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;

import com.google.gson.Gson;

import de.devbliss.apitester.factory.DeleteFactory;
import de.devbliss.apitester.factory.HttpDeleteWithBody;

/**
 * Default implementation.
 * 
 * @author hschuetz
 * 
 */
public class DefaultDeleteFactory implements DeleteFactory {
    private static final String ENCODING = "UTF-8";
    private final Gson gson = new Gson();

    public HttpDelete createDeleteRequest(URI uri) throws IOException {
        return new HttpDelete(uri);
    }

    public HttpDeleteWithBody createDeleteRequest(URI uri, Object payload) throws IOException {
        HttpDeleteWithBody request = new HttpDeleteWithBody(uri);

        if (payload != null) {
            String json = gson.toJson(payload);
            StringEntity entity = new StringEntity(json, ENCODING);
            entity.setContentType(ContentType.APPLICATION_JSON.getMimeType());
            request.setEntity(entity);
        }

        return request;
    }
}