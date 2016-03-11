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
package com.jporm.rm.session.script;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jporm.rm.session.SessionImpl;
import com.jporm.rm.session.SqlExecutor;

/**
 *
 * @author Francesco Cina
 *
 *         02/lug/2011
 */
public class SessionParserCallback implements ParserCallback {

    private final SqlExecutor sqlExec;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    public SessionParserCallback(final SessionImpl session) {
        sqlExec = session.sql().executor();
    }

    @Override
    public void parseAction(final String sqlStatement) {
        if (logger.isDebugEnabled()) {
            logger.debug("Execute statement: " + sqlStatement); //$NON-NLS-1$
        }
        sqlExec.update(sqlStatement, new Object[0]);
    }

}
