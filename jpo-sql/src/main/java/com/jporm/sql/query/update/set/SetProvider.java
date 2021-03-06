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
package com.jporm.sql.query.update.set;

public interface SetProvider<SET_PROVIDER extends SetProvider<SET_PROVIDER>> {

    /**
     * Express the new value of the objects property after the execution of the
     * update.
     *
     * @param property
     * @param value
     * @return
     */
    SET_PROVIDER set(String property, Object value);

    /**
     * Express the new value of the objects property using a multi value CASE condition.
     *
     * @param property
     * @param value
     * @return
     */
    SET_PROVIDER set(String property, CaseWhen caseWhen);

}
