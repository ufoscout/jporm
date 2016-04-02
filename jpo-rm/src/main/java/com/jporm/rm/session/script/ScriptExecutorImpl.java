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

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jporm.commons.core.exception.JpoException;
import com.jporm.rm.session.ScriptExecutor;
import com.jporm.rm.session.SessionImpl;

/**
 *
 * @author Francesco Cina
 *
 *         02/lug/2011
 */
public class ScriptExecutorImpl implements ScriptExecutor {

    private final SessionImpl session;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    public ScriptExecutorImpl(final SessionImpl session) {
        this.session = session;
    }

    @Override
    public void execute(final InputStream scriptStream) throws IOException, JpoException {
        this.execute(scriptStream, Charset.defaultCharset());
    }

    @Override
    public void execute(final InputStream scriptStream, final Charset charset) throws IOException, JpoException {
        logger.info("Begin script execution"); //$NON-NLS-1$
        Parser parser = new StreamParser(scriptStream, true, charset);
        parser.parse((final String sqlStatement) -> {
            if (logger.isDebugEnabled()) {
                logger.debug("Execute statement: " + sqlStatement); //$NON-NLS-1$
            }
            session.sql().executor().update(sqlStatement, new Object[0]);
        });
        logger.info("End script execution"); //$NON-NLS-1$
    }

    @Override
    public void execute(final String script) throws JpoException {
        Charset charset = Charset.defaultCharset();
        InputStream is = new ByteArrayInputStream(script.getBytes(charset));
        try {
            this.execute(is, charset);
        } catch (IOException e) {
            throw new JpoException(e);
        }
    }

}
