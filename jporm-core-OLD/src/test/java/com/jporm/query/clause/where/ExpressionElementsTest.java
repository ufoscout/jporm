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
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import com.jporm.BaseTestApi;
import com.jporm.query.clause.where.AndExpressionElement;
import com.jporm.query.clause.where.EqExpressionElement;
import com.jporm.query.clause.where.Exp;
import com.jporm.query.clause.where.ExpressionElement;
import com.jporm.query.clause.where.GtPropertiesExpressionElement;
import com.jporm.query.clause.where.IEqExpressionElement;
import com.jporm.query.clause.where.InExpressionElement;
import com.jporm.query.clause.where.IsNotNullExpressionElement;
import com.jporm.query.clause.where.IsNullExpressionElement;
import com.jporm.query.clause.where.LeExpressionElement;
import com.jporm.query.clause.where.NInExpressionElement;
import com.jporm.query.clause.where.NePropertiesExpressionElement;
import com.jporm.query.clause.where.NotExpressionElement;
import com.jporm.query.clause.where.OrExpressionElement;
import com.jporm.query.namesolver.NullNameSolver;

/**
 * 
 * @author Francesco Cina
 *
 * 19/giu/2011
 */
public class ExpressionElementsTest extends BaseTestApi {

    @Test
    public void testEq() {
        final String property = "goodbye"; //$NON-NLS-1$
        final Object value = "hello"; //$NON-NLS-1$
        final ExpressionElement expElemFirst = new EqExpressionElement(property, value);
        System.out.println("expElemFirst.render(false): " + expElemFirst.renderSqlElement(new NullNameSolver())); //$NON-NLS-1$
        assertEquals( "goodbye = ? " , expElemFirst.renderSqlElement(new NullNameSolver()) ); //$NON-NLS-1$
    }

    @Test
    public void testIEq() {
        final String property = "goodbye"; //$NON-NLS-1$
        final Object value = "hello"; //$NON-NLS-1$
        final ExpressionElement expElemFirst = new IEqExpressionElement(property, value);
        System.out.println("expElemFirst.render(): " + expElemFirst.renderSqlElement(new NullNameSolver())); //$NON-NLS-1$
        assertEquals( "LOWER(goodbye) = LOWER(?) " , expElemFirst.renderSqlElement(new NullNameSolver()) ); //$NON-NLS-1$
    }

    @Test
    public void testLe() {
        final String property = "goodbye"; //$NON-NLS-1$
        final Object value = "hello"; //$NON-NLS-1$
        final ExpressionElement expElemFirst = new LeExpressionElement(property, value);
        System.out.println("expElemFirst.render(): " + expElemFirst.renderSqlElement(new NullNameSolver())); //$NON-NLS-1$
        assertEquals( "goodbye <= ? " , expElemFirst.renderSqlElement(new NullNameSolver()) ); //$NON-NLS-1$

        final List<Object> valuesList = new ArrayList<Object>();
        expElemFirst.appendElementValues(valuesList);
        System.out.println("valuesList: " + valuesList); //$NON-NLS-1$
        assertEquals(1, valuesList.size());
        assertEquals(value, valuesList.get(0));
    }

    @Test
    public void testIn() {
        final String property = "goodbye"; //$NON-NLS-1$
        final Object[] values = new Object[]{"hello0","hello1","hello2","hello3"}; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
        final ExpressionElement expElemFirst = new InExpressionElement( property, values );
        System.out.println("expElemFirst.render(): " + expElemFirst.renderSqlElement(new NullNameSolver())); //$NON-NLS-1$
        assertEquals( "goodbye in ( ?, ?, ?, ? ) " , expElemFirst.renderSqlElement(new NullNameSolver()) ); //$NON-NLS-1$

        final List<Object> valuesList = new ArrayList<Object>();
        expElemFirst.appendElementValues(valuesList);
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
        final ExpressionElement expElemFirst = new IsNotNullExpressionElement( property);
        System.out.println("expElemFirst.render(): " + expElemFirst.renderSqlElement(new NullNameSolver())); //$NON-NLS-1$
        assertEquals( "goodbye IS NOT NULL " , expElemFirst.renderSqlElement(new NullNameSolver()) ); //$NON-NLS-1$

        final List<Object> valuesList = new ArrayList<Object>();
        expElemFirst.appendElementValues(valuesList);
        System.out.println("valuesList: " + valuesList); //$NON-NLS-1$
        assertEquals(0, valuesList.size());
    }

