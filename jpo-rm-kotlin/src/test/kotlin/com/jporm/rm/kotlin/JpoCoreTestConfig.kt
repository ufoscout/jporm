/*******************************************************************************
 * Copyright 2014 Francesco Cina'

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

import javax.sql.DataSource

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.env.Environment

import com.jporm.test.TestConstants
import com.zaxxer.hikari.HikariDataSource

import liquibase.integration.spring.SpringLiquibase

@Configuration
open class JpoCoreTestConfig {

    @Bean
    open fun getH2DataSource(env: Environment): DataSource {
        val dataSource = HikariDataSource()
        dataSource.driverClassName = env.getProperty("H2.jdbc.driverClassName")
        dataSource.jdbcUrl = env.getProperty("H2.jdbc.url")
        dataSource.username = env.getProperty("H2.jdbc.username")
        dataSource.password = env.getProperty("H2.jdbc.password")
        dataSource.isAutoCommit = false
        return dataSource
    }

    @Bean
    open fun getSpringLiquibase(dataSource: DataSource): SpringLiquibase {
        val liquibase = SpringLiquibase()
        liquibase.dataSource = dataSource
        liquibase.changeLog = TestConstants.LIQUIBASE_FILE
        // liquibase.setContexts("development, production");
        return liquibase
    }

}
