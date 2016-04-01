/*******************************************************************************
 * Copyright 2013 Francesco Cina' Licensed under the Apache License, Version 2.0 (the "License"); you may not use this
 * file except in compliance with the License. You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0 Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific language governing permissions and limitations under the
 * License.
 ******************************************************************************/
package com.jporm.commons.core.io;

import java.util.function.Function;

import com.jporm.types.io.ResultSet;

/**
 * @author ufo
 */
public class StringResultSetReader implements Function<ResultSet, String> {

    @Override
    public String apply(final ResultSet resultSet) {
        if (resultSet.next()) {
            String result = resultSet.getString(0);
            return result;
        }
        return null;
    }

}
