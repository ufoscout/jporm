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
package com.jporm.sql.query.select.unions;

import com.jporm.sql.query.select.SelectCommon;

public interface UnionsProvider<UNION_PROVIDER extends UnionsProvider<UNION_PROVIDER>> extends SelectCommon {

    UNION_PROVIDER union(SelectCommon select);

    UNION_PROVIDER unionAll(SelectCommon select);

//    UNION_PROVIDER except(SelectCommon select);

//    UNION_PROVIDER intersect(SelectCommon select);

}
