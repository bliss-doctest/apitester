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

package de.devbliss.apitester.transport;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static org.junit.Assert.assertNull;

import java.net.URI;

import org.apache.http.HttpStatus;
import org.apache.http.impl.client.DefaultHttpClient;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import de.devbliss.apitester.ApiTestUtil;
import de.devbliss.apitester.ApiTesterModule;
import de.devbliss.apitester.dummyserver.DummyApiServer;
import de.devbliss.apitester.dummyserver.DummyDto;
import de.devbliss.apitester.entity.ApiRequest;
import de.devbliss.apitester.entity.ApiResponse;
import de.devbliss.apitester.entity.Context;
import de.devbliss.apitester.entity.TestState;

/**
 * Tests the methods of {@link Getter} and its delegates against an embedded local instance of
 * {@link DummyApiServer} with "real" HTTP requests.
 *
 * @author hschuetz
 *
 */
@RunWith(MockitoJUnitRunner.class)
public class GetterIntegrationTest extends AbstractRequestIntegrationTest {

    @Test
    public void testGetOk() throws Exception {
        URI uri = server.buildGetRequestUri(HttpStatus.SC_OK);
        Context context = Getter.get(uri);
        ApiResponse response = context.apiResponse;
        ApiTestUtil.assertOk(response);
        DummyDto result = response.payloadJsonAs(DummyDto.class);
        assertEquals(DummyDto.createSampleInstance(), result);
    }

    @Test
    public void testGetNoContent() throws Exception {
        URI uri = server.buildGetRequestUri(HttpStatus.SC_NO_CONTENT);
        Context context = Getter.get(uri);
        ApiResponse response = context.apiResponse;
        ApiTestUtil.assertNoContent(response);
        DummyDto result = response.payloadJsonAs(DummyDto.class);
        assertFalse(DummyDto.createSampleInstance().equals(result));
    }

    @Test
    public void testGetOkWithOwnGetFactory() throws Exception {
        URI uri = server.buildGetRequestUri(HttpStatus.SC_OK);
        Context context = Getter.get(uri);
        ApiResponse response = context.apiResponse;
        ApiTestUtil.assertOk(response);
        DummyDto result = response.payloadJsonAs(DummyDto.class);
        assertEquals(DummyDto.createSampleInstance(), result);
    }

    @Test
    public void testGetOkWithOwnTestState() throws Exception {
        URI uri = server.buildGetRequestUri(HttpStatus.SC_OK);
        Context context = Getter.get(uri, ApiTesterModule.createTestState());
        ApiResponse response = context.apiResponse;
        ApiTestUtil.assertOk(response);
        DummyDto result = response.payloadJsonAs(DummyDto.class);
        assertEquals(DummyDto.createSampleInstance(), result);
    }

    @Test
    public void testGetOkWithOwnGetFactoryAndTestState() throws Exception {
        URI uri = server.buildGetRequestUri(HttpStatus.SC_OK);
        TestState testState = ApiTesterModule.createTestState();
        Context context = Getter.get(uri, testState);
        ApiResponse response = context.apiResponse;
        ApiTestUtil.assertOk(response);
        DummyDto result = response.payloadJsonAs(DummyDto.class);
        assertEquals(DummyDto.createSampleInstance(), result);
    }

    @Test
    public void testGetWithHeaders() throws Exception {
        URI uri = server.buildGetRequestUri(HttpStatus.SC_OK);
        TestState testState = ApiTesterModule.createTestState();
        Context context = Getter.get(uri, testState, createCustomHeaders());
        ApiResponse response = context.apiResponse;
        ApiRequest request = context.apiRequest;
        ApiTestUtil.assertOk(response);
        DummyDto result = response.payloadJsonAs(DummyDto.class);
        assertEquals(DummyDto.createSampleInstance(), result);
        assertEquals(HEADER_VALUE1, request.getHeader(HEADER_NAME1));
        assertEquals(HEADER_VALUE2, request.getHeader(HEADER_NAME2));
    }

    @Test
    public void testGetWithCookiesAndHeaders() throws Exception {
        URI uri = server.buildGetRequestUri(HttpStatus.SC_OK);
        TestState testState = new TestState(new DefaultHttpClient(), cookieStore);
        Context context = Getter.get(uri, testState, createCustomHeaders());
        ApiResponse response = context.apiResponse;
        ApiRequest request = context.apiRequest;
        ApiTestUtil.assertOk(response);
        DummyDto result = response.payloadJsonAs(DummyDto.class);
        assertEquals(DummyDto.createSampleInstance(), result);
        assertEquals(HEADER_VALUE1, request.getHeader(HEADER_NAME1));
        assertEquals(HEADER_VALUE2, request.getHeader(HEADER_NAME2));
        assertEquals(COOKIE_VALUE_1, request.getCookie(COOKIE_NAME_1));
        assertEquals(COOKIE_VALUE_2, request.getCookie(COOKIE_NAME_2));

        assertNull(request.getCookie(HEADER_NAME1));
        assertNull(request.getHeader(COOKIE_NAME_1));
    }
}
