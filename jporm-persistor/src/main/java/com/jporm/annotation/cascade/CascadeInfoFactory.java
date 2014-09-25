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
package com.jporm.annotation.cascade;

import java.lang.reflect.Field;

import com.jporm.annotation.Cascade;

/**
 * 
 * @author cinafr
 *
 */
public class CascadeInfoFactory {

    private CascadeInfoFactory() {}

    public static CascadeInfo getCascadeInfoInfo(final Field field) {
        Cascade cascade = field.getAnnotation(Cascade.class);
        if (cascade!=null) {
            return cascade.on().getInfo();
        }
        return CascadeType.ALWAYS.getInfo();
    }

}
