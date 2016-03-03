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
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import com.jporm.sql.BaseSqlTestApi;
import com.jporm.sql.dialect.h2.H2DBProfile;
import com.jporm.sql.query.processor.NoOpsStringPropertiesProcessor;
import com.jporm.sql.query.where.WhereExpressionElement;
import com.jporm.sql.query.where.expression.AndExpressionElement;
import com.jporm.sql.query.where.expression.EqExpressionElement;
import com.jporm.sql.query.where.expression.Exp;
import com.jporm.sql.query.where.expression.GtPropertiesExpressionElement;
import com.jporm.sql.query.where.expression.IEqExpressionElement;
import com.jporm.sql.query.where.expression.InExpressionElement;
import com.jporm.sql.query.where.expression.IsNotNullExpressionElement;
import com.jporm.sql.query.where.expression.IsNullExpressionElement;
import com.jporm.sql.query.where.expression.LeExpressionElement;
import com.jporm.sql.query.where.expression.NInExpressionElement;
import com.jporm.sql.query.where.expression.NePropertiesExpressionElement;
import com.jporm.sql.query.where.expression.NotExpressionElement;
import com.jporm.sql.query.where.expression.OrExpressionElement;

/**
 *
 * @author Francesco Cina
 *
 *         19/giu/2011
 */
public class ExpressionElementsTest extends BaseSqlTestApi {

    @Test
    public void testAnd_Empty() {
        final WhereExpressionElement andExpression = new AndExpressionElement(new WhereExpressionElement[] {});
        StringBuilder queryElement = new StringBuilder();
        andExpression.sqlElementQuery(queryElement, new H2DBProfile(), new NoOpsStringPropertiesProcessor());
        assertEquals("( 1=1 ) ", queryElement.toString()); //$NON-NLS-1$
    }

    @Test
    public void testAnd_OneElement() {
        final WhereExpressionElement expression = new IsNullExpressionElement("hello"); //$NON-NLS-1$

        final WhereExpressionElement andExpression = new AndExpressionElement(new WhereExpressionElement[] { expression });

        StringBuilder queryElement = new StringBuilder();
        andExpression.sqlElementQuery(queryElement, new H2DBProfile(), new NoOpsStringPropertiesProcessor());
        assertEquals("( hello IS NULL ) ", queryElement.toString()); //$NON-NLS-1$
    }

    @Test
    public void testAnd_ThreeElements() {
        final String property = "goodbye"; //$NON-NLS-1$
        final Object[] values = new Object[] { "hello1", "hello2", "hello3", "hello4" }; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$

        final WhereExpressionElement andExpression = Exp.and(Exp.in(property, Arrays.asList(values)), Exp.isNull("hello"), Exp.isNotNull("notHello")); //$NON-NLS-1$ //$NON-NLS-2$

        StringBuilder queryElement = new StringBuilder();
        andExpression.sqlElementQuery(queryElement, new H2DBProfile(), new NoOpsStringPropertiesProcessor());
        assertEquals("( goodbye in ( ?, ?, ?, ? ) AND hello IS NULL AND notHello IS NOT NULL ) ", queryElement.toString());

    }

    @Test
    public void testAnd_TwoElements() {
        final String property = "goodbye"; //$NON-NLS-1$
        final Object[] values = new Object[] { "hello1", "hello2", "hello3", "hello4" }; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
        final WhereExpressionElement expElemOne = new InExpressionElement(property, Arrays.asList(values));
        final WhereExpressionElement expElemTwo = new IsNullExpressionElement("hello"); //$NON-NLS-1$

        final WhereExpressionElement andExpression = new AndExpressionElement(new WhereExpressionElement[] { expElemOne, expElemTwo });

        StringBuilder queryElement = new StringBuilder();
        andExpression.sqlElementQuery(queryElement, new H2DBProfile(), new NoOpsStringPropertiesProcessor());
        assertEquals("( goodbye in ( ?, ?, ?, ? ) AND hello IS NULL ) ", queryElement.toString()); //$NON-NLS-1$

        final List<Object> valuesList = new ArrayList<Object>();
        andExpression.sqlElementValues(valuesList);
        System.out.println("valuesList: " + valuesList); //$NON-NLS-1$
        assertEquals(4, valuesList.size());
        assertEquals("hello1", valuesList.get(0)); //$NON-NLS-1$
        assertEquals("hello2", valuesList.get(1)); //$NON-NLS-1$
        assertEquals("hello3", valuesList.get(2)); //$NON-NLS-1$
        assertEquals("hello4", valuesList.get(3)); //$NON-NLS-1$
    }

    @Test
    public void testEq() {
        final String property = "goodbye"; //$NON-NLS-1$
        final Object value = "hello"; //$NON-NLS-1$
        final WhereExpressionElement expElemFirst = new EqExpressionElement(property, value);
        
        StringBuilder queryElement = new StringBuilder();
        expElemFirst.sqlElementQuery(queryElement, new H2DBProfile(), new NoOpsStringPropertiesProcessor());
        
        assertEquals("goodbye = ? ", queryElement.toString()); //$NON-NLS-1$
    }

