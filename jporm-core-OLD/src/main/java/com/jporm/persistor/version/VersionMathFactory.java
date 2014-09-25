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
package com.jporm.persistor.version;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import com.jporm.exception.OrmConfigurationException;
import com.jporm.util.MapUtil;

/**
 * 
 * @author ufo
 *
 */
public class VersionMathFactory  {

    private Map<Class<?>, VersionMath<?>> maths = new HashMap<Class<?>, VersionMath<?>>();

    public VersionMathFactory() {
        this.addToMap(Integer.TYPE, new IntegerVersionMath());
        this.addToMap(Integer.class, new IntegerVersionMath());
        this.addToMap(Long.TYPE, new LongVersionMath());
        this.addToMap(Long.class, new LongVersionMath());
        this.addToMap(BigDecimal.class, new BigDecimalVersionMath());
    }

    private <K> void addToMap(final Class<K> clazz, final VersionMath<K> math) {
        maths.put(clazz, math);
    }

    public <T> VersionMath<T> getMath(final Class<T> clazz, final boolean isVersionField) {
        if (!isVersionField) {
            return new NullVersionMath<T>();
        }
        if (maths.containsKey(clazz)) {
            return (VersionMath<T>) maths.get(clazz);
        }
        throw new OrmConfigurationException("Cannot manipulate version for type [" + clazz + "]. Allowed types [" + MapUtil.keysToString(maths) + "]."); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
    }

}
