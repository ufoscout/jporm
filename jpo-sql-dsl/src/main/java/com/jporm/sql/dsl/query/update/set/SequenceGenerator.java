/*******************************************************************************
 * Copyright 2016 Francesco Cina'
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
package com.jporm.sql.dsl.query.update.set;

import com.jporm.sql.dsl.dialect.DBProfile;

public class SequenceGenerator implements Generator {

    private final String sequenceName;

    public SequenceGenerator(String sequenceName) {
        this.sequenceName = sequenceName;
    }

    @Override
    public boolean replaceQuestionMark() {
        return true;
    }

    @Override
    public boolean hasValue() {
        return false;
    }

    @Override
    public Object getValue() {
        return null;
    }

    @Override
    public void questionMarkReplacement(StringBuilder queryBuilder, DBProfile dbProfile) {
        queryBuilder.append(dbProfile.getSqlStrategy().insertQuerySequence(sequenceName));
    }

}
