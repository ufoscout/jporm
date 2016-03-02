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
package com.jporm.commons.core.inject.valuegenerator;

import com.jporm.annotation.introspector.generator.GeneratorInfo;
import com.jporm.commons.core.exception.JpoException;
import com.jporm.sql.dialect.DBProfile;
import com.jporm.sql.query.insert.values.Generator;

public interface ValueGenerator {

    public static ValueGenerator get(final Class<?> clazz, final GeneratorInfo generatorInfo, final DBProfile dbProfile) {
        Generator columnValueGenerator = Generator.noOps();
            switch (generatorInfo.getGeneratorType()) {
            case SEQUENCE:
                if (dbProfile.getDbFeatures().isSequenceSupport()) {
                    columnValueGenerator = Generator.sequence(generatorInfo.getName());
                } else {
                    throw new JpoException("Error processing class [" + clazz.getSimpleName() + "]: the [" + dbProfile.getDBName() + "] database does not support sequences");
                }
                break;
            case SEQUENCE_FALLBACK_AUTOGENERATED:
                if (dbProfile.getDbFeatures().isSequenceSupport()) {
                    columnValueGenerator = Generator.sequence(generatorInfo.getName());
                } else {
                    columnValueGenerator = Generator.auto();
                }
                break;
            case AUTOGENERATED:
                if (dbProfile.getDbFeatures().isAutogeneratedKeySupport()) {
                    columnValueGenerator = Generator.auto();
                } else {
                    throw new JpoException("Error processing class [" + clazz.getSimpleName() + "]: the [" + dbProfile.getDBName() + "] database does not support autogenerated columns");
                }
                break;
            case AUTOGENERATED_FALLBACK_SEQUENCE:
                if (dbProfile.getDbFeatures().isAutogeneratedKeySupport()) {
                    columnValueGenerator = Generator.auto();
                } else {
                    columnValueGenerator = Generator.sequence(generatorInfo.getName());
                }
                break;
            case NONE:
                break;
        }
        return new ValueGeneratorImpl(columnValueGenerator);
    }

    Generator getGenerator();

}
