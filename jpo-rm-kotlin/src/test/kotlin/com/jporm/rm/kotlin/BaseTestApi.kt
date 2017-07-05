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
package com.jporm.rm.kotlin

import java.io.File
import java.math.BigDecimal
import java.util.Date

import javax.annotation.Resource
import javax.sql.DataSource

import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.rules.TestName
import org.junit.runner.RunWith
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit4.SpringRunner

import com.jporm.test.util.DerbyNullOutputUtil

/**

 * @author Francesco Cina
 * *
 * *         20/mag/2011
 */
@RunWith(SpringRunner::class)
@SpringBootTest(classes = arrayOf(JpoCoreTestConfig::class))
abstract class BaseTestApi {

    protected val testInputBasePath = "./src/test/files" //$NON-NLS-1$
    private val TEST_FILE_OUTPUT_BASE_PATH = "./target/test/files" //$NON-NLS-1$

    @Rule @JvmField
    val name = TestName()

    @Resource
    protected val h2DataSource: DataSource? = null

    private var startTime: Date? = null

    val logger = LoggerFactory.getLogger(this.javaClass)

    protected val jpo: JpoRm
        get() = JpoRmBuilder.get().build(h2DataSource)

    protected val testOutputBasePath: String
        get() {
            mkDir(TEST_FILE_OUTPUT_BASE_PATH)
            return TEST_FILE_OUTPUT_BASE_PATH
        }

    protected fun mkDir(dirPath: String) {
        val path = File(dirPath)
        if (!path.exists()) {
            path.mkdirs()
        }
    }

    @Before
    fun setUpBeforeTest() {

        startTime = Date()

        logger.info("===================================================================") //$NON-NLS-1$
        logger.info("BEGIN TEST " + name.methodName) //$NON-NLS-1$
        logger.info("===================================================================") //$NON-NLS-1$

    }

    @After
    fun tearDownAfterTest() {

        val time = BigDecimal(Date().time - startTime!!.time).divide(BigDecimal(1000)).toString()

        logger.info("===================================================================") //$NON-NLS-1$
        logger.info("END TEST " + name.methodName) //$NON-NLS-1$
        logger.info("Execution time: $time seconds") //$NON-NLS-1$ //$NON-NLS-2$
        logger.info("===================================================================") //$NON-NLS-1$

    }

    companion object {

        init {
            System.setProperty("derby.stream.error.field", DerbyNullOutputUtil.NULL_DERBY_LOG)
        }
    }

}
