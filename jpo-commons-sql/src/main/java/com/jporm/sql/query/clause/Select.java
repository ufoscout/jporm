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
package com.jporm.sql.query.clause;

import java.util.List;

import com.jporm.annotation.LockMode;
import com.jporm.sql.dialect.DBProfile;



/**
 *
 * @author Francesco Cina
 *
 * 07/lug/2011
 */
public interface Select extends SelectCommon {

	Select distinct(boolean distinct);

	Select selectFields(String... selectFields);

	From from();

	Where where();

	OrderBy orderBy();

	GroupBy groupBy();

	void lockMode(LockMode lockMode);

	String renderRowCountSql(DBProfile dbProfile);

	Where where(List<WhereExpressionElement> expressionElements);

	Where where(String customClause, Object... args);

	Where where(WhereExpressionElement... expressionElements);

	Select firstRow(int firstRow);

	Select maxRows(int maxRows);
}
