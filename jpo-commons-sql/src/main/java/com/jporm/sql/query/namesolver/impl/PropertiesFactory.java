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
package com.jporm.sql.query.namesolver.impl;

import java.util.Map;

import com.jporm.annotation.exception.JpoWrongPropertyNameException;
import com.jporm.sql.util.MaxSizeMap;

/**
 * <class_description>
 * <p>
 * <b>notes</b>:
 * <p>
 * ON : Mar 9, 2013
 *
 * @author - Francesco Cina
 * @version $Revision
 */
public class PropertiesFactory {

    private final Map<String, Property> PROPERTIES = new MaxSizeMap<>(1000);

    private String alias(final String property) {
        if (property.contains(".")) { //$NON-NLS-1$
            return property.substring(0, property.lastIndexOf(".")); //$NON-NLS-1$
        }
        return null;
    }

    private String field(final String property) {
        try {
            return property.substring(property.lastIndexOf(".") + 1); //$NON-NLS-1$
        } catch (final Exception e) {
            throw new JpoWrongPropertyNameException(
                    "Error parsing property [" + property + "], the format must be CLASS_NAME.CLASS_FIELD or CLASS_ALIAS.CLASS_FIELD"); //$NON-NLS-1$ //$NON-NLS-2$
        }
    }

    public Property property(final String propertyValue) {
        Property property = PROPERTIES.get(propertyValue);
        if (property == null) {
            property = new Property(alias(propertyValue), field(propertyValue));
            PROPERTIES.put(propertyValue, property);
        }
        return property;
    }

}