    @Test
    public void testIEq() {
        final String property = "goodbye"; //$NON-NLS-1$
        final Object value = "hello"; //$NON-NLS-1$
        final WhereExpressionElement expElemFirst = new IEqExpressionElement(property, value);
                StringBuilder queryElement = new StringBuilder();
        expElemFirst.sqlElementQuery(queryElement, new H2DBProfile(), new NoOpsStringPropertiesProcessor());
        assertEquals("LOWER(goodbye) = LOWER(?) ", queryElement.toString()); //$NON-NLS-1$
    }

    @Test
    public void testIn() {
        final String property = "goodbye"; //$NON-NLS-1$
        final Object[] values = new Object[] { "hello0", "hello1", "hello2", "hello3" }; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
        final WhereExpressionElement expElemFirst = new InExpressionElement(property, values);
                StringBuilder queryElement = new StringBuilder();
        expElemFirst.sqlElementQuery(queryElement, new H2DBProfile(), new NoOpsStringPropertiesProcessor());
        assertEquals("goodbye in ( ?, ?, ?, ? ) ", queryElement.toString()); //$NON-NLS-1$

        final List<Object> valuesList = new ArrayList<Object>();
        expElemFirst.sqlElementValues(valuesList);
        System.out.println("valuesList: " + valuesList); //$NON-NLS-1$
        assertEquals(4, valuesList.size());
        assertEquals("hello0", valuesList.get(0)); //$NON-NLS-1$
        assertEquals("hello1", valuesList.get(1)); //$NON-NLS-1$
        assertEquals("hello2", valuesList.get(2)); //$NON-NLS-1$
        assertEquals("hello3", valuesList.get(3)); //$NON-NLS-1$

    }

    @Test
    public void testIsNotNull() {
        final String property = "goodbye"; //$NON-NLS-1$
        final WhereExpressionElement expElemFirst = new IsNotNullExpressionElement(property);
                StringBuilder queryElement = new StringBuilder();
        expElemFirst.sqlElementQuery(queryElement, new H2DBProfile(), new NoOpsStringPropertiesProcessor());
        assertEquals("goodbye IS NOT NULL ", queryElement.toString()); //$NON-NLS-1$

        final List<Object> valuesList = new ArrayList<Object>();
        expElemFirst.sqlElementValues(valuesList);
        System.out.println("valuesList: " + valuesList); //$NON-NLS-1$
        assertEquals(0, valuesList.size());
    }

    @Test
    public void testIsNull() {
        final String property = "goodbye"; //$NON-NLS-1$
        final WhereExpressionElement expElemFirst = new IsNullExpressionElement(property);
                StringBuilder queryElement = new StringBuilder();
        expElemFirst.sqlElementQuery(queryElement, new H2DBProfile(), new NoOpsStringPropertiesProcessor());
        assertEquals("goodbye IS NULL ", queryElement.toString()); //$NON-NLS-1$
    }

    @Test
    public void testLe() {
        final String property = "goodbye"; //$NON-NLS-1$
        final Object value = "hello"; //$NON-NLS-1$
        final WhereExpressionElement expElemFirst = new LeExpressionElement(property, value);
         
        StringBuilder queryElement = new StringBuilder();
        expElemFirst.sqlElementQuery(queryElement, new H2DBProfile(), new NoOpsStringPropertiesProcessor());
        assertEquals("goodbye <= ? ", queryElement.toString()); //$NON-NLS-1$

        final List<Object> valuesList = new ArrayList<Object>();
        expElemFirst.sqlElementValues(valuesList);
        System.out.println("valuesList: " + valuesList); //$NON-NLS-1$
        assertEquals(1, valuesList.size());
        assertEquals(value, valuesList.get(0));
    }

    @Test
    public void testNot() {
        final String property = "goodbye"; //$NON-NLS-1$
        final Object[] values = new Object[] { "hello1", "hello2", "hello3", "hello4" }; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
        final WhereExpressionElement expElemOne = new InExpressionElement(property, Arrays.asList(values));

        final WhereExpressionElement notExpression = new NotExpressionElement(expElemOne);

        StringBuilder queryElement = new StringBuilder();
        notExpression.sqlElementQuery(queryElement, new H2DBProfile(), new NoOpsStringPropertiesProcessor());
        assertEquals("NOT ( goodbye in ( ?, ?, ?, ? ) ) ", queryElement.toString()); //$NON-NLS-1$

        final List<Object> valuesList = new ArrayList<Object>();
        notExpression.sqlElementValues(valuesList);
        System.out.println("valuesList: " + valuesList); //$NON-NLS-1$
        assertEquals(4, valuesList.size());
        assertEquals("hello1", valuesList.get(0)); //$NON-NLS-1$
        assertEquals("hello2", valuesList.get(1)); //$NON-NLS-1$
        assertEquals("hello3", valuesList.get(2)); //$NON-NLS-1$
        assertEquals("hello4", valuesList.get(3)); //$NON-NLS-1$
    }

