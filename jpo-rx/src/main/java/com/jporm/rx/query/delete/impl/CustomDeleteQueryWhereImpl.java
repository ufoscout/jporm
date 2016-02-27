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
package com.jporm.rx.query.delete.impl;

import java.util.concurrent.CompletableFuture;

import com.jporm.commons.core.query.delete.CommonDeleteQueryWhereImpl;
import com.jporm.rx.query.delete.CustomDeleteQuery;
import com.jporm.rx.query.delete.CustomDeleteQueryWhere;
import com.jporm.rx.query.delete.DeleteResult;

/**
 *
 * @author ufo
 *
 */
public class CustomDeleteQueryWhereImpl<BEAN> extends CommonDeleteQueryWhereImpl<CustomDeleteQuery<BEAN>, CustomDeleteQueryWhere<BEAN>>
        implements CustomDeleteQueryWhere<BEAN> {

    public CustomDeleteQueryWhereImpl(final Where sqlWhere, final CustomDeleteQuery<BEAN> deleteQuery) {
        super(sqlWhere, deleteQuery);
    }

    @Override
    public CompletableFuture<DeleteResult> execute() {
        return root().execute();
    }

}
