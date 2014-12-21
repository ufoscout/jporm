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
package com.jporm;

import io.vertx.test.core.VertxTestBase;

import java.io.File;
import java.math.BigDecimal;
import java.util.Date;

import javax.sql.DataSource;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.rules.TestName;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.jporm.core.JPOrm;
import com.jporm.core.session.datasource.JPOrmDataSource;

/**
 *
 * @author Francesco Cina
 *
 * 20/mag/2011
 */
public abstract class BaseVertxTestApi extends VertxTestBase {

	@Rule public final TestName name = new TestName();

	protected ConfigurableApplicationContext context;
	private Date startTime;
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Before
	public void setUpBeforeTest() {

		if (context==null) {
			context = new AnnotationConfigApplicationContext(JpoVertxTestConfig.class);
			context.start();
		}

		startTime = new Date();

		logger.info("==================================================================="); //$NON-NLS-1$
		logger.info("BEGIN TEST " + name.getMethodName()); //$NON-NLS-1$
		logger.info("==================================================================="); //$NON-NLS-1$

	}


	@After
	public void tearDownAfterTest() {

		final String time = new BigDecimal( new Date().getTime() - startTime.getTime() ).divide(new BigDecimal(1000)).toString();

		logger.info("==================================================================="); //$NON-NLS-1$
		logger.info("END TEST " + name.getMethodName()); //$NON-NLS-1$
		logger.info("Execution time: " + time + " seconds"); //$NON-NLS-1$ //$NON-NLS-2$
		logger.info("==================================================================="); //$NON-NLS-1$

	}

	protected void mkDir( final String dirPath ) {
		final File path = new File(dirPath);
		if (!path.exists()) {
			path.mkdirs();
		}
	}

	protected JPOrm getJPO() {
		return new JPOrmDataSource(getDataSource());
	}

	protected DataSource getDataSource() {
		return context.getBean(DataSource.class);
	}

	public Logger getLogger() {
		return logger;
	}

}

