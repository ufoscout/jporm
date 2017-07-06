/*******************************************************************************
 * Copyright 2013 Francesco Cina'

 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at

 * http://www.apache.org/licenses/LICENSE-2.0

 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.jporm.rm.kotlin.session.script

import com.jporm.commons.core.exception.JpoException
import com.jporm.rm.kotlin.session.ScriptExecutor
import com.jporm.rm.kotlin.session.SessionImpl
import org.slf4j.Logger
import org.slf4j.LoggerFactory

import java.io.ByteArrayInputStream
import java.io.IOException
import java.io.InputStream
import java.nio.charset.Charset

/**

 * @author Francesco Cina
 * *
 * *         02/lug/2011
 */
class ScriptExecutorImpl(private val session: SessionImpl) : ScriptExecutor {
    private val logger = LoggerFactory.getLogger(this.javaClass)

    @Throws(IOException::class, JpoException::class)
    override fun execute(scriptStream: InputStream) {
        this.execute(scriptStream, Charset.defaultCharset())
    }

    @Throws(IOException::class, JpoException::class)
    override fun execute(scriptStream: InputStream, charset: Charset) {
        logger.info("Begin script execution") //$NON-NLS-1$
        val parser = StreamParser(scriptStream, true, charset)
        parser.parse { sqlStatement: String ->
            if (logger.isDebugEnabled) {
                logger.debug("Execute statement: " + sqlStatement) //$NON-NLS-1$
            }
            session.sql().executor().update(sqlStatement, *arrayOfNulls<Any>(0))
        }
        logger.info("End script execution") //$NON-NLS-1$
    }

    @Throws(JpoException::class)
    override fun execute(script: String) {
        val charset = Charset.defaultCharset()
        val `is` = ByteArrayInputStream(script.toByteArray(charset))
        try {
            this.execute(`is`, charset)
        } catch (e: IOException) {
            throw JpoException(e)
        }

    }

}
