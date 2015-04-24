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
package com.jporm.rx.core.query.update.impl;

import com.jporm.commons.core.query.update.impl.CommonUpdateQueryWhereImpl;
import com.jporm.rx.core.connection.UpdateResult;
import com.jporm.rx.core.query.update.CustomUpdateQuery;
import com.jporm.rx.core.query.update.CustomUpdateQuerySet;
import com.jporm.rx.core.query.update.CustomUpdateQueryWhere;
import java.util.concurrent.CompletableFuture;

/**
 *
 * @author ufo
 *
 */
public class CustomUpdateQueryWhereImpl extends CommonUpdateQueryWhereImpl<CustomUpdateQuery, CustomUpdateQueryWhere, CustomUpdateQuerySet>  implements CustomUpdateQueryWhere {


	public CustomUpdateQueryWhereImpl(com.jporm.sql.query.clause.Where sqlWhere, final CustomUpdateQuery updateQuery) {
		super(sqlWhere, updateQuery);
	}

    @Override
    public CompletableFuture<UpdateResult> now() {
        return root().now(); //To change body of generated methods, choose Tools | Templates.
    }

}
