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
package com.jporm.query;

/**
 * An Orm object result reader
 * @author Francesco Cina
 *
 * 15/lug/2011
 */
public interface OrmRowMapper<BEAN>  {

    /**
     * A callback method called for every bean returned by the Query
     * @param newObject
     * @param rowCount a row counter starting from 0
     */
    void read(BEAN newObject, int rowCount);

}
