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
package com.jporm.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 *
 * Specify that the annotated field has to be managed as a JSON object.
 *
 * @author Francesco Cina
 *
 *         08/giu/2011
 */

@Target(value = {ElementType.FIELD, ElementType.METHOD})
@Retention(value = RetentionPolicy.RUNTIME)
@Inherited
public @interface JsonType {

	/**
	 * Whether a deep copy of the field is required when the bean is cloned. By default and for safety reasons this is set to true; however,
	 * deep copy is an heavy operation and in many situations it is better to avoid it. For example, if the field is an immutable object,
	 * the deepCopy can be safely set to false.
	 *
	 * @return
	 */
	boolean deepCopy() default true;

}
