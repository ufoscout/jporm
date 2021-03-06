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
package com.jporm.annotation.introspector.generator;

import com.jporm.annotation.GeneratorType;

/**
 *
 * @author cinafr
 *
 */
public class GeneratorInfoImpl implements GeneratorInfo {

    private final GeneratorType generatorType;
    private final String name;
    private final boolean valid;

    public GeneratorInfoImpl(final GeneratorType generatorType, final String name, final boolean valid) {
        this.generatorType = generatorType;
        this.name = name;
        this.valid = valid;

    }

    @Override
    public GeneratorType getGeneratorType() {
        return generatorType;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public boolean isValid() {
        return valid;
    }

}
