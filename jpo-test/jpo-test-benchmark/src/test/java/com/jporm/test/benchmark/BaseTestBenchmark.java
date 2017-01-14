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
package com.jporm.test.benchmark;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.rules.TestName;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.junit4.SpringRunner;

import com.jporm.test.BaseTestAllDBConfig;
import com.jporm.test.config.DBData;

/**
 *
 * @author Francesco Cina
 *
 *         20/mag/2011
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = BaseTestAllDBConfig.class)
public abstract class BaseTestBenchmark {

	@Rule
	public final TestName testName = new TestName();

	@Resource
	private List<DBData> testDataList;
	private final List<BenchmarkData> benchmarkData = new ArrayList<>();

	@Value("${benchmark.enabled}")
	private boolean enabled;

	@Resource
	private ApplicationContext context;
	private Date startTime;
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	public List<BenchmarkData> getBenchmarkData() {
		return benchmarkData;
	}

	protected String getTestInputBasePath() {
		return "./src/test/files"; //$NON-NLS-1$
	}

	protected String getTestOutputBasePath() {
		final String output = "./target/test/files"; //$NON-NLS-1$
		mkDir(output);
		return output;
	}

	public boolean isEnabled() {
		return enabled;
	}

	protected void mkDir(final String dirPath) {
		final File path = new File(dirPath);
		if (!path.exists()) {
			path.mkdirs();
		}
	}

	@Before
	public void setUp() {

		assertNotNull(testDataList);
		assertFalse(testDataList.isEmpty());

		if (getBenchmarkData().isEmpty()) {
			for (final DBData dbData : testDataList) {
				if (dbData.isDbAvailable()) {
					getBenchmarkData().add(new BenchmarkData(dbData));
				}
			}
		}

		startTime = new Date();

		logger.info("==================================================================="); //$NON-NLS-1$
		logger.info("BEGIN TEST " + testName.getMethodName()); //$NON-NLS-1$
		logger.info("==================================================================="); //$NON-NLS-1$

	}

	@After
	public void tearDown() {

		final String time = new BigDecimal(new Date().getTime() - startTime.getTime()).divide(new BigDecimal(1000)).toString();

		logger.info("==================================================================="); //$NON-NLS-1$
		logger.info("END TEST " + testName.getMethodName()); //$NON-NLS-1$
		logger.info("Execution time: " + time + " seconds"); //$NON-NLS-1$ //$NON-NLS-2$
		logger.info("==================================================================="); //$NON-NLS-1$

	}

}
