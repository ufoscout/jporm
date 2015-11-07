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
package com.jporm.rm.transaction;

import com.jporm.rm.session.Session;

/**
 *
 * @author ufo
 *
 * @param <T>
 */
@FunctionalInterface
public interface TransactionCallback<T> {

    /**
     * The code inside this block is executed inside a transaction. Transaction
     * begin, commit and rollback calls are handled automatically
     * 
     * @param session
     * @return
     */
    T doInTransaction(Session txSession);

}
