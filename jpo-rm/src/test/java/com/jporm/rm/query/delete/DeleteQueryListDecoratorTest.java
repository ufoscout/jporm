/*******************************************************************************
 * Copyright 2015 Francesco Cina'
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
package com.jporm.rm.query.delete;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.jporm.rm.BaseTestApi;

public class DeleteQueryListDecoratorTest extends BaseTestApi {

    private class TestDeleteQuery implements DeleteQuery {

        int value;

        TestDeleteQuery(final int value) {
            this.value = value;
        }

        @Override
        public int execute() {
            return value;
        }

    }

    @Test
    public void testDeleteQueryListDecorator() {
        TestDeleteQuery query1 = new TestDeleteQuery(1);
        TestDeleteQuery query2 = new TestDeleteQuery(2);
        TestDeleteQuery query3 = new TestDeleteQuery(3);
        DeleteQueryListDecorator queryList = new DeleteQueryListDecorator();
        queryList.add(query1);
        queryList.add(query2);
        queryList.add(query3);

        int result = queryList.execute();
        assertEquals(1 + 2 + 3, result);

    }

}
