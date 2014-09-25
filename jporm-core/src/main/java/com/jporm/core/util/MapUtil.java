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
package com.jporm.core.util;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

/**
 * 
 * @author ufo
 *
 */
public abstract class MapUtil {

	public static String keysToString(final Map<?, ?> map) {
		Iterator<?> iterable = map.entrySet().iterator();
		StringBuilder keys = new StringBuilder();
		while (iterable.hasNext()) {
			keys.append( ((Entry<?,?>) iterable.next()).getKey().toString() );
			if (iterable.hasNext()) {
				keys.append( ", " ); //$NON-NLS-1$
			}
		}
		return keys.toString();
	}
}
