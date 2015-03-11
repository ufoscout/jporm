/*******************************************************************************
 * Copyright 2015 Francesco Cina'
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
package com.jporm.core.query;

import com.jporm.core.inject.ServiceCatalog;
import com.jporm.sql.query.clause.Delete;
import com.jporm.sql.query.clause.Insert;
import com.jporm.sql.query.clause.Select;
import com.jporm.sql.query.clause.Update;
import com.jporm.sql.query.clause.impl.DeleteImpl;
import com.jporm.sql.query.clause.impl.InsertImpl;
import com.jporm.sql.query.clause.impl.SelectImpl;
import com.jporm.sql.query.clause.impl.UpdateImpl;

public interface SqlFactory {

	public static <BEAN> Delete delete(ServiceCatalog catalog, Class<BEAN> clazz) {
		return new DeleteImpl<BEAN>(catalog.getDbProfile(), catalog.getClassToolMap(), catalog.getPropertiesFactory(), clazz);
	}

	public static <BEAN> Select select(ServiceCatalog catalog, Class<BEAN> clazz) {
		return new SelectImpl<BEAN>(catalog.getDbProfile(), catalog.getClassToolMap(), catalog.getPropertiesFactory(), clazz);
	}

	public static <BEAN> Select select(ServiceCatalog catalog, Class<BEAN> clazz, String alias) {
		return new SelectImpl<BEAN>(catalog.getDbProfile(), catalog.getClassToolMap(), catalog.getPropertiesFactory(), clazz, alias);
	}

	public static <BEAN> Update update(ServiceCatalog catalog, Class<BEAN> clazz) {
		return new UpdateImpl<BEAN>(catalog.getDbProfile(), catalog.getClassToolMap(), catalog.getPropertiesFactory(), clazz);
	}

	public static <BEAN> Insert insert(ServiceCatalog catalog, Class<BEAN> clazz) {
		return new InsertImpl<BEAN>(catalog.getDbProfile(), catalog.getClassToolMap(), catalog.getPropertiesFactory(), clazz);
	}

}
