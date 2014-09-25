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
package com.jporm.core.query.clause.order;

/**
 * 
 * @author Francesco Cina
 *
 * 24/giu/2011
 */
public enum OrderByType {

	ASC("ASC ", ""), ASC_NULLS_FIRST("ASC ", "NULLS FIRST "), ASC_NULLS_LAST("ASC ",  "NULLS LAST "), //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$
	DESC("DESC ", ""), DESC_NULLS_FIRST("DESC ", "NULLS FIRST "), DESC_NULLS_LAST("DESC ",  "NULLS LAST "); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$

	private final String type;
	private final String nulls;

	OrderByType(String type, String nulls) {
		this.type = type;
		this.nulls = nulls;
	}


	public String getType() {
		return type;
	}

	public String getNulls() {
		return nulls;
	}

}