    @Test
    public void testIsNull() {
        final String property = "goodbye"; //$NON-NLS-1$
        final ExpressionElement expElemFirst = new IsNullExpressionElement(property);
        System.out.println("expElemFirst.render(): " + expElemFirst.renderSqlElement(new NullNameSolver())); //$NON-NLS-1$
        assertEquals( "goodbye IS NULL " , expElemFirst.renderSqlElement(new NullNameSolver()) ); //$NON-NLS-1$
    }

    @Test
    public void testAnd_Empty() {
        final ExpressionElement andExpression = new AndExpressionElement(new ExpressionElement[]{});
        System.out.println("andExpression.render(): " + andExpression.renderSqlElement(new NullNameSolver())); //$NON-NLS-1$
        assertEquals( "( 1=1 ) " , andExpression.renderSqlElement(new NullNameSolver()) ); //$NON-NLS-1$
    }

    @Test
    public void testAnd_OneElement() {
        final ExpressionElement expression = new IsNullExpressionElement("hello"); //$NON-NLS-1$

        final ExpressionElement andExpression = new AndExpressionElement(new ExpressionElement[]{expression});

        System.out.println("andExpression.render(): " + andExpression.renderSqlElement(new NullNameSolver())); //$NON-NLS-1$
        assertEquals( "( hello IS NULL ) " , andExpression.renderSqlElement(new NullNameSolver()) ); //$NON-NLS-1$
    }

    @Test
    public void testAnd_TwoElements() {
        final String property = "goodbye"; //$NON-NLS-1$
        final Object[] values = new Object[]{"hello1","hello2","hello3","hello4"}; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
        final ExpressionElement expElemOne = new InExpressionElement( property, Arrays.asList( values));
        final ExpressionElement expElemTwo = new IsNullExpressionElement("hello"); //$NON-NLS-1$

        final ExpressionElement andExpression = new AndExpressionElement(new ExpressionElement[]{expElemOne, expElemTwo});

        System.out.println("andExpression.render(): " + andExpression.renderSqlElement(new NullNameSolver())); //$NON-NLS-1$
        assertEquals( "( goodbye in ( ?, ?, ?, ? ) AND hello IS NULL ) " , andExpression.renderSqlElement(new NullNameSolver()) ); //$NON-NLS-1$

        final List<Object> valuesList = new ArrayList<Object>();
        andExpression.appendElementValues(valuesList);
        System.out.println("valuesList: " + valuesList); //$NON-NLS-1$
        assertEquals(4, valuesList.size());
        assertEquals("hello1", valuesList.get(0)); //$NON-NLS-1$
        assertEquals("hello2", valuesList.get(1)); //$NON-NLS-1$
        assertEquals("hello3", valuesList.get(2)); //$NON-NLS-1$
        assertEquals("hello4", valuesList.get(3)); //$NON-NLS-1$
    }

    @Test
    public void testAnd_ThreeElements() {
        final String property = "goodbye"; //$NON-NLS-1$
        final Object[] values = new Object[]{"hello1","hello2","hello3","hello4"}; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$

        final ExpressionElement andExpression = Exp.and(Exp.in( property, Arrays.asList( values) ),
                Exp.isNull("hello"), Exp.isNotNull("notHello")); //$NON-NLS-1$ //$NON-NLS-2$

        System.out.println("andExpression.render(): " + andExpression.renderSqlElement(new NullNameSolver())); //$NON-NLS-1$
        assertEquals( "( goodbye in ( ?, ?, ?, ? ) AND hello IS NULL AND notHello IS NOT NULL ) " , andExpression.renderSqlElement(new NullNameSolver()) ); //$NON-NLS-1$

    }

    @Test
    public void testOr_Empty() {
        final ExpressionElement orExpression = new OrExpressionElement(new ExpressionElement[]{});
        System.out.println("andExpression.render(): " + orExpression.renderSqlElement(new NullNameSolver())); //$NON-NLS-1$
        assertEquals( "( 1=1 ) " , orExpression.renderSqlElement(new NullNameSolver()) ); //$NON-NLS-1$
    }

    @Test
    public void testOr_OneElement() {
        final ExpressionElement expression = new IsNullExpressionElement("hello"); //$NON-NLS-1$

        final ExpressionElement orExpression = new OrExpressionElement(new ExpressionElement[]{expression});

        System.out.println("andExpression.render(): " + orExpression.renderSqlElement(new NullNameSolver())); //$NON-NLS-1$
        assertEquals( "( hello IS NULL ) " , orExpression.renderSqlElement(new NullNameSolver()) ); //$NON-NLS-1$
    }

