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
package com.jporm.test.session;

import org.junit.Ignore;

import com.jporm.test.BaseTestAllDB;
import com.jporm.test.TestData;

/**
 *
 * @author Francesco Cina
 *
 *         20/mag/2011
 */
@Ignore
// This test is disabled because it needs the methods to handle collections of
// objects which are not yet implemented
public class SessionCollectionsCRUDTestDisabled extends BaseTestAllDB {

    public SessionCollectionsCRUDTestDisabled(final String testName, final TestData testData) {
        super(testName, testData);
    }

    // @Test
    // public void testCreateDeleteCollection() {
    // final JPO jpOrm =getJPO();
    //
    // // CREATE
    // final Session conn = jpOrm.session();
    //
    // conn.txVoidNow((_session) -> {
    // List<AutoId> entries = new ArrayList<>();
    // entries.add(new AutoId());
    // entries.add(new AutoId());
    // entries.add(new AutoId());
    // entries.add(new AutoId());
    // entries = conn.save(entries);
    //
    // entries.forEach(entry ->
    // threadAssertTrue(_session.find(entry).getRowCount()>0));
    //
    // threadAssertEquals( entries.size(), _session.delete(entries) );
    //
    // entries.forEach(entry ->
    // threadAssertFalse(_session.find(entry).getRowCount()>0));
    // });
    //
    // }
    //
    // @Test
    // public void testCreateUpdateCollection() {
    // final JPO jpOrm =getJPO();
    //
    // // CREATE
    // final Session conn = jpOrm.session();
    //
    // conn.txVoidNow((_session) -> {
    // List<AutoId> entries = new ArrayList<>();
    // entries.add(new AutoId());
    // entries.add(new AutoId());
    // entries.add(new AutoId());
    // entries.add(new AutoId());
    //
    // String value1 = UUID.randomUUID().toString();
    // entries.forEach(entry -> entry.setValue(value1));
    //
    // entries = conn.save(entries);
    // entries.forEach(entry -> threadAssertEquals(value1,
    // _session.find(entry).getUnique().getValue()));
    //
    // String value2 = UUID.randomUUID().toString();
    // entries.forEach(entry -> entry.setValue(value2));
    // entries = conn.update(entries);
    //
    // entries.forEach(entry -> threadAssertEquals(value2,
    // _session.find(entry).getUnique().getValue()));
    //
    // });
    //
    // }

}
