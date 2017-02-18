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
package com.jporm.persistor.accessor;

import com.jporm.annotation.mapper.clazz.ValueProcessor;

/**
 * Get the value of a field
 *
 * @author Francesco Cina'
 *
 *         Mar 31, 2012
 */
public abstract class Getter<BEAN, R, P> {

	private final ValueProcessor<R, P> valueProcessor;

	public Getter(ValueProcessor<R, P> valueProcessor) {
		this.valueProcessor = valueProcessor;

	}

	public final P getValue(BEAN bean) {
		return valueProcessor.to(getUnProcessedValue(bean));
	}

	protected abstract R getUnProcessedValue(BEAN bean);

}
