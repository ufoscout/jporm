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
/* ----------------------------------------------------------------------------
 *     PROJECT : JPOrm
 *
 *  CREATED BY : Francesco Cina'
 *          ON : Mar 5, 2013
 * ----------------------------------------------------------------------------
 */
package com.jporm.query.find.cache;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;

import org.junit.Test;

import com.jporm.BaseTestApi;
import com.jporm.domain.section08.UserAddress;
import com.jporm.domain.section08.UserCountry;
import com.jporm.domain.section08.UserWithAddress;
import com.jporm.query.OrmRowMapper;
import com.jporm.query.find.cache.CacheStrategy;
import com.jporm.query.find.cache.CacheStrategyCallback;
import com.jporm.query.find.cache.CacheStrategyEntry;
import com.jporm.session.Session;
import com.jporm.session.TransactionCallback;

/**
 * <class_description>
 * <p><b>notes</b>:
 * <p>ON : Mar 5, 2013
 *
 * @author Francesco Cina'
 * @version $Revision
 */
@SuppressWarnings("nls")
public class CacheStrategyImplTest extends BaseTestApi{

    @Test
    public void testCache() {

        CacheStrategy cacheStrategy = getJPO().getServiceCatalog().getCacheStrategy();

        String sql = "sql" + UUID.randomUUID(); //$NON-NLS-1$
        List<Object> values = Arrays.asList(new Object[]{Integer.MAX_VALUE, Integer.MIN_VALUE});

        final ArrayList<Integer> result = new ArrayList<Integer>();
        final OrmRowMapper<Integer> srr = new OrmRowMapper<Integer>() {
            @Override
            public void read(final Integer newObject, final int rowCount) {
                getLogger().info("SSR called for [{}]", newObject); //$NON-NLS-1$
                result.add(newObject);
            }
        };

        final int howMany = 100;
        final AtomicBoolean callbackCalled = new AtomicBoolean(false);

        CacheStrategyCallback<Integer> csc = new CacheStrategyCallback<Integer>() {
            @Override
            public void doWhenNotInCache(final CacheStrategyEntry<Integer> cacheStrategyEntry) {
                callbackCalled.set(true);
                for (int i=0; i<howMany; i++) {
                    Integer bean = new Random().nextInt();
                    srr.read(bean, i);
                    cacheStrategyEntry.add(bean);
                }
                cacheStrategyEntry.end();
            }
        };

        String cacheName = "cacheName";
        //FIND NOT IN CACHE
        cacheStrategy.find(cacheName, sql, values, new ArrayList<String>(), srr, csc);
        assertTrue(callbackCalled.get());
        assertEquals(howMany, result.size());

        //FIND IN CACHE
        callbackCalled.set(false);
        final List<Integer> oldResult = (List<Integer>) result.clone();
        result.clear();
        cacheStrategy.find(cacheName, sql, values, new ArrayList<String>(), srr, csc);
        assertFalse(callbackCalled.get());
        assertEquals(howMany, result.size());
        assertEquals(oldResult, result);

    }

    @Test
    public void testCachedUserCountry() {
        getJPO().session().doInTransaction(new TransactionCallback<Void>() {
            @Override
            public Void doInTransaction(final Session session) {

                UserWithAddress user = new UserWithAddress();
                user.setFirstname("firstname" + UUID.randomUUID());
                user.setLastname("lastname" + UUID.randomUUID());

                user.setAddress(new UserAddress());
                user.getAddress().setCountry( new UserCountry() );

                String countryName = "countryName" + UUID.randomUUID();
                user.getAddress().getCountry().setName(countryName);

                user = session.save(user);

                getLogger().info("Search user address");
                assertEquals(countryName, session.find(UserWithAddress.class, user.getId()).get().getAddress().getCountry().getName());

                String newCountryName = "Changed-" + countryName;
                user.getAddress().getCountry().setName(newCountryName);
                session.update(user.getAddress().getCountry());

                //The new name should be returned because in this query the Bean cache is not used
                getLogger().info("Search user country with no cache");
                assertEquals(newCountryName, session.findQuery(UserCountry.class).where().eq("id", user.getAddress().getCountry().getId()).get().getName());
                //The old name should be return if the cache works properly
                getLogger().info("Search user country with cache");
                assertEquals(countryName, session.find(UserCountry.class, user.getAddress().getCountry().getId()).get().getName());
                assertEquals(countryName, session.find(UserWithAddress.class, user.getId()).get().getAddress().getCountry().getName());

                assertTrue( session.delete(user) > 0 );

                assertNull( session.findQuery(UserCountry.class).where().eq("id", user.getAddress().getCountry().getId()).get() );
                assertNotNull( session.find(UserCountry.class, user.getAddress().getCountry().getId()).get() );

                return null;
            }
        });
    }

}
