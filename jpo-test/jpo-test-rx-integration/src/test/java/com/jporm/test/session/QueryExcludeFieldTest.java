/**
 * *****************************************************************************
 * Copyright 2013 Francesco Cina'
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 * ****************************************************************************
 */
package com.jporm.test.session;

import com.jporm.rx.core.session.Session;
import com.jporm.sql.query.clause.impl.where.Exp;
import com.jporm.test.BaseTestAllDB;
import com.jporm.test.TestData;
import com.jporm.test.domain.section05.AutoId;
import com.jporm.test.domain.section08.CommonUser;
import java.util.Date;
import java.util.Random;
import java.util.concurrent.ExecutionException;
import org.junit.Test;

/**
 *
 * @author Francesco Cina
 *
 * 20/mag/2011
 */
public class QueryExcludeFieldTest extends BaseTestAllDB {

    public QueryExcludeFieldTest(final String testName, final TestData testData) {
        super(testName, testData);
    }

    @Test
    public void testExcludeOnFind() throws InterruptedException, ExecutionException {
        Session session = getJPO().session();
        AutoId autoId = new AutoId();
        final String value = "value for test " + new Date().getTime(); //$NON-NLS-1$
        autoId.setValue(value);
        autoId = session.save(autoId).get();

        AutoId autoIdWithoutValue = session.findQuery(AutoId.class).ignore("value").where(Exp.eq("id", autoId.getId())).getUnique().get(); //$NON-NLS-1$
        AutoId autoIdWithValue = session.findQuery(AutoId.class).ignore(false, "value").where(Exp.eq("id", autoId.getId())).getUnique().get(); //$NON-NLS-1$

        assertEquals(autoId.getId(), autoIdWithValue.getId());
        assertNull(autoIdWithoutValue.getValue());
        assertEquals(autoId.getId(), autoIdWithValue.getId());
        assertEquals(value, autoIdWithValue.getValue());
    }

    @Test
    public void testGetShouldReturnFirstResultSetEntry() throws InterruptedException, ExecutionException {
        Session session = getJPO().session();

        long suffix = new Random().nextLong();

        session.deleteQuery(CommonUser.class).now().get();

        CommonUser user = new CommonUser();
        user.setUserAge(0l);
        user.setFirstname("aaa" + suffix);
        user.setLastname("aaa" + suffix);
        session.save(user).get();

        user.setFirstname("bbb" + suffix);
        session.save(user).get();

        user.setFirstname("ccc" + suffix);
        session.save(user).get();

        assertEquals(session.findQuery(CommonUser.class).orderBy().desc("firstname").getList().get().get(0).getFirstname(),
                session.findQuery(CommonUser.class).orderBy().desc("firstname").getOptional().get().get().getFirstname());

        assertEquals(session.findQuery(CommonUser.class).orderBy().asc("firstname").getList().get().get(0).getFirstname(),
                session.findQuery(CommonUser.class).orderBy().asc("firstname").getOptional().get().get().getFirstname());

    }
}
