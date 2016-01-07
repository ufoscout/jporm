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
package com.jporm.sql.query.clause.select;

import static org.junit.Assert.*;

import org.junit.Test;

import com.jporm.core.domain.Employee;
import com.jporm.sql.BaseSqlTestApi;
import com.jporm.sql.dialect.DBType;
import com.jporm.sql.query.clause.impl.SelectImpl;
import com.jporm.sql.query.namesolver.impl.PropertiesFactory;

/**
 *
 * @author Francesco Cina
 *
 *         07/lug/2011
 */
public class SelectUnionAllTest extends BaseSqlTestApi {

    @Test
    public void testUnionTwoQueries() {
        SelectImpl<Employee> selectOne = new SelectImpl<>(getClassDescriptorMap(), new PropertiesFactory(), Employee.class, "emp1");
        selectOne.selectFields(new String[]{ "emp1.name" });

        SelectImpl<Employee> selectTwo = new SelectImpl<>(getClassDescriptorMap(), new PropertiesFactory(), Employee.class, "emp2");
        selectTwo.selectFields(new String[]{ "emp2.name" });

        selectOne.unionAll(selectTwo);

        String sql = selectOne.renderSql(DBType.ORACLE.getDBProfile());
        getLogger().info(sql);

        String expected = "";
        expected += "SELECT emp1_0.NAME AS \"emp1.name\" FROM EMPLOYEE emp1_0 ";
        expected += "\nUNION ALL \n";
        expected += "SELECT emp2_0.NAME AS \"emp2.name\" FROM EMPLOYEE emp2_0 ";

        assertEquals(expected, sql);

    }

    @Test
    public void testUnionMoreQueries() {
        SelectImpl<Employee> selectOne = new SelectImpl<>(getClassDescriptorMap(), new PropertiesFactory(), Employee.class, "emp1");
        selectOne.selectFields(new String[]{ "emp1.name" });

        SelectImpl<Employee> selectTwo = new SelectImpl<>(getClassDescriptorMap(), new PropertiesFactory(), Employee.class, "emp2");
        selectTwo.selectFields(new String[]{ "emp2.name" });

        SelectImpl<Employee> selectThree = new SelectImpl<>(getClassDescriptorMap(), new PropertiesFactory(), Employee.class, "emp3");
        selectThree.selectFields(new String[]{ "emp3.name" });

        selectOne.unionAll(selectTwo);
        selectOne.unionAll(selectThree);
        selectOne.unionAll(selectThree);

        String sql = selectOne.renderSql(DBType.ORACLE.getDBProfile());
        getLogger().info(sql);

        String expected = "";
        expected += "SELECT emp1_0.NAME AS \"emp1.name\" FROM EMPLOYEE emp1_0 ";
        expected += "\nUNION ALL \n";
        expected += "SELECT emp2_0.NAME AS \"emp2.name\" FROM EMPLOYEE emp2_0 ";
        expected += "\nUNION ALL \n";
        expected += "SELECT emp3_0.NAME AS \"emp3.name\" FROM EMPLOYEE emp3_0 ";
        expected += "\nUNION ALL \n";
        expected += "SELECT emp3_0.NAME AS \"emp3.name\" FROM EMPLOYEE emp3_0 ";

        assertEquals(expected, sql);

    }

    @Test
    public void testInnerUnionQueries() {
        SelectImpl<Employee> selectOne = new SelectImpl<>(getClassDescriptorMap(), new PropertiesFactory(), Employee.class, "emp1");
        selectOne.selectFields(new String[]{ "emp1.name" });

        SelectImpl<Employee> selectTwo = new SelectImpl<>(getClassDescriptorMap(), new PropertiesFactory(), Employee.class, "emp2");
        selectTwo.selectFields(new String[]{ "emp2.name" });

        SelectImpl<Employee> selectThree = new SelectImpl<>(getClassDescriptorMap(), new PropertiesFactory(), Employee.class, "emp3");
        selectThree.selectFields(new String[]{ "emp3.name" });

        selectOne.unionAll(selectTwo);
        selectTwo.unionAll(selectThree);

        String sql = selectOne.renderSql(DBType.ORACLE.getDBProfile());
        getLogger().info(sql);

        String expected = "";
        expected += "SELECT emp1_0.NAME AS \"emp1.name\" FROM EMPLOYEE emp1_0 ";
        expected += "\nUNION ALL \n";
        expected += "SELECT emp2_0.NAME AS \"emp2.name\" FROM EMPLOYEE emp2_0 ";
        expected += "\nUNION ALL \n";
        expected += "SELECT emp3_0.NAME AS \"emp3.name\" FROM EMPLOYEE emp3_0 ";

        assertEquals(expected, sql);

    }

}
