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

/**
 *
 * @author Francesco Cina
 *
 *         07/lug/2011
 */
public class SelectUnionAllTest extends BaseSqlTestApi {

    @Test
    public void testUnionTwoQueries() {
        Select<String> selectOne = dsl().select("emp1_0.name").from("EMPLOYEE", "emp1_0");

        Select<String> selectTwo = dsl().select("emp2_0.name").from("EMPLOYEE", "emp2_0");

        selectOne.unionAll(selectTwo);

        String sql = selectOne.sqlQuery();
        getLogger().info(sql);

        String expected = "";
        expected += "SELECT emp1_0.name AS \"emp1_0.name\" FROM EMPLOYEE emp1_0 ";
        expected += "\nUNION ALL \n";
        expected += "SELECT emp2_0.name AS \"emp2_0.name\" FROM EMPLOYEE emp2_0 ";

        assertEquals(expected, sql);

    }

    @Test
    public void testUnionMoreQueries() {
        Select<String> selectOne = dsl().select("emp1_0.name").from("EMPLOYEE", "emp1_0");

        Select<String> selectTwo = dsl().select("emp2_0.name").from("EMPLOYEE", "emp2_0");

        Select<String> selectThree = dsl().select("emp3_0.name").from("EMPLOYEE", "emp3_0");

        selectOne.unionAll(selectTwo);
        selectOne.unionAll(selectThree);
        selectOne.unionAll(selectThree);

        String sql = selectOne.sqlQuery();
        getLogger().info(sql);

        String expected = "";
        expected += "SELECT emp1_0.name AS \"emp1_0.name\" FROM EMPLOYEE emp1_0 ";
        expected += "\nUNION ALL \n";
        expected += "SELECT emp2_0.name AS \"emp2_0.name\" FROM EMPLOYEE emp2_0 ";
        expected += "\nUNION ALL \n";
        expected += "SELECT emp3_0.name AS \"emp3_0.name\" FROM EMPLOYEE emp3_0 ";
        expected += "\nUNION ALL \n";
        expected += "SELECT emp3_0.name AS \"emp3_0.name\" FROM EMPLOYEE emp3_0 ";

        assertEquals(expected, sql);

    }

    @Test
    public void testInnerUnionQueries() {
        Select<String> selectOne = dsl().select("emp1_0.name").from("EMPLOYEE", "emp1_0");

        Select<String> selectTwo = dsl().select("emp2_0.name").from("EMPLOYEE", "emp2_0");

        Select<String> selectThree = dsl().select("emp3_0.name").from("EMPLOYEE", "emp3_0");

        selectOne.unionAll(selectTwo);
        selectTwo.unionAll(selectThree);

        String sql = selectOne.sqlQuery();
        getLogger().info(sql);

        String expected = "";
        expected += "SELECT emp1_0.name AS \"emp1_0.name\" FROM EMPLOYEE emp1_0 ";
        expected += "\nUNION ALL \n";
        expected += "SELECT emp2_0.name AS \"emp2_0.name\" FROM EMPLOYEE emp2_0 ";
        expected += "\nUNION ALL \n";
        expected += "SELECT emp3_0.name AS \"emp3_0.name\" FROM EMPLOYEE emp3_0 ";

        assertEquals(expected, sql);

    }

}
