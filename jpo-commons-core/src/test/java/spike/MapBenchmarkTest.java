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
package spike;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.TreeMap;
import java.util.UUID;

import org.junit.Test;

public class MapBenchmarkTest {

	@Test
	public void testHashMapNullKey() {
		Map<Object, Object> map = new HashMap<>();

		Object value = new Object();
		map.put(null, value);

		assertEquals(value, map.get(null));

		Object value2 = new Object();

		assertEquals(value, map.put(null, value2));
		assertEquals(value2, map.get(null));
	}

	@Test
	public void testMapReadSpeed() {
		int desiredSize = 100000;
		List<String> validKeys = new ArrayList<String>();
		List<String> notValidKeys = new ArrayList<String>();
		Map<String, Object> hashMap = new HashMap<String, Object>();
		Map<String, Object> treeMap = new TreeMap<String, Object>();
		fillMaps(hashMap, treeMap, validKeys, notValidKeys, desiredSize);

		benchmark(hashMap, validKeys, notValidKeys, "HashMap[" + desiredSize + "]"); //$NON-NLS-1$ //$NON-NLS-2$
		benchmark(treeMap, validKeys, notValidKeys, "TreeMap[" + desiredSize + "]"); //$NON-NLS-1$ //$NON-NLS-2$
	}

	private void fillMaps(final Map<String, Object> hashMap, final Map<String, Object> treeMap, final List<String> validKeys, final List<String> notValidKeys, final int desiredSize) {
		for (int i=0; i<desiredSize; i++) {
			String key = UUID.randomUUID().toString();
			hashMap.put(key, new Object());
			treeMap.put(key, new Object());
			validKeys.add(key);
			notValidKeys.add(UUID.randomUUID().toString());
		}
		//Random key order
		Random rnd = new Random();
		List<String> randomList = new ArrayList<String>();
		while (validKeys.size() != 0)
		{
			int index = rnd.nextInt(validKeys.size());
			randomList.add(validKeys.get(index));
			validKeys.remove(index);
		}
		validKeys.addAll(randomList);
	}

	private void benchmark(final Map<String, Object> map, final List<String> validKeys, final List<String> notValidKeys, final String mapName) {

		long validContain = 0;
		long validGet = 0;
		long notValidContain = 0;
		long notValidGet = 0;
		long total = 0;

		// benchmark containsKey with valid keys
		long start = System.currentTimeMillis();
		for (String key : validKeys) {
			map.containsKey(key);
		}
		validContain = System.currentTimeMillis() - start;
		total+=validContain;
		System.out.println(mapName + " - containsKey with valid keys     -> " + validContain + "ms"); //$NON-NLS-1$ //$NON-NLS-2$

		//benchmark get with valid keys
		start = System.currentTimeMillis();
		for (String key : validKeys) {
			map.get(key);
		}
		validGet = System.currentTimeMillis() - start;
		total+= validGet;
		System.out.println(mapName + " - get with valid keys test        -> " + validGet + "ms"); //$NON-NLS-1$ //$NON-NLS-2$

		// benchmark containsKey with not valid keys
		start = System.currentTimeMillis();
		for (String key : notValidKeys) {
			map.containsKey(key);
		}
		notValidContain = System.currentTimeMillis() - start;
		total+=notValidContain;
		System.out.println(mapName + " - containsKey with not valid keys -> " + notValidContain + "ms"); //$NON-NLS-1$ //$NON-NLS-2$

		//benchmark get with not valid keys
		start = System.currentTimeMillis();
		for (String key : notValidKeys) {
			map.get(key);
		}
		notValidGet = System.currentTimeMillis() - start;
		total+= notValidGet;
		System.out.println(mapName + " - get with not valid keys         -> " + notValidGet + "ms"); //$NON-NLS-1$ //$NON-NLS-2$
		System.out.println(mapName + " - TOTAL TIME                      -> " + total + "ms"); //$NON-NLS-1$ //$NON-NLS-2$

	}

	@Test
	public void testSameHashCodeMap() {
		MyObj obj1 = new MyObj();
		Object value1 = new Object();
		MyObj obj2 = new MyObj();
		Object value2 = new Object();

		Map<MyObj, Object> map = new HashMap<>();
		assertNull(map.put(obj1, value1));
		assertNull(map.put(obj2, value2));

		assertEquals(value1, map.get(obj1));
		assertEquals(value2, map.get(obj2));
	}

	class MyObj {

		@Override
		public int hashCode() {
			return 1;
		}

	}
}
