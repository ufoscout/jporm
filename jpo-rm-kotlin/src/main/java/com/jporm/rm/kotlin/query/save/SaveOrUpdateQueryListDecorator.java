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
package com.jporm.rm.kotlin.query.save;

import java.util.ArrayList;
import java.util.List;

public class SaveOrUpdateQueryListDecorator<BEAN> implements SaveOrUpdateQuery<BEAN> {

    private final List<SaveOrUpdateQuery<BEAN>> queries = new ArrayList<>();

    public void add(final SaveOrUpdateQuery<BEAN> query) {
        queries.add(query);
    }

    @Override
    public List<BEAN> execute() {
        List<BEAN> stream = new ArrayList<>();
        for (SaveOrUpdateQuery<BEAN> updateQuery : queries) {
            stream.addAll(updateQuery.execute());
        }
        return stream;
    }

    public List<SaveOrUpdateQuery<BEAN>> getQueries() {
        return queries;
    }

}
