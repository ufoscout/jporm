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

import com.jporm.sql.query.delete.DeleteImpl;
import com.jporm.sql.query.processor.PropertiesProcessor;

public interface SqlDeleteRender {

    public static final String WHITE_SPACE = " ";
    public static final String DELETE_FROM = "DELETE FROM ";

    SqlWhereRender getWhereRender();

    default void render(DeleteImpl delete, StringBuilder queryBuilder, PropertiesProcessor propertiesProcessor) {
        queryBuilder.append(DELETE_FROM);
        queryBuilder.append(delete.getTableName().getTable());
        queryBuilder.append(WHITE_SPACE);
        getWhereRender().render(delete.where(), queryBuilder, propertiesProcessor);
    }

}
