/*******************************************************************************
 * Copyright 2013 Francesco Cina'
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package com.jporm.persistor.generator.manipulator;

import java.lang.reflect.InvocationTargetException;

import com.jporm.persistor.generator.PropertyPersistor;
import com.jporm.persistor.generator.valuechecker.ValueChecker;
import com.jporm.persistor.generator.valuechecker.ValueCheckerFactory;

/**
 *
 * @author Francesco Cina'
 *
 *         Apr 1, 2012
 */
public class GeneratorManipulatorImpl<BEAN, P> extends GeneratorManipulator<BEAN> {

    private final PropertyPersistor<BEAN, P, ?> fieldManipulator;
    private final ValueChecker<P> valueChecker;

    public GeneratorManipulatorImpl(final PropertyPersistor<BEAN, P, ?> fieldManipulator) throws SecurityException {
        this.fieldManipulator = fieldManipulator;
        valueChecker = ValueCheckerFactory.getValueChecker(fieldManipulator.propertyType());
    }

    @Override
    public boolean hasGenerator() {
        return true;
    }

    @Override
    public boolean useGenerator(final BEAN bean) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {
        P value = this.fieldManipulator.getPropertyValueFromBean(bean);
        return valueChecker.useGenerator(value);
    }
}
