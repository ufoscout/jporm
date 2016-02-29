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
package com.jporm.sql.query.select.from;

import com.jporm.sql.query.SqlSubElement;

/**
 *
 * @author Francesco Cina
 *
 *         18/giu/2011
 */
public interface From<JOIN, FROM extends From<JOIN, FROM>> extends SqlSubElement {

    /**
     * Perform a natural full outer Join. The name of the class will be used as
     * alias.
     *
     * @return
     */
    FROM fullOuterJoin(JOIN joinTable);

    /**
     * Perform a natural full outer Join.
     *
     * @return
     */
    FROM fullOuterJoin(JOIN joinTable, String joinTableAlias);

    /**
     * Perform a full outer Join.
     *
     * @return
     */
    FROM fullOuterJoin(JOIN joinTable, String onLeftProperty, String onRigthProperty);

    /**
     * Perform full outer Join.
     *
     * @return
     */
    FROM fullOuterJoin(JOIN joinTable, String joinTableAlias, String onLeftProperty, String onRigthProperty);

    /**
     * Perform a inner Join. An inner join can be performed in a normal sql
     * query simply using the key JOIN.
     *
     * @return
     */
    FROM innerJoin(JOIN joinTable);

    /**
     * Perform a inner Join. An inner join can be performed in a normal sql
     * simply using the key JOIN.
     *
     * @return
     */
    FROM innerJoin(JOIN joinTable, String joinTableAlias);

    /**
     * Perform a inner Join. An inner join can be performed in a normal sql
     * query simply using the key JOIN.
     *
     * @return
     */
    FROM innerJoin(JOIN joinTable, String onLeftProperty, String onRigthProperty);

    /**
     * Perform a inner Join. An inner join can be performed in a normal sql
     * simply using the key JOIN.
     *
     * @return
     */
    FROM innerJoin(JOIN joinTable, String joinTableAlias, String onLeftProperty, String onRigthProperty);

    /**
     * Perform a simple (cross) Join. This join returns the Cartesian product of
     * rows from tables in the join. A cross join is the join commonly used when
     * more tables are comma separated in a from clause. The name of the class
     * will be used as alias.
     *
     * @return
     */
    FROM join(JOIN joinTable);

    /**
     * Perform a simple (cross) Join. This join returns the Cartesian product of
     * rows from tables in the join. A cross join is the join commonly used when
     * more tables are comma separated in a from clause.
     *
     * @return
     */
    FROM join(JOIN joinTable, String joinTableAlias);

    /**
     * Perform a natural left outer Join. The name of the class will be used as
     * alias.
     *
     * @return
     */
    FROM leftOuterJoin(JOIN joinTable);

    /**
     * Perform a natural left outer Join.
     *
     * @return
     */
    FROM leftOuterJoin(JOIN joinTable, String joinTableAlias);

    /**
     * Perform a left outer Join.
     *
     * @return
     */
    FROM leftOuterJoin(JOIN joinTable, String onLeftProperty, String onRigthProperty);

    /**
     * Perform left outer Join.
     *
     * @return
     */
    FROM leftOuterJoin(JOIN joinTable, String joinTableAlias, String onLeftProperty, String onRigthProperty);

    /**
     * Perform a natural Join. The join predicate arises implicitly by comparing
     * all columns in both tables that have the same column-names in the joined
     * tables. The resulting joined table contains only one column for each pair
     * of equally-named columns. The name of the class will be used as alias.
     *
     * @return
     */
    FROM naturalJoin(JOIN joinTable);

    /**
     * Perform a natural Join. The join predicate arises implicitly by comparing
     * all columns in both tables that have the same column-names in the joined
     * tables. The resulting joined table contains only one column for each pair
     * of equally-named columns..
     *
     * @return
     */
    FROM naturalJoin(JOIN joinTable, String joinTableAlias);

    /**
     * Perform a natural right outer Join. The name of the class will be used as
     * alias.
     *
     * @return
     */
    FROM rightOuterJoin(JOIN joinTable);

    /**
     * Perform a natural right outer Join.
     *
     * @return
     */
    FROM rightOuterJoin(JOIN joinTable, String joinTableAlias);

    /**
     * Perform a right outer Join.
     *
     * @return
     */
    FROM rightOuterJoin(JOIN joinTable, String onLeftProperty, String onRigthProperty);

    /**
     * Perform right outer Join.
     *
     * @return
     */
    FROM rightOuterJoin(JOIN joinTable, String joinTableAlias, String onLeftProperty, String onRigthProperty);

}
