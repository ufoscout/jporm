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
package com.jporm.core.dialect;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.jporm.core.util.StringUtil;

/**
 * Return the type of underlying DB
 * @author ufo
 *
 */
public class DetermineDBType {

    private final Map<String, DBType> dbProductNameMap = new HashMap<String, DBType>();

    public DetermineDBType() {

        dbProductNameMap.put("Derby", DBType.DERBY); //$NON-NLS-1$

        dbProductNameMap.put("H2", DBType.H2); //$NON-NLS-1$

        dbProductNameMap.put("HSQL", DBType.HSQLDB); //$NON-NLS-1$

        dbProductNameMap.put("Mysql", DBType.MYSQL); //$NON-NLS-1$

        dbProductNameMap.put("Oracle", DBType.ORACLE); //$NON-NLS-1$

        dbProductNameMap.put("Postgresql", DBType.POSTGRESQL); //$NON-NLS-1$
    }

    public DBType determineDBType(final String driverName, final String URL, final String databaseProductName) {
        for (Entry<String, DBType> entry : dbProductNameMap.entrySet()) {
            if ((databaseProductName!=null) && StringUtil.containsIgnoreCase(databaseProductName, entry.getKey())) {
                return entry.getValue();
            }
        }
        return DBType.UNKNOWN;
    }

}
