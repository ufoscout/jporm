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
package com.jporm.test;

import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map.Entry;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.rules.TestName;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.jporm.JPOrm;
import com.jporm.test.db.DBData;

/**
 *
 * @author Francesco Cina
 *
 *         20/mag/2011
 */
@RunWith(Parameterized.class)
//BaseTestAllDB
public abstract class BaseTestAllDB {

	public static ApplicationContext CONTEXT = null;

	private final String TEST_FILE_INPUT_BASE_PATH = "./src/test/files"; //$NON-NLS-1$
	private final String TEST_FILE_OUTPUT_BASE_PATH = "./target/test/files"; //$NON-NLS-1$
	private final TestData testData;

	public BaseTestAllDB(final String testName, final TestData testData) {
		this.testData = testData;
	}

	@Parameterized.Parameters(name="{0}")
	public static Collection<Object[]> generateData() {
		if (CONTEXT == null) {
			CONTEXT = new ClassPathXmlApplicationContext("spring-context.xml"); //$NON-NLS-1$
		}

		List<Object[]> parameters = new ArrayList<Object[]>();
		for ( Entry<String, DBData> dbDataEntry :  CONTEXT.getBeansOfType(DBData.class).entrySet() ) {
			DBData dbData = dbDataEntry.getValue();
			if ( dbData.isDbAvailable() ) {
				parameters.add(new Object[]{ dbData.getDBType() + "_DataSource", new TestData(dbData.getDataSourceSessionProvider(), dbData.getDataSource(), dbData.getDBType(), dbData.supportMultipleSchemas()) }); //$NON-NLS-1$
				parameters.add(new Object[]{ dbData.getDBType() + "_JdbcTemplate", new TestData(dbData.getJdbcTemplateSessionProvider(), dbData.getDataSource(), dbData.getDBType(), dbData.supportMultipleSchemas()) }); //$NON-NLS-1$

			}
		}
		return parameters;
	}

	@Rule
	public final TestName name = new TestName();
	private Date startTime;
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Before
	public void setUpBeforeTest() {

		startTime = new Date();

		getLogger().info("==================================================================="); //$NON-NLS-1$
		getLogger().info("BEGIN TEST " + name.getMethodName()); //$NON-NLS-1$
		getLogger().info("==================================================================="); //$NON-NLS-1$

	}

	@After
	public void tearDownAfterTest() {

		final String time = new BigDecimal( new Date().getTime() - startTime.getTime() ).divide(new BigDecimal(1000)).toString();

		getLogger().info("==================================================================="); //$NON-NLS-1$
		getLogger().info("END TEST " + name.getMethodName()); //$NON-NLS-1$
		getLogger().info("Execution time: " + time + " seconds"); //$NON-NLS-1$ //$NON-NLS-2$
		getLogger().info("==================================================================="); //$NON-NLS-1$

	}

	protected String getTestInputBasePath() {
		return TEST_FILE_INPUT_BASE_PATH;
	}

	protected String getTestOutputBasePath() {
		mkDir(TEST_FILE_OUTPUT_BASE_PATH);
		return TEST_FILE_OUTPUT_BASE_PATH;
	}

	protected void mkDir(final String dirPath) {
		final File path = new File(dirPath);
		if (!path.exists()) {
			path.mkdirs();
		}
	}

	protected JPOrm getJPOrm() {
		return new JPOrm(testData.getSessionProvider());
	}

	public TestData getTestData() {
		return testData;
	}

	public Logger getLogger() {
		return logger;
	}

}
