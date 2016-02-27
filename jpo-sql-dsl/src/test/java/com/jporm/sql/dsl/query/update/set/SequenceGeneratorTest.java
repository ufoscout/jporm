/*******************************************************************************
 * Copyright 2016 Francesco Cina'
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
package com.jporm.sql.dsl.query.update.set;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.jporm.sql.dsl.BaseSqlTestApi;
import com.jporm.sql.dsl.dialect.H2DBProfile;
import com.jporm.sql.dsl.dialect.PostgresDBProfile;
import com.jporm.sql.dsl.query.set.Generator;

public class SequenceGeneratorTest extends BaseSqlTestApi {

    @Test
    public void testH2Sequence() {
        String sequenceName = "MY_SEQUENCE";
        String expected = new H2DBProfile().getSqlStrategy().insertQuerySequence(sequenceName);

        StringBuilder queryBuilder = new StringBuilder();
        Generator.sequence(sequenceName).questionMarkReplacement(queryBuilder, new H2DBProfile());
        String actual = queryBuilder.toString();
        getLogger().info("Sequence render is: [{}]", actual);

        assertEquals(expected, actual);
    }

    @Test
    public void testPostgresSequence() {
        String sequenceName = "MY_SEQUENCE";
        String expected = new PostgresDBProfile().getSqlStrategy().insertQuerySequence(sequenceName);

        StringBuilder queryBuilder = new StringBuilder();
        Generator.sequence(sequenceName).questionMarkReplacement(queryBuilder, new PostgresDBProfile());
        String actual = queryBuilder.toString();

        getLogger().info("Sequence render is: [{}]", actual);
        assertEquals(expected, actual);
    }

}
