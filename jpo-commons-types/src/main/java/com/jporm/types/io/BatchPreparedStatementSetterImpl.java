/*******************************************************************************
 * Copyright 2016 Francesco Cina'
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
package com.jporm.types.io;

import java.util.function.BiConsumer;

public class BatchPreparedStatementSetterImpl implements BatchPreparedStatementSetter {

    private final int batchSize;
    private final BiConsumer<Statement, Integer> result;

    public BatchPreparedStatementSetterImpl(int batchSize, BiConsumer<Statement, Integer> result) {
        this.batchSize = batchSize;
        this.result = result;
    }

    @Override
    public int getBatchSize() {
        return batchSize;
    }

    @Override
    public void set(Statement ps, int i) {
        result.accept(ps, i);
    }

}
