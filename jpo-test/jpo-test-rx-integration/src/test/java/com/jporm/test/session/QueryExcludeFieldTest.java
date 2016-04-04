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

import java.util.Date;
import java.util.Random;
import java.util.concurrent.ExecutionException;

import org.junit.Test;

import com.jporm.rx.session.Session;
import com.jporm.sql.query.where.Exp;
import com.jporm.test.BaseTestAllDB;
import com.jporm.test.TestData;
import com.jporm.test.domain.section05.AutoId;
import com.jporm.test.domain.section08.CommonUser;

/**
 *
 * @author Francesco Cina
 *
 *         20/mag/2011
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

        AutoId autoIdWithoutValue = session.find(AutoId.class).ignore("value").where(Exp.eq("id", autoId.getId())).fetchUnique().get(); //$NON-NLS-1$
        AutoId autoIdWithValue = session.find(AutoId.class).where(Exp.eq("id", autoId.getId())).fetchUnique().get(); //$NON-NLS-1$

        threadAssertEquals(autoId.getId(), autoIdWithValue.getId());
        threadAssertNull(autoIdWithoutValue.getValue());
        threadAssertEquals(autoId.getId(), autoIdWithValue.getId());
        threadAssertEquals(value, autoIdWithValue.getValue());
    }

    @Test
    public void testGetShouldReturnFirstResultSetEntry() throws InterruptedException, ExecutionException {
        Session session = getJPO().session();

        long suffix = new Random().nextLong();

        session.delete(CommonUser.class).execute().get();

        CommonUser user = new CommonUser();
        user.setUserAge(0l);
        user.setFirstname("aaa" + suffix);
        user.setLastname("aaa" + suffix);
        session.save(user).get();

        user.setFirstname("bbb" + suffix);
        session.save(user).get();

        user.setFirstname("ccc" + suffix);
        session.save(user).get();

        threadAssertEquals(session.find(CommonUser.class).orderBy().desc("firstname").fetchList().get().get(0).getFirstname(),
                session.find(CommonUser.class).orderBy().desc("firstname").fetchOptional().get().get().getFirstname());

        threadAssertEquals(session.find(CommonUser.class).orderBy().asc("firstname").fetchList().get().get(0).getFirstname(),
                session.find(CommonUser.class).orderBy().asc("firstname").fetchOptional().get().get().getFirstname());

    }
}
