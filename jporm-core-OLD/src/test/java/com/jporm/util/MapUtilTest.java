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
package com.jporm.util;

import static org.junit.Assert.assertEquals;

import java.util.LinkedHashMap;
import java.util.Map;

import org.junit.Test;

import com.jporm.BaseTestApi;
import com.jporm.util.MapUtil;

/**
 * 
 * @author ufo
 *
 */
public class MapUtilTest extends BaseTestApi {


	@Test
	public void test() {
		Map<Class<?>, Object> map = new LinkedHashMap<Class<?>, Object>();
		map.put(String.class, null);
		map.put(Integer.class, null);
		map.put(Long.class, null);

		assertEquals( "class java.lang.String, class java.lang.Integer, class java.lang.Long" , MapUtil.keysToString(map) ); //$NON-NLS-1$

	}

}
