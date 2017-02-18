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
package com.jporm.annotation.mapper.clazz;

import java.util.Optional;

public class PropertyWrapperDefault<T, R, P> implements PropertyWrapper<T, R, P> {

	private final Optional<T> accessor;
	private final ValueProcessor<R, P> valueProcessor;

	public PropertyWrapperDefault(Optional<T> accessor, ValueProcessor<R, P> valueProcessor) {
		this.accessor = accessor;
		this.valueProcessor = valueProcessor;
	}

	@Override
	public Optional<T> getAccessor() {
		return accessor;
	}

	@Override
	public ValueProcessor<R, P> getProcessor() {
		return valueProcessor;
	}

}
