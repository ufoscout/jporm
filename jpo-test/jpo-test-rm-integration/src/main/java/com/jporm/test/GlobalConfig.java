/*******************************************************************************
 * Copyright 2015 Francesco Cina'
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

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class GlobalConfig {

    @Value("${benchmark.enabled}")
    boolean isBenchmarkEnabled;

    @Value("${test.dataSource.enabled}")
    boolean isDataSourceEnabled;

    @Value("${test.jdbcTemplate.enabled}")
    boolean isJdbcTemplateEnabled;

    @Value("${test.quasar.enabled}")
    boolean isQuasarEnabled;

    public boolean isBenchmarkEnabled() {
        return isBenchmarkEnabled;
    }

    public boolean isDataSourceEnabled() {
        return isDataSourceEnabled;
    }

    public boolean isJdbcTemplateEnabled() {
        return isJdbcTemplateEnabled;
    }

    public boolean isQuasarEnabled() {
        return isQuasarEnabled;
    }

}
