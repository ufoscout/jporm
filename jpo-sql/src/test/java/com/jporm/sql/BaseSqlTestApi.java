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
package com.jporm.sql;

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
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.jporm.sql.SqlDsl;
import com.jporm.sql.dialect.DBProfile;
import com.jporm.sql.dialect.DBType;
import com.jporm.sql.dialect.h2.H2DBProfile;
import com.jporm.test.util.DerbyNullOutputUtil;

/**
 *
 * @author Francesco Cina
 *
 *         20/mag/2011
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { JpoSqlSqlTestConfig.class })
public abstract class BaseSqlTestApi {

    static {
        System.setProperty("derby.stream.error.field", DerbyNullOutputUtil.NULL_DERBY_LOG);
    }

    @Rule
    public final TestName name = new TestName();

    @Resource
    private DataSource H2_DATASOURCE;

    private Date startTime;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    protected DataSource getH2DataSource() {
        return H2_DATASOURCE;
    }

    protected DBProfile getH2DDProfile() {
        return new H2DBProfile();
    }

    protected JdbcTemplate getJdbcTemplate() {
        return new JdbcTemplate(getH2DataSource());
    }

    protected Logger getLogger() {
        return logger;
    }

    protected SqlDsl<String> dsl() {
        return dsl(DBType.H2.getDBProfile());
    }

    protected SqlDsl<String> dsl(DBProfile profile) {
        return SqlDsl.get(profile);
    }

    @Before
    public void setUpBeforeTest() {

        startTime = new Date();

        logger.info("==================================================================="); //$NON-NLS-1$
        logger.info("BEGIN TEST " + name.getMethodName()); //$NON-NLS-1$
        logger.info("==================================================================="); //$NON-NLS-1$

    }

    @After
    public void tearDownAfterTest() {

        final String time = new BigDecimal(new Date().getTime() - startTime.getTime()).divide(new BigDecimal(1000)).toString();

        logger.info("==================================================================="); //$NON-NLS-1$
        logger.info("END TEST " + name.getMethodName()); //$NON-NLS-1$
        logger.info("Execution time: " + time + " seconds"); //$NON-NLS-1$ //$NON-NLS-2$
        logger.info("==================================================================="); //$NON-NLS-1$

    }
}
