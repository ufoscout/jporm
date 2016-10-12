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
package com.jporm.rx;

import com.jporm.rm.connection.Transaction;
import com.jporm.rx.connection.MaybeFunction;
import com.jporm.rx.connection.RxTransaction;
import com.jporm.rx.connection.SingleFunction;
import com.jporm.rx.session.Session;

import io.reactivex.Maybe;
import io.reactivex.Single;

/**
 *
 * @author Francesco Cina
 *
 *         21/mag/2011
 */
public interface JpoRx {

    /**
     * Return a {@link Session} from the current {@link JpoRx} implementation
     *
     * @return
     */
    Session session();
    /**
     * Returns a new {@link Transaction} instance.
     *
     * @return
     */
    RxTransaction tx();

    /**
     * Executes the transaction. All the actions performed on the session are
     * executed in a transaction. The transaction is committed only if all the
     * performed actions succeed.
     *
     * @param session
     * @return
     */
    <T> Maybe<T> tx(MaybeFunction<T> txSession);

    /**
     * Executes the transaction. All the actions performed on the session are
     * executed in a transaction. The transaction is committed only if all the
     * performed actions succeed.
     *
     * @param session
     * @return
     */
    <T> Single<T> tx(SingleFunction<T> txSession);

}