    @Test
    public void testOr_TwoElements() {
        final String property = "goodbye"; //$NON-NLS-1$
        final Object[] values = new Object[]{"hello1","hello2","hello3","hello4"}; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
        final ExpressionElement expElemOne = new InExpressionElement( property, values);
        final ExpressionElement expElemTwo = new NInExpressionElement( property, new Object[]{"hello5","hello6","hello7","hello8"} ); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$

        final ExpressionElement orExpression = new OrExpressionElement(new ExpressionElement[]{expElemOne, expElemTwo});

        System.out.println("orExpression.render(): " + orExpression.renderSqlElement(new NullNameSolver())); //$NON-NLS-1$
        assertEquals( "( goodbye in ( ?, ?, ?, ? ) OR goodbye not in ( ?, ?, ?, ? ) ) " , orExpression.renderSqlElement(new NullNameSolver()) ); //$NON-NLS-1$

        final List<Object> valuesList = new ArrayList<Object>();
        orExpression.appendElementValues(valuesList);
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
    public void testOr_ThreeElements() {
        final String property = "goodbye"; //$NON-NLS-1$
        final Object[] values = new Object[]{"hello1","hello2","hello3","hello4"}; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$

        final ExpressionElement orExpression = Exp.or(Exp.in( property, Arrays.asList( values)),
                Exp.isNull("hello"), Exp.isNotNull("notHello")); //$NON-NLS-1$ //$NON-NLS-2$

        System.out.println("andExpression.render(): " + orExpression.renderSqlElement(new NullNameSolver())); //$NON-NLS-1$
        assertEquals( "( goodbye in ( ?, ?, ?, ? ) OR hello IS NULL OR notHello IS NOT NULL ) " , orExpression.renderSqlElement(new NullNameSolver()) ); //$NON-NLS-1$

    }

    @Test
    public void testNot() {
        final String property = "goodbye"; //$NON-NLS-1$
        final Object[] values = new Object[]{"hello1","hello2","hello3","hello4"}; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
        final ExpressionElement expElemOne = new InExpressionElement( property, Arrays.asList( values));

        final ExpressionElement notExpression = new NotExpressionElement(expElemOne);

        System.out.println("notExpression.render(): " + notExpression.renderSqlElement(new NullNameSolver())); //$NON-NLS-1$
        assertEquals( "NOT ( goodbye in ( ?, ?, ?, ? ) ) " , notExpression.renderSqlElement(new NullNameSolver()) ); //$NON-NLS-1$

        final List<Object> valuesList = new ArrayList<Object>();
        notExpression.appendElementValues(valuesList);
        System.out.println("valuesList: " + valuesList); //$NON-NLS-1$
        assertEquals(4, valuesList.size());
        assertEquals("hello1", valuesList.get(0)); //$NON-NLS-1$
        assertEquals("hello2", valuesList.get(1)); //$NON-NLS-1$
        assertEquals("hello3", valuesList.get(2)); //$NON-NLS-1$
        assertEquals("hello4", valuesList.get(3)); //$NON-NLS-1$
    }

    @Test
    public void testProperties1() {
        final String firstProperty = "User.id"; //$NON-NLS-1$
        final String secondProperty = "Employee.id"; //$NON-NLS-1$
        final ExpressionElement expElemOne = new NePropertiesExpressionElement( firstProperty, secondProperty);

        System.out.println("exp.render(): " + expElemOne.renderSqlElement(new NullNameSolver())); //$NON-NLS-1$
        assertEquals( "User.id != Employee.id " , expElemOne.renderSqlElement(new NullNameSolver()) ); //$NON-NLS-1$

        final List<Object> valuesList = new ArrayList<Object>();
        expElemOne.appendElementValues(valuesList);
        System.out.println("valuesList: " + valuesList); //$NON-NLS-1$
        assertEquals(0, valuesList.size());
    }

    @Test
    public void testProperties2() {
        final String firstProperty = "User.id"; //$NON-NLS-1$
        final String secondProperty = "Employee.id"; //$NON-NLS-1$
        final ExpressionElement expElemOne = new GtPropertiesExpressionElement( firstProperty, secondProperty);

        System.out.println("exp.render(): " + expElemOne.renderSqlElement(new NullNameSolver())); //$NON-NLS-1$
        assertEquals( "User.id > Employee.id " , expElemOne.renderSqlElement(new NullNameSolver()) ); //$NON-NLS-1$

        final List<Object> valuesList = new ArrayList<Object>();
        expElemOne.appendElementValues(valuesList);
        System.out.println("valuesList: " + valuesList); //$NON-NLS-1$
        assertEquals(0, valuesList.size());
    }
}
