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
package com.jporm.persistor.generator;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import com.jporm.annotation.exception.JpoWrongAnnotationException;

/**
 *
 * @author ufo
 *
 */
public class ValueCheckerFactory {

	private ValueCheckerFactory() {}

	private static Map<Class<?>, ValueChecker<?>> VALUE_CHECKERS = new HashMap<Class<?>, ValueChecker<?>>();

	static {
		addValueChecker(BigDecimal.class , new BigDecimalValueChecker());
		addValueChecker(Byte.class , new ByteValueChecker());
		addValueChecker(Byte.TYPE , new ByteValueChecker());
		addValueChecker(Integer.class , new IntegerValueChecker());
		addValueChecker(Integer.TYPE , new IntegerValueChecker());
		addValueChecker(Long.class , new LongValueChecker());
		addValueChecker(Long.TYPE , new LongValueChecker());
		addValueChecker(Short.class , new ShortValueChecker());
		addValueChecker(Short.TYPE , new ShortValueChecker());
		addValueChecker(String.class , new StringValueChecker());
	}

	private static <P> void addValueChecker(final Class<P> clazz , final ValueChecker<P> valueChecker) {
		VALUE_CHECKERS.put(clazz, valueChecker);
	}

	public static <P> ValueChecker<P> getValueChecker(final Class<P> clazz) {
		if (VALUE_CHECKERS.containsKey(clazz)) {
			return (ValueChecker<P>) VALUE_CHECKERS.get(clazz);
		}
		throw new JpoWrongAnnotationException("Cannot use type " + clazz + " for a field annotated as Generated. Valid classes are [" + Arrays.toString( VALUE_CHECKERS.keySet().toArray() ) + "]" ); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
	}

}
