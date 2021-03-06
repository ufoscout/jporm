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

public class OptionalValueProcessor<VALUE> implements ValueProcessor<Optional<VALUE>, VALUE>{

	private static OptionalValueProcessor<?> DEFAULT_PROCESSOR = new OptionalValueProcessor<>();

	public static <VALUE> OptionalValueProcessor<VALUE> build() {
		return (OptionalValueProcessor<VALUE>) DEFAULT_PROCESSOR;
	}

	private OptionalValueProcessor() {

	}

	@Override
	public VALUE to(Optional<VALUE> from) {
		if (from!=null && from.isPresent()) {
			return from.get();
		}
		return null;
	}

	@Override
	public Optional<VALUE> from(VALUE to) {
		return Optional.ofNullable(to);
	}

}
