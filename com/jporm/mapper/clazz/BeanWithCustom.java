/*******************************************************************************
 * Copyright 2014 Francesco Cina'
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
package com.jporm.mapper.clazz;

import com.jporm.annotation.Generator;
import com.jporm.annotation.Id;
import com.jporm.annotation.generator.GeneratorType;
import com.jporm.util.ValueGenerator;

public class BeanWithCustom {
    
    @Id
    @Generator(generatorType = GeneratorType.CUSTOM, valueGeneratorClass = CustomGenerator.class)
    long custom;
    
    static class CustomGenerator implements ValueGenerator<String> {
        
        public static int COUNT = 0;
        
        @Override
        public String next() {
            return "customValueFromCustomGenerator-" + COUNT++;
        }
    }
    
}