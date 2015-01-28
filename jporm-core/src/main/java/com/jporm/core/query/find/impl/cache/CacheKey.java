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
package com.jporm.core.query.find.impl.cache;

import java.util.List;

/**
 * 
 * @author ufo
 *
 */
public class CacheKey {

    private final String sql;
    private final List<Object> args;
    private final List<String> ignoredFields;

    public CacheKey(final String sql, final List<Object> args, final List<String> ignoredFields) {
        this.sql = sql;
        this.args = args;
        this.ignoredFields = ignoredFields;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = (prime * result) + ((args == null) ? 0 : args.hashCode());
        result = (prime * result)
                + ((ignoredFields == null) ? 0 : ignoredFields.hashCode());
        result = (prime * result) + ((sql == null) ? 0 : sql.hashCode());
        return result;
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        CacheKey other = (CacheKey) obj;
        if (args == null) {
            if (other.args != null) {
                return false;
            }
        } else if (!args.equals(other.args)) {
            return false;
        }
        if (ignoredFields == null) {
            if (other.ignoredFields != null) {
                return false;
            }
        } else if (!ignoredFields.equals(other.ignoredFields)) {
            return false;
        }
        if (sql == null) {
            if (other.sql != null) {
                return false;
            }
        } else if (!sql.equals(other.sql)) {
            return false;
        }
        return true;
    }

}
