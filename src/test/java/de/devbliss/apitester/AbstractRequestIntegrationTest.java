package de.devbliss.apitester;

import java.util.HashMap;
import java.util.Map;

import org.junit.Ignore;

import de.devbliss.apitester.dummyserver.DummyDto;

@Ignore
public abstract class AbstractRequestIntegrationTest {

    protected static final String HEADER_VALUE1 = "header_value1";
    protected static final String HEADER_NAME1 = "header_name1";
    protected static final String HEADER_VALUE2 = "header_value2";
    protected static final String HEADER_NAME2 = "header_name2";
    protected static final String COOKIE_VALUE_1 = "cookie_value_1";
    protected static final String COOKIE_NAME_1 = "cookie_name_1";
    protected static final String COOKIE_VALUE_2 = "cookie_value_2";
    protected static final String COOKIE_NAME_2 = "cookie_name_2";

    protected static final String HEADER_NAME_CONTENTTYPE = "content-type";
    protected static final String HEADER_VALUE_CONTENTTYPE_JSON = "application/json; charset=UTF-8";
    protected static final String HEADER_VALUE_CONTENTTYPE_TEXT = "text/plain; charset=UTF-8";

    public AbstractRequestIntegrationTest() {
        super();
    }

    protected Map<String,String> createCustomHeaders() {
    	Map<String, String> returnValue = new HashMap<String, String>();
    	returnValue.put(HEADER_NAME1, HEADER_VALUE1);
    	returnValue.put(HEADER_NAME2, HEADER_VALUE2);
    	return returnValue;
    }

    protected DummyDto createPayload() {
        return new DummyDto("Don't care, just some text", 1981, Boolean.FALSE);
    }

}