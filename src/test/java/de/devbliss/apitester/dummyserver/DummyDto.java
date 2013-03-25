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

package de.devbliss.apitester.dummyserver;

/**
 * Dummy dto used for test requests / responses to verify correct conversion from / to JSON. Its
 * {@link #equals(Object)} verifies the equalitity of all fields of the object.
 * 
 * @author hschuetz
 * 
 */
public class DummyDto {
    private final String someStringValue;
    private final int someIntValue;
    private final Boolean someBooleanValue;

    public DummyDto(String someStringValue, int someIntValue, Boolean someBooleanValue) {
        this.someBooleanValue = someBooleanValue;
        this.someIntValue = someIntValue;
        this.someStringValue = someStringValue;
    }

    public String getSomeStringValue() {
        return someStringValue;
    }

    public int getSomeIntValue() {
        return someIntValue;
    }

    public Boolean isSomeBooleanValue() {
        return someBooleanValue;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof DummyDto) {
            DummyDto other = (DummyDto) obj;
            return someStringValue != null && someStringValue.equals(other.someStringValue)
                    && someIntValue == other.someIntValue && someBooleanValue != null
                    && someBooleanValue.equals(other.someBooleanValue);
        } else {
            return super.equals(obj);
        }
    }

    /**
     * Creates a new instance of {@link DummyDto}. All results have the same dummy values, so the
     * results of any calls of this method are always equal in terms of
     * {@link DummyDto#equals(Object)}
     * 
     * @return
     */
    public static DummyDto createSampleInstance() {
        return new DummyDto("test 123", 42, Boolean.TRUE);
    }
}
