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
package com.jporm.sql.query.where;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.jporm.sql.BaseSqlTestApi;
import com.jporm.sql.dialect.h2.H2DBProfile;
import com.jporm.sql.query.processor.NoOpsStringPropertiesProcessor;
import com.jporm.sql.query.select.where.SelectWhereImpl;

/**
 *
 * @author Francesco Cina
 *
 *         23/giu/2011
 */
public class ExpressionTest extends BaseSqlTestApi {

    @Test
    public void testExpression1() {
        final SelectWhereImpl expression = new SelectWhereImpl(dsl().selectAll().from(""));

        expression.eq("eqKey", "eqValue");
        expression.ge("ge1Key", "ge1Value");
        expression.in("inKey", new Object[] { "valueIn1", 2, "valueIn3" });

        StringBuilder queryElement = new StringBuilder();
        new H2DBProfile().getSqlRender().getSelectRender().getWhereRender().render(expression, queryElement, new NoOpsStringPropertiesProcessor());

        assertEquals("WHERE eqKey = ? AND ge1Key >= ? AND inKey in ( ?, ?, ? ) ", queryElement.toString());

        final List<Object> valuesList = new ArrayList<Object>();
        expression.sqlElementValues(valuesList);
        System.out.println("valuesList: " + valuesList);
        assertEquals(5, valuesList.size());
        assertEquals("eqValue", valuesList.get(0));
        assertEquals("ge1Value", valuesList.get(1));
        assertEquals("valueIn1", valuesList.get(2));
        assertEquals(Integer.valueOf(2), valuesList.get(3));
        assertEquals("valueIn3", valuesList.get(4));
    }

    @Test
    public void testExpression2() {
        final SelectWhereImpl expression = new SelectWhereImpl(dsl().selectAll().from(""));

        expression.eq("eqKey", "eqValue");
        expression.ge("ge1Key", "ge1Value");
        expression.in("inKey", new Object[] { "valueIn1", 2, "valueIn3" });

        expression.or(Exp.in("prop1", new Object[] { "hello1", "hello2", "hello3", "hello4" })
                        .nin("prop2", new Object[] { "hello5", "hello6", "hello7", "hello8" }));

        StringBuilder queryElement = new StringBuilder();
        new H2DBProfile().getSqlRender().getSelectRender().getWhereRender().render(expression, queryElement, new NoOpsStringPropertiesProcessor());

        assertEquals("WHERE eqKey = ? AND ge1Key >= ? AND inKey in ( ?, ?, ? ) OR ( prop1 in ( ?, ?, ?, ? ) AND prop2 not in ( ?, ?, ?, ? ) ) ",
        		queryElement.toString());

        final List<Object> valuesList = new ArrayList<Object>();
        expression.sqlElementValues(valuesList);
        System.out.println("valuesList: " + valuesList);
        assertEquals(13, valuesList.size());
        int i = 0;
        assertEquals("eqValue", valuesList.get(i++));
        assertEquals("ge1Value", valuesList.get(i++));
        assertEquals("valueIn1", valuesList.get(i++));
        assertEquals(Integer.valueOf(2), valuesList.get(i++));
        assertEquals("valueIn3", valuesList.get(i++));
        assertEquals("hello1", valuesList.get(i++));
        assertEquals("hello2", valuesList.get(i++));
        assertEquals("hello3", valuesList.get(i++));
        assertEquals("hello4", valuesList.get(i++));
        assertEquals("hello5", valuesList.get(i++));
        assertEquals("hello6", valuesList.get(i++));
        assertEquals("hello7", valuesList.get(i++));
        assertEquals("hello8", valuesList.get(i++));

    }

}
