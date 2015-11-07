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
package com.jporm.rx.vertx.session.vertx3.jdbcclient;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.jporm.rx.vertx.BaseTestApi;
import com.jporm.types.io.ResultSet;

import io.vertx.core.json.JsonArray;

public class Vertx3GeneratedKeysResultSetTest extends BaseTestApi {

    @Test
    public void nextShouldReturnFalseForEmptyArray() {
        JsonArray keys = new JsonArray();
        ResultSet keysRS = new Vertx3GeneratedKeysResultSet(keys, new String[0]);
        assertFalse(keysRS.next());
    }

    @Test
    public void nextShouldReturnTrueForNotEmptyArray() {
        JsonArray keys = new JsonArray();
        keys.add(true);
        ResultSet keysRS = new Vertx3GeneratedKeysResultSet(keys, new String[0]);
        assertTrue(keysRS.next());
        assertFalse(keysRS.next());
    }

}
