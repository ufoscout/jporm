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
package com.jporm.commons.core.inject.config;

import com.jporm.commons.core.transaction.TransactionIsolation;

public class ConfigServiceImpl implements ConfigService {

    private int transactionDefaultTimeoutSeconds = -1;
    private TransactionIsolation defaultTransactionIsolation = TransactionIsolation.READ_COMMITTED;

    /**
     * @return the defaultTransactionIsolation
     */
    @Override
    public TransactionIsolation getDefaultTransactionIsolation() {
        return defaultTransactionIsolation;
    }

    /**
     * @return the transactionDefaultTimeoutSeconds
     */
    @Override
    public int getTransactionDefaultTimeoutSeconds() {
        return transactionDefaultTimeoutSeconds;
    }

    /**
     * @param defaultTransactionIsolation
     *            the defaultTransactionIsolation to set
     */
    public void setDefaultTransactionIsolation(final TransactionIsolation defaultTransactionIsolation) {
        this.defaultTransactionIsolation = defaultTransactionIsolation;
    }

    /**
     * @param transactionDefaultTimeoutSeconds
     *            the transactionDefaultTimeoutSeconds to set
     */
    public void setTransactionDefaultTimeoutSeconds(final int transactionDefaultTimeoutSeconds) {
        this.transactionDefaultTimeoutSeconds = transactionDefaultTimeoutSeconds;
    }

}
