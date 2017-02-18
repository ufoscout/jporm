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
package com.jporm.rm.spring;

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
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.PlatformTransactionManager;

import com.jporm.rm.JpoRm;
import com.jporm.rm.spring.transactional.ITransactionalExecutor;
import com.jporm.test.util.DerbyNullOutputUtil;

/**
 *
 * @author Francesco Cina
 *
 *         20/mag/2011
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = { JpoSpringTestConfig.class })
public abstract class BaseTestJdbcTemplate {

	static {
		System.setProperty("derby.stream.error.field", DerbyNullOutputUtil.NULL_DERBY_LOG);
	}

	@Rule
	public final TestName name = new TestName();

	@Resource
	public DataSource H2_DATASOURCE;
	@Resource
	public PlatformTransactionManager H2_JDBC_PLATFORM_TRANSACTION_MANAGER;
	@Resource
	private ITransactionalExecutor H2_TRANSACTIONAL_EXECUTOR;

	private Date startTime;
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	public DataSource getH2Datasource() {
		return H2_DATASOURCE;
	}

	protected PlatformTransactionManager getH2PlatformTransactionManager() {
		return H2_JDBC_PLATFORM_TRANSACTION_MANAGER;
	}

	protected ITransactionalExecutor getH2TransactionalExecutor() {
		return H2_TRANSACTIONAL_EXECUTOR;
	}

	/**
	 * @return
	 */
	public JpoRm getJPO() {
		return JpoRmJdbcTemplateBuilder.get().build(new JdbcTemplate(getH2Datasource()), getH2PlatformTransactionManager());
	}

	/**
	 * @return the logger
	 */
	public Logger getLogger() {
		return logger;
	}

	@Before
	public void setUpBeforeTest() {

		startTime = new Date();

		getLogger().info("==================================================================="); //$NON-NLS-1$
		getLogger().info("BEGIN TEST " + name.getMethodName()); //$NON-NLS-1$
		getLogger().info("==================================================================="); //$NON-NLS-1$

	}

	@After
	public void tearDownAfterTest() {

		final String time = new BigDecimal(new Date().getTime() - startTime.getTime()).divide(new BigDecimal(1000)).toString();

		getLogger().info("==================================================================="); //$NON-NLS-1$
		getLogger().info("END TEST " + name.getMethodName()); //$NON-NLS-1$
		getLogger().info("Execution time: " + time + " seconds"); //$NON-NLS-1$ //$NON-NLS-2$
		getLogger().info("==================================================================="); //$NON-NLS-1$

	}

}
