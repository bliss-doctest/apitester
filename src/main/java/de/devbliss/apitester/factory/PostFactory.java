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

package de.devbliss.apitester.factory;

import java.io.IOException;
import java.net.URI;

import org.apache.http.client.methods.HttpPost;

/**
 * Creates POST requests. Override this if you need to add custom behaviour.
 *
 * @author hschuetz
 *
 */
public interface PostFactory {

    /**
     *
     * @param uri
     * @param payload will end up as data in response body, may be <code>null</code> if there is no
     *            use for a payload
     * @return
     * @throws IOException
     */
    HttpPost createPostRequest(URI uri, Object payload) throws IOException;

}
