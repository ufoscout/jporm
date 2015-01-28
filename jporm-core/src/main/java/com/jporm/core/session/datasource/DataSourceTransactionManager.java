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
package com.jporm.core.session.datasource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jporm.core.transaction.TransactionDefinition;

/**
 * <class_description>
 * <p><b>notes</b>:
 * <p>ON : Mar 9, 2013
 *
 * @author  - Francesco Cina
 * @version $Revision
 */
public class DataSourceTransactionManager {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    public DataSourceTransaction startTransaction(final DataSourceSessionProvider dataSourceSessionProvider, final TransactionDefinition transactionDefinition) {
        logger.debug("Starting new Transaction"); //$NON-NLS-1$
        return new DataSourceTransaction(dataSourceSessionProvider, transactionDefinition, this);
    }

    public void setRollbackOnly(final DataSourceTransaction transaction) {
        logger.debug("Set transaction as rollback only"); //$NON-NLS-1$
        transaction.setRollbackOnly(true);
        transaction.getConnection().setRollbackOnly();
    }

    public void commit(final DataSourceTransaction transaction) {
        if (transaction.isRollbackOnly()) {
            rollback(transaction);
            return;
        }
        try {
            if (!transaction.isClosed() && !transaction.getConnection().isClosed()) {
                logger.debug("Commit called"); //$NON-NLS-1$
                transaction.getConnection().commit();
            }
        } finally {
            transaction.getConnection().close(transaction);
            transaction.setClosed(true);
        }
    }

    public void rollback(final DataSourceTransaction transaction) {
        setRollbackOnly(transaction);
        try {
            if (!transaction.isClosed() && !transaction.getConnection().isClosed()) {
                logger.debug("Rollback called"); //$NON-NLS-1$
                transaction.getConnection().rollback();
            }
        } finally {
            transaction.getConnection().close(transaction);
            transaction.setClosed(true);
        }
    }

}
