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
package com.jporm.query.clause.where;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.jporm.BaseTestApi;
import com.jporm.query.clause.WhereExpressionElement;
import com.jporm.query.clause.WhereImpl;
import com.jporm.query.clause.where.InExpressionElement;
import com.jporm.query.clause.where.NInExpressionElement;
import com.jporm.query.namesolver.NullNameSolver;

/**
 * 
 * @author Francesco Cina
 *
 * 23/giu/2011
 */
public class ExpressionTest extends BaseTestApi {

    @Test
    public void testExpression1() {
        final WhereImpl<WhereImpl<?>> expression = new WhereImpl<WhereImpl<?>>() {
            @Override
            public WhereImpl<?> where() {
                return this;
            }
        };

        expression.eq("eqKey", "eqValue"); //$NON-NLS-1$ //$NON-NLS-2$
        expression.ge("ge1Key", "ge1Value"); //$NON-NLS-1$ //$NON-NLS-2$
        expression.in("inKey", new Object[]{"valueIn1", 2, "valueIn3"}); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

        System.out.println(expression.renderSqlElement(new NullNameSolver()));

        assertEquals("WHERE eqKey = ? AND ge1Key >= ? AND inKey in ( ?, ?, ? ) " , expression.renderSqlElement(new NullNameSolver())); //$NON-NLS-1$

        final List<Object> valuesList = new ArrayList<Object>();
        expression.appendElementValues(valuesList);
        System.out.println("valuesList: " + valuesList); //$NON-NLS-1$
        assertEquals(5, valuesList.size());
        assertEquals("eqValue", valuesList.get(0)); //$NON-NLS-1$
        assertEquals("ge1Value", valuesList.get(1)); //$NON-NLS-1$
        assertEquals("valueIn1", valuesList.get(2)); //$NON-NLS-1$
        assertEquals( Integer.valueOf(2), valuesList.get(3));
        assertEquals("valueIn3", valuesList.get(4)); //$NON-NLS-1$
    }

    @Test
    public void testExpression2() {
        final WhereImpl<WhereImpl<?>> expression = new WhereImpl<WhereImpl<?>>() {
            @Override
            public WhereImpl<?> where() {
                return this;
            }
        };

        expression.eq("eqKey", "eqValue"); //$NON-NLS-1$ //$NON-NLS-2$
        expression.ge("ge1Key", "ge1Value"); //$NON-NLS-1$ //$NON-NLS-2$
        expression.in("inKey", new Object[]{"valueIn1", 2, "valueIn3"}); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

        final WhereExpressionElement expressionOne = new InExpressionElement( "prop1", new Object[]{"hello1","hello2","hello3","hello4"}); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$
        final WhereExpressionElement expressionTwo = new NInExpressionElement( "prop2", new Object[]{"hello5","hello6","hello7","hello8"} ); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$

        expression.or(expressionOne, expressionTwo);

        System.out.println(expression.renderSqlElement(new NullNameSolver()));

        assertEquals("WHERE eqKey = ? AND ge1Key >= ? AND inKey in ( ?, ?, ? ) AND ( prop1 in ( ?, ?, ?, ? ) OR prop2 not in ( ?, ?, ?, ? ) ) " , expression.renderSqlElement(new NullNameSolver())); //$NON-NLS-1$

        final List<Object> valuesList = new ArrayList<Object>();
        expression.appendElementValues(valuesList);
        System.out.println("valuesList: " + valuesList); //$NON-NLS-1$
        assertEquals(13, valuesList.size());
        int i=0;
        assertEquals("eqValue", valuesList.get(i++)); //$NON-NLS-1$
        assertEquals("ge1Value", valuesList.get(i++)); //$NON-NLS-1$
        assertEquals("valueIn1", valuesList.get(i++)); //$NON-NLS-1$
        assertEquals( Integer.valueOf(2), valuesList.get(i++));
        assertEquals("valueIn3", valuesList.get(i++)); //$NON-NLS-1$
        assertEquals("hello1", valuesList.get(i++)); //$NON-NLS-1$
        assertEquals("hello2", valuesList.get(i++)); //$NON-NLS-1$
        assertEquals("hello3", valuesList.get(i++)); //$NON-NLS-1$
        assertEquals("hello4", valuesList.get(i++)); //$NON-NLS-1$
        assertEquals("hello5", valuesList.get(i++)); //$NON-NLS-1$
        assertEquals("hello6", valuesList.get(i++)); //$NON-NLS-1$
        assertEquals("hello7", valuesList.get(i++)); //$NON-NLS-1$
        assertEquals("hello8", valuesList.get(i++)); //$NON-NLS-1$

    }

}
