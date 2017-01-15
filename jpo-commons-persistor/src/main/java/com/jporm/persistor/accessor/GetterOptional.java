/*******************************************************************************
 * Copyright 2017 Francesco Cina'
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
package com.jporm.persistor.accessor;

import java.util.Optional;

public class GetterOptional<BEAN, P> implements Getter<BEAN, P> {

	private final Getter<BEAN, Optional<P>> getter;

	public GetterOptional(Getter<BEAN, Optional<P>> getter) {
		this.getter = getter;
	}

	@Override
	public P getValue(BEAN bean) {
		final Optional<P> optional = getter.getValue(bean);
		if (optional!=null && optional.isPresent()) {
			return optional.get();
		}
		return null;
	}

	/**
	 * @return the getter
	 */
	public Getter<BEAN, Optional<P>> getInnerGetter() {
		return getter;
	}

}
