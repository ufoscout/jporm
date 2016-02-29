/*******************************************************************************
 * Copyright 2015 Francesco Cina'
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
package com.jporm.commons.core.query.strategy;

import java.util.List;

import com.jporm.sql.dialect.DBProfile;

public interface QueryExecutionStrategy {

    public static QueryExecutionStrategy build(final boolean returnsCountOfRowsInBatchUpdate) {
        if (returnsCountOfRowsInBatchUpdate) {
            return new QueryExecutionStrategyBatchUpdate();
        }
        return new QueryExecutionStrategySimpleUpdate();
    }

    public static QueryExecutionStrategy build(final DBProfile dbProfile) {
        return build(dbProfile.getDbFeatures().isReturnCountsOnBatchUpdate());
    }

    int executeDelete(DeleteExecutionStrategy strategy);

    <BEAN> List<BEAN> executeUpdate(UpdateExecutionStrategy<BEAN> strategy);
}