    @Test
    public void testOr_Empty() {
        final WhereExpressionElement orExpression = new OrExpressionElement(new WhereExpressionElement[] {});

        StringBuilder queryElement = new StringBuilder();
        orExpression.sqlElementQuery(queryElement, new H2DBProfile(), new NoOpsStringPropertiesProcessor());
        assertEquals("( 1=1 ) ", queryElement.toString()); //$NON-NLS-1$
    }

    @Test
    public void testOr_OneElement() {
        final WhereExpressionElement expression = new IsNullExpressionElement("hello"); //$NON-NLS-1$

        final WhereExpressionElement orExpression = new OrExpressionElement(new WhereExpressionElement[] { expression });

        StringBuilder queryElement = new StringBuilder();
        orExpression.sqlElementQuery(queryElement, new H2DBProfile(), new NoOpsStringPropertiesProcessor());
        assertEquals("( hello IS NULL ) ", queryElement.toString()); //$NON-NLS-1$
    }

    @Test
    public void testOr_ThreeElements() {
        final String property = "goodbye"; //$NON-NLS-1$
        final Object[] values = new Object[] { "hello1", "hello2", "hello3", "hello4" }; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$

        final WhereExpressionElement orExpression = Exp.or(Exp.in(property, Arrays.asList(values)), Exp.isNull("hello"), Exp.isNotNull("notHello")); //$NON-NLS-1$ //$NON-NLS-2$

        StringBuilder queryElement = new StringBuilder();
        orExpression.sqlElementQuery(queryElement, new H2DBProfile(), new NoOpsStringPropertiesProcessor());
        assertEquals("( goodbye in ( ?, ?, ?, ? ) OR hello IS NULL OR notHello IS NOT NULL ) ", //$NON-NLS-1$
        		queryElement.toString());

    }

    @Test
    public void testOr_TwoElements() {
        final String property = "goodbye"; //$NON-NLS-1$
        final Object[] values = new Object[] { "hello1", "hello2", "hello3", "hello4" }; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
        final WhereExpressionElement expElemOne = new InExpressionElement(property, values);
        final WhereExpressionElement expElemTwo = new NInExpressionElement(property, new Object[] { "hello5", "hello6", "hello7", "hello8" }); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$

        final WhereExpressionElement orExpression = new OrExpressionElement(new WhereExpressionElement[] { expElemOne, expElemTwo });

        StringBuilder queryElement = new StringBuilder();
        orExpression.sqlElementQuery(queryElement, new H2DBProfile(), new NoOpsStringPropertiesProcessor());
        assertEquals("( goodbye in ( ?, ?, ?, ? ) OR goodbye not in ( ?, ?, ?, ? ) ) ", queryElement.toString()); //$NON-NLS-1$

        final List<Object> valuesList = new ArrayList<Object>();
        orExpression.sqlElementValues(valuesList);
        System.out.println("valuesList: " + valuesList); //$NON-NLS-1$
        assertEquals(8, valuesList.size());
        assertEquals("hello1", valuesList.get(0)); //$NON-NLS-1$
        assertEquals("hello2", valuesList.get(1)); //$NON-NLS-1$
        assertEquals("hello3", valuesList.get(2)); //$NON-NLS-1$
        assertEquals("hello4", valuesList.get(3)); //$NON-NLS-1$
        assertEquals("hello5", valuesList.get(4)); //$NON-NLS-1$
        assertEquals("hello6", valuesList.get(5)); //$NON-NLS-1$
        assertEquals("hello7", valuesList.get(6)); //$NON-NLS-1$
        assertEquals("hello8", valuesList.get(7)); //$NON-NLS-1$
    }

    @Test
    public void testProperties1() {
        final String firstProperty = "User.id"; //$NON-NLS-1$
        final String secondProperty = "Employee.id"; //$NON-NLS-1$
        final WhereExpressionElement expElemOne = new NePropertiesExpressionElement(firstProperty, secondProperty);

        StringBuilder queryElement = new StringBuilder();
        expElemOne.sqlElementQuery(queryElement, new H2DBProfile(), new NoOpsStringPropertiesProcessor());
        assertEquals("User.id != Employee.id ", queryElement.toString()); //$NON-NLS-1$

        final List<Object> valuesList = new ArrayList<Object>();
        expElemOne.sqlElementValues(valuesList);
        System.out.println("valuesList: " + valuesList); //$NON-NLS-1$
        assertEquals(0, valuesList.size());
    }

    @Test
    public void testProperties2() {
        final String firstProperty = "User.id"; //$NON-NLS-1$
        final String secondProperty = "Employee.id"; //$NON-NLS-1$
        final WhereExpressionElement expElemOne = new GtPropertiesExpressionElement(firstProperty, secondProperty);

        StringBuilder queryElement = new StringBuilder();
        expElemOne.sqlElementQuery(queryElement, new H2DBProfile(), new NoOpsStringPropertiesProcessor());
        assertEquals("User.id > Employee.id ", queryElement.toString()); //$NON-NLS-1$

        final List<Object> valuesList = new ArrayList<Object>();
        expElemOne.sqlElementValues(valuesList);
        System.out.println("valuesList: " + valuesList); //$NON-NLS-1$
        assertEquals(0, valuesList.size());
    }
}
