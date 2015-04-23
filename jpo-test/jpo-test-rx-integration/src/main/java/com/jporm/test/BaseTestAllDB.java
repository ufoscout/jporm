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

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map.Entry;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

import net.jodah.concurrentunit.ConcurrentTestCase;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.rules.TestName;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.jporm.rx.JpoRX;
import com.jporm.rx.JpoRxBuilder;
import com.jporm.rx.core.session.Session;
import com.jporm.test.config.DBData;

/**
 *
 * @author Francesco Cina
 *
 *         20/mag/2011
 */
@RunWith(Parameterized.class)
//BaseTestAllDB
public abstract class BaseTestAllDB extends ConcurrentTestCase {

	public static ApplicationContext CONTEXT = null;

	private final TestData testData;

	public BaseTestAllDB(final String testName, final TestData testData) {
		this.testData = testData;
	}

	@Parameterized.Parameters(name="{0}")
	public static Collection<Object[]> generateData() {
		if (CONTEXT == null) {
			CONTEXT = new AnnotationConfigApplicationContext(BaseTestAllDBConfig.class);
		}

		List<Object[]> parameters = new ArrayList<Object[]>();
		for ( Entry<String, DBData> dbDataEntry :  CONTEXT.getBeansOfType(DBData.class).entrySet() ) {
			DBData dbData = dbDataEntry.getValue();
			if ( dbData.isDbAvailable() ) {
				parameters.add(new Object[]{ dbData.getDescription(), new TestData(dbData.getConnectionProvider(), dbData.getDataSource(), dbData.getDBType(), dbData.isMultipleSchemaSupport()) });
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

	protected JpoRX getJPO() {
		return new JpoRxBuilder().build(testData.getConnectionProvider());
	}

	public TestData getTestData() {
		return testData;
	}

	public Logger getLogger() {
		return logger;
	}

	@Override
	protected void await() {
	    try {
			super.await(2500, 1);
		} catch (Throwable e) {
			throw new RuntimeException(e);
		}
	  }

	protected <T> void transaction(boolean shouldFail, Function<Session, CompletableFuture<T>> txSession) {
		getJPO().transaction().execute(txSession)
		.handle((fn, ex) -> {
			if (ex!=null) {
				getLogger().info("Exception thrown during test: {}", ex);
				if (!shouldFail) {
					threadFail(ex);
				}
			}
			resume();
			return null;
		});
		await();
	}

	protected <T> void transaction(Function<Session, CompletableFuture<T>> session) {
		transaction(false, session);
	}
}
