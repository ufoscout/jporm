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
package com.jporm.rm.query.save;

import com.jporm.commons.core.query.Query;
import com.jporm.commons.core.query.RenderableQuery;
import com.jporm.commons.core.query.save.CommonSaveQuery;

/**
 *
 * @author Francesco Cina
 *
 *         10/lug/2011
 */
public interface CustomSaveQuery extends RenderableQuery, Query, CommonSaveQuery<CustomSaveQuery> {

    /**
     * Perform the insert and return the number of affected rows.
     * 
     * @return
     */
    int execute();

}
