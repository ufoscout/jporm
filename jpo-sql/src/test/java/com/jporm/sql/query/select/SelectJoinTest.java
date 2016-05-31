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
package com.jporm.sql.query.select;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;

import com.jporm.sql.BaseSqlTestApi;
import com.jporm.sql.query.select.where.SelectWhere;

/**
 *
 * @author Francesco Cina
 *
 *         07/lug/2011
 */
public class SelectJoinTest extends BaseSqlTestApi {

    @Test
    public void testLimitOffset() {
        Select<String> select = dsl().select("emp.name").from("Employee", "emp");
        select.limit(10);
        select.offset(5);
        String sql = select.sqlQuery();
        getLogger().info(sql);
    }


    @Test
    public void testJoinWithInnerSelectQuery() {
        SelectWhere query = dsl()
                .select("first", "second")
                .from("Employee")
                .innerJoin(dsl().selectAll().from("inner").where("x = ?", "X"), "i")
                .where("mod(Employee.id, 10) = ?", "Y");


        String sqlQuery = query.sqlQuery();
        getLogger().info("Generated select: \n{}", sqlQuery);

        assertTrue(containsIgnoreCase(sqlQuery, "SELECT"));
        assertTrue(containsIgnoreCase(sqlQuery, " first AS \"first\", "));
        assertTrue(containsIgnoreCase(sqlQuery, " second "));
        assertTrue(containsIgnoreCase(sqlQuery, " FROM EMPLOYEE INNER JOIN (SELECT * FROM inner WHERE x = ? ) i WHERE mod(Employee.ID, 10) = ? "));

        List<Object> values = query.sqlValues();

        assertEquals(2, values.size());
        assertEquals("X", values.get(0));
        assertEquals("Y", values.get(1));
    }

    private boolean containsIgnoreCase(String text, String substring) {
        return text.toLowerCase().contains(substring.toLowerCase());
    }

}
