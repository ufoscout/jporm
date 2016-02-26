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
package com.jporm.sql.query.clause.impl.value;

import com.jporm.annotation.mapper.clazz.FieldDescriptor;
import com.jporm.sql.dsl.dialect.DBProfile;

/**
 *
 * @author Francesco Cina
 *
 *         13/giu/2011
 */
public class ColumnValueGeneratorFactory {

    public static <BEAN> AColumnValueGenerator getColumnValueGenerator(final FieldDescriptor<BEAN, ? extends Object> classField, final DBProfile dbProfile,
            final boolean ignoreGenerator) {
        AColumnValueGenerator columnValueGenerator = new NullColumnValueGenerator(classField.getGeneratorInfo().getName(), dbProfile);
        if (!ignoreGenerator) {

            switch (classField.getGeneratorInfo().getGeneratorType()) {
            case SEQUENCE:
                columnValueGenerator = new SequenceColumnValueGenerator(classField.getGeneratorInfo().getName(), dbProfile);
                break;
            case SEQUENCE_FALLBACK_AUTOGENERATED:
                if (dbProfile.getDbFeatures().isSequenceSupport()) {
                    columnValueGenerator = new SequenceColumnValueGenerator(classField.getGeneratorInfo().getName(), dbProfile);
                } else {
                    columnValueGenerator = new AutogeneratedColumnValueGenerator(classField.getGeneratorInfo().getName(), dbProfile);
                }
                break;
            case AUTOGENERATED:
                columnValueGenerator = new AutogeneratedColumnValueGenerator(classField.getGeneratorInfo().getName(), dbProfile);
                break;
            case AUTOGENERATED_FALLBACK_SEQUENCE:
                if (dbProfile.getDbFeatures().isAutogeneratedKeySupport()) {
                    columnValueGenerator = new AutogeneratedColumnValueGenerator(classField.getGeneratorInfo().getName(), dbProfile);
                } else {
                    columnValueGenerator = new SequenceColumnValueGenerator(classField.getGeneratorInfo().getName(), dbProfile);
                }
                break;
            case UUID:
                break;
            case NONE:
                break;
            }
        }
        columnValueGenerator.setGeneratedColumnName(classField.getColumnInfo().getDBColumnName());
        return columnValueGenerator;
    }

    private ColumnValueGeneratorFactory() {
    }
}
