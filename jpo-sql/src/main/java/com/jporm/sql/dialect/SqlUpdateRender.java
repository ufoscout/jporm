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
package com.jporm.sql.dialect;

import com.jporm.sql.query.processor.PropertiesProcessor;
import com.jporm.sql.query.update.UpdateImpl;

public interface SqlUpdateRender {

    public static final String WHITE_SPACE = " ";
    public static final String UPDATE = "UPDATE ";

    SqlSetRender getSetRender();
    SqlWhereRender getWhereRender();

    default void render(UpdateImpl update, StringBuilder queryBuilder, PropertiesProcessor propertiesProcessor) {
        queryBuilder.append(UPDATE);
        queryBuilder.append(update.getTableName().getTable());
        queryBuilder.append(WHITE_SPACE);

        getSetRender().render(update.getSet(), queryBuilder, propertiesProcessor);
        getWhereRender().render(update.where(), queryBuilder, propertiesProcessor);
    }

}
