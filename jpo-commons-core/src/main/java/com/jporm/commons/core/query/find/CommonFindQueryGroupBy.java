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
package com.jporm.commons.core.query.find;

import java.util.List;

import com.jporm.commons.core.exception.JpoException;
import com.jporm.commons.core.query.clause.GroupBy;
import com.jporm.sql.query.clause.WhereExpressionElement;
import com.jporm.sql.query.clause.impl.where.Exp;

/**
 * <class_description>
 * <p><b>notes</b>:
 * <p>ON : Mar 23, 2013
 *
 * @author Francesco Cina'
 * @version $Revision
 */
public interface CommonFindQueryGroupBy <FIND extends CommonFindQuery<FIND, WHERE, ORDER_BY>,
										WHERE extends CommonFindQueryWhere<FIND, WHERE, ORDER_BY>,
										ORDER_BY extends CommonFindQueryOrderBy<FIND, WHERE, ORDER_BY>,
										GROUP_BY extends CommonFindQueryGroupBy<FIND, WHERE, ORDER_BY, GROUP_BY>>
								extends CommonFindQueryBase<FIND,WHERE,ORDER_BY>, GroupBy<GROUP_BY> {

	/**
	 * Chain more {@link WhereExpressionElement} with a logical and.
	 * To build the {@link WhereExpressionElement} use the {@link Exp} factory.
	 *
	 * @return
	 */
	WHERE where(final WhereExpressionElement... expressionElements);

	/**
	 * Chain more {@link WhereExpressionElement} with a logical and.
	 * To build the {@link WhereExpressionElement} use the {@link Exp} factory.
	 *
	 * @return
	 */
	WHERE where(final List<WhereExpressionElement> expressionElements);

	/**
	 * It permits to define a custom where clause.
	 * E.g.: clause("mod(Bean.id, 10) = 1 AND Bean.property is not null")
	 *
	 * For a better readability and usability placeholders can be used:
	 * E.g.: clause("mod(Bean.id, ?) = ? AND Bean.property is not null", new Object[]{10,1})
	 *
	 * @param customClause the custom where clause
	 * @param args the values of the placeholders if present
	 * To build the {@link WhereExpressionElement} use the {@link Exp} factory.
	 *
	 * @return
	 */
	WHERE where(String customClause, Object... args);

	/**
	 * Set the order by clause.
	 * @return
	 */
	ORDER_BY orderBy() throws JpoException;

	FIND query();

}
