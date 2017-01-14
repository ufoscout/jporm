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
package com.jporm.rm.quasar;

import java.math.BigDecimal;
import java.util.Date;

import javax.annotation.Resource;
import javax.sql.DataSource;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.rules.TestName;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.jporm.rm.JpoRm;
import com.jporm.test.util.DerbyNullOutputUtil;

/**
 *
 * @author Francesco Cina
 *
 *         20/mag/2011
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = { RmQuasarTestConfig.class })
public abstract class RmQuasarTestBase {

	static {
		System.setProperty("derby.stream.error.field", DerbyNullOutputUtil.NULL_DERBY_LOG);
	}

	@Rule
	public final TestName name = new TestName();

	private Date startTime;

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Resource
	private DataSource dataSource;

	protected DataSource getH2DataSource() {
		return dataSource;
	}

	public Logger getLogger() {
		return logger;
	}

	protected JpoRm newJpo() {
		return JpoRmQuasarBuilder.get().build(getH2DataSource());
	}

	@Before
	public void setUpBeforeTest() {

		startTime = new Date();

		logger.info("===================================================================");
		logger.info("BEGIN TEST " + name.getMethodName());
		logger.info("===================================================================");

	}

	@After
	public void tearDownAfterTest() {

		final String time = new BigDecimal(new Date().getTime() - startTime.getTime()).divide(new BigDecimal(1000)).toString();

		logger.info("===================================================================");
		logger.info("END TEST " + name.getMethodName());
		logger.info("Execution time: " + time + " seconds");
		logger.info("===================================================================");

	}
}
