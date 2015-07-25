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
package test;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.rules.TestName;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.math.BigDecimal;
import java.util.Date;

/**
 *
 * @author Francesco Cina
 *
 * 20/mag/2011
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes={TestConfig.class})
public abstract class TestBase {

	@Rule
	public final TestName name = new TestName();

	@Resource
	private DataSource dataSource;

	private Date startTime;

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Before
	public void setUpBeforeTest() {

		startTime = new Date();

		logger.info("===================================================================");
		logger.info("BEGIN TEST " + name.getMethodName());
		logger.info("===================================================================");

	}


	@After
	public void tearDownAfterTest() {

		final String time = new BigDecimal( new Date().getTime() - startTime.getTime() ).divide(new BigDecimal(1000)).toString();

		logger.info("===================================================================");
		logger.info("END TEST " + name.getMethodName());
		logger.info("Execution time: " + time + " seconds");
		logger.info("===================================================================");

	}

	public Logger getLogger() {
		return logger;
	}

	public DataSource getDataSource() {
		return dataSource;
	}
}

