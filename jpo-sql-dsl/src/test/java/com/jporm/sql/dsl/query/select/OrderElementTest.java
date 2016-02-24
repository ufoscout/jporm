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
package com.jporm.sql.dsl.query.select;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.jporm.sql.dsl.BaseSqlTestApi;
import com.jporm.sql.dsl.query.select.orderby.OrderByImpl;

/**
 *
 * @author Francesco Cina
 *
 *         24/giu/2011
 */
public class OrderElementTest extends BaseSqlTestApi {

    @Test
    public void testOrderBy0() {
        final OrderByImpl orderBy = new OrderByImpl(dsl().selectAll().from(""));
        System.out.println(orderBy.sqlElementQuery(getH2DDProfile()));
        assertEquals("", orderBy.sqlElementQuery(getH2DDProfile())); //$NON-NLS-1$
    }

    @Test
    public void testOrderBy1() {
        final OrderByImpl orderBy = new OrderByImpl(dsl().selectAll().from(""));
        orderBy.asc("helloAsc"); //$NON-NLS-1$
        System.out.println(orderBy.sqlElementQuery(getH2DDProfile()));
        assertEquals("ORDER BY helloAsc ASC ", orderBy.sqlElementQuery(getH2DDProfile())); //$NON-NLS-1$
    }

    @Test
    public void testOrderBy10() {
        final OrderByImpl orderBy = new OrderByImpl(dsl().selectAll().from(""));
        orderBy.asc("helloAsc1"); //$NON-NLS-1$
        orderBy.desc("helloDesc1"); //$NON-NLS-1$
        orderBy.desc("helloDesc2"); //$NON-NLS-1$
        orderBy.asc("helloAsc2"); //$NON-NLS-1$
        System.out.println(orderBy.sqlElementQuery(getH2DDProfile()));
        assertEquals("ORDER BY helloAsc1 ASC , helloDesc1 DESC , helloDesc2 DESC , helloAsc2 ASC ", //$NON-NLS-1$
                orderBy.sqlElementQuery(getH2DDProfile()));
    }

    @Test
    public void testOrderBy2() {
        final OrderByImpl orderBy = new OrderByImpl(dsl().selectAll().from(""));
        orderBy.desc("helloDesc"); //$NON-NLS-1$
        System.out.println(orderBy.sqlElementQuery(getH2DDProfile()));
        assertEquals("ORDER BY helloDesc DESC ", orderBy.sqlElementQuery(getH2DDProfile())); //$NON-NLS-1$
    }

    @Test
    public void testOrderBy3() {
        final OrderByImpl orderBy = new OrderByImpl(dsl().selectAll().from(""));
        orderBy.descNullsFirst("helloDesc"); //$NON-NLS-1$
        System.out.println(orderBy.sqlElementQuery(getH2DDProfile()));
        assertEquals("ORDER BY helloDesc DESC NULLS FIRST ", orderBy.sqlElementQuery(getH2DDProfile())); //$NON-NLS-1$
    }

    @Test
    public void testOrderBy4() {
        final OrderByImpl orderBy = new OrderByImpl(dsl().selectAll().from(""));
        orderBy.descNullsLast("helloDesc"); //$NON-NLS-1$
        System.out.println(orderBy.sqlElementQuery(getH2DDProfile()));
        assertEquals("ORDER BY helloDesc DESC NULLS LAST ", orderBy.sqlElementQuery(getH2DDProfile())); //$NON-NLS-1$
    }

    @Test
    public void testOrderBy5() {
        final OrderByImpl orderBy = new OrderByImpl(dsl().selectAll().from(""));
        orderBy.ascNullsFirst("helloDesc"); //$NON-NLS-1$
        System.out.println(orderBy.sqlElementQuery(getH2DDProfile()));
        assertEquals("ORDER BY helloDesc ASC NULLS FIRST ", orderBy.sqlElementQuery(getH2DDProfile())); //$NON-NLS-1$
    }

    @Test
    public void testOrderBy6() {
        final OrderByImpl orderBy = new OrderByImpl(dsl().selectAll().from(""));
        orderBy.ascNullsLast("helloDesc"); //$NON-NLS-1$
        System.out.println(orderBy.sqlElementQuery(getH2DDProfile()));
        assertEquals("ORDER BY helloDesc ASC NULLS LAST ", orderBy.sqlElementQuery(getH2DDProfile())); //$NON-NLS-1$
    }
}
