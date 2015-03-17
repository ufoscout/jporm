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
package com.jporm.validator;

import java.util.Collection;

/**
 *
 * @author ufo
 *
 */
public interface ValidatorService {

	/**
	 * Validate the bean. If there are validation errors a specific {@link RuntimeException} is thrown.
	 * The type of exception depends on the validator implementation.
	 *
	 */
	<T> void validateThrowException(T data);

	/**
	 * Validate the bean. If there are validation errors a specific {@link RuntimeException} is thrown.
	 * The type of exception depends on the validator implementation.
	 *
	 */
	<T> void validateThrowException(Collection<T> data);

}