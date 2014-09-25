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
package com.jporm.query.clause.from;

import com.jporm.mapper.ServiceCatalog;

/**
 * 
 * @author Francesco Cina
 *
 * 27/giu/2011
 */
public class NaturalJoinElement extends AFromElement {

	public NaturalJoinElement(final ServiceCatalog serviceCatalog, final Class<?> joinClass, final Integer nameSolverClassId) {
		super(serviceCatalog, joinClass, nameSolverClassId);
	}

	@Override
	protected String getJoinName() {
		return "NATURAL JOIN "; //$NON-NLS-1$
	}

	@Override
	protected boolean hasOnClause() {
		return false;
	}

	@Override
	protected String onLeftProperty() {
		return ""; //$NON-NLS-1$
	}

	@Override
	protected String onRightProperty() {
		return ""; //$NON-NLS-1$
	}

}
