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
/* ----------------------------------------------------------------------------
 *     PROJECT : JPOrm
 *
 *  CREATED BY : Francesco Cina'
 *          ON : Feb 28, 2013
 * ----------------------------------------------------------------------------
 */
package com.jporm.deprecated.core.mapper;

import com.jporm.core.persistor.OrmPersistor;
import com.jporm.deprecated.core.mapper.clazz.ClassMap;
import com.jporm.deprecated.core.mapper.clazz.ClassMapImpl;
import com.jporm.introspector.annotation.cache.CacheInfoImpl;
import com.jporm.introspector.annotation.table.TableInfo;

/**
 * <class_description>
 * <p><b>notes</b>:
 * <p>ON : Feb 28, 2013
 *
 * @author Francesco Cina'
 * @version $Revision
 */
public class NullOrmClassTool<T> implements OrmClassTool<T> {

	@Override
	public ClassMap<T> getClassMap() {
		return new ClassMapImpl<T>(null, new TableInfo("", ""), new CacheInfoImpl(false, ""));
	}

	@Override
	public OrmPersistor<T> getOrmPersistor() {
		return null;
	}

}
