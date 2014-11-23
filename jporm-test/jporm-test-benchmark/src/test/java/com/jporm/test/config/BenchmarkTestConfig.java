/*******************************************************************************
 * Copyright 2014 Francesco Cina'
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
package com.jporm.test.config;

import java.util.Properties;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.orm.hibernate3.annotation.AnnotationSessionFactoryBean;

@Configuration
@PropertySource({"file:./../jporm-test-integration/test-config/test-config.properties"})
public class BenchmarkTestConfig {

	public AnnotationSessionFactoryBean getBaseSessionFactory() {
		AnnotationSessionFactoryBean sessionFactory = new AnnotationSessionFactoryBean();
		sessionFactory.setPackagesToScan("com.jporm.test.benchmark.domain");
		sessionFactory.setConfigurationClass(org.hibernate.cfg.Configuration.class);
		Properties properties = new Properties();
		properties.put("hibernate.query.factory_class", "org.hibernate.hql.classic.ClassicQueryTranslatorFactory");
		properties.put("hibernate.connection.release_mode", "after_transaction");
		properties.put("hibernate.transaction.flush_before_completion", "true");
		sessionFactory.setHibernateProperties(properties);
		return sessionFactory;
	}

	@Bean(name="DERBY_HibernateSessionFactory")
	public AnnotationSessionFactoryBean getDerbySessionFactoryBean(@Qualifier(DerbyConfig.DATASOURCE_NAME) final DataSource dataSource) {
		AnnotationSessionFactoryBean sessionFactory = getBaseSessionFactory();
		sessionFactory.setDataSource(dataSource);
		return sessionFactory;
	}

	@Bean(name="H2_HibernateSessionFactory")
	public AnnotationSessionFactoryBean getH2SessionFactoryBean(@Qualifier(H2Config.DATASOURCE_NAME) final DataSource dataSource) {
		AnnotationSessionFactoryBean sessionFactory = getBaseSessionFactory();
		sessionFactory.setDataSource(dataSource);
		return sessionFactory;
	}

	@Bean(name="HSQLDB_HibernateSessionFactory")
	public AnnotationSessionFactoryBean getHSQLDBSessionFactoryBean(@Qualifier(HSQLDBConfig.DATASOURCE_NAME) final DataSource dataSource) {
		AnnotationSessionFactoryBean sessionFactory = getBaseSessionFactory();
		sessionFactory.setDataSource(dataSource);
		return sessionFactory;
	}


	@Bean(name="MYSQL_HibernateSessionFactory")
	public AnnotationSessionFactoryBean getMySqlSessionFactoryBean(@Qualifier(MySqlConfig.DATASOURCE_NAME) final DataSource dataSource) {
		AnnotationSessionFactoryBean sessionFactory = getBaseSessionFactory();
		sessionFactory.setDataSource(dataSource);
		return sessionFactory;
	}

	@Bean(name="ORACLE_HibernateSessionFactory")
	public AnnotationSessionFactoryBean getOracleSessionFactoryBean(@Qualifier(OracleConfig.DATASOURCE_NAME) final DataSource dataSource) {
		AnnotationSessionFactoryBean sessionFactory = getBaseSessionFactory();
		sessionFactory.setDataSource(dataSource);
		return sessionFactory;
	}

	@Bean(name="POSTGRESQL_HibernateSessionFactory")
	public AnnotationSessionFactoryBean getPostgresqlSessionFactoryBean(@Qualifier(PostgresqlConfig.DATASOURCE_NAME) final DataSource dataSource) {
		AnnotationSessionFactoryBean sessionFactory = getBaseSessionFactory();
		sessionFactory.setDataSource(dataSource);
		return sessionFactory;
	}

}
