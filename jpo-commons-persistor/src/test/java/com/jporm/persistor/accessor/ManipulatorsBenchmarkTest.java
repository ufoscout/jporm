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

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.junit.Before;
import org.junit.Test;

import com.jporm.annotation.mapper.clazz.NoOpsValueProcessor;
import com.jporm.annotation.mapper.clazz.ValueProcessor;
import com.jporm.persistor.BaseTestApi;
import com.jporm.persistor.accessor.lambda.LambdaAccessorFactory;
import com.jporm.persistor.accessor.methodhandler.MethodHandlerAccessorFactory;

public class ManipulatorsBenchmarkTest extends BaseTestApi {

	private final ValueProcessor<String, String> valueProcessor = new NoOpsValueProcessor<>();

	class TestBeanDirectGetter extends Getter<TestBean, String, String> {
		TestBeanDirectGetter() {
			super(valueProcessor);
		}
		@Override
		public String getUnProcessedValue(final TestBean bean) {
			return bean.getString();
		}
	}

	class TestBeanDirectSetter extends Setter<TestBean, String, String> {
		TestBeanDirectSetter() {
			super(valueProcessor);
		}
		@Override
		public TestBean setUnProcessedValue(final TestBean bean, final String value) {
			bean.setString(value);
			return bean;
		}
	}

	private final int attempts = 2;

	private final int warm = 10_000;
	private final int loop = 10_000_000;

	private Getter<TestBean, String, String> directGet;
	private Setter<TestBean, String, String> directSet;

	private Getter<TestBean, String, String> mhGet;
	private Setter<TestBean, String, String> mhSet;
	private Getter<TestBean, String, String> lambdaGet;
	private Setter<TestBean, String, String> lambdaSet;

	private final int randomValues = 1000;

	private String[] precalculatedTestValues;

	private final String[] testSaveValues = new String[randomValues];

	@Before
	public void setUp() throws NoSuchMethodException, SecurityException, NoSuchFieldException {
		final Method stringSetterMethod = TestBean.class.getMethod("setString", String.class); //$NON-NLS-1$
		final Method stringGetterMethod = TestBean.class.getMethod("getString"); //$NON-NLS-1$

		final AccessorFactory lambdaFactory = new LambdaAccessorFactory();
		lambdaGet = lambdaFactory.buildGetter(stringGetterMethod, valueProcessor);
		lambdaSet = lambdaFactory.buildSetter(stringSetterMethod, valueProcessor);

		final AccessorFactory mhFactory = new MethodHandlerAccessorFactory();
		mhGet = mhFactory.buildGetter(stringGetterMethod, valueProcessor);
		mhSet = mhFactory.buildSetter(stringSetterMethod, valueProcessor);

		directGet = new TestBeanDirectGetter();
		directSet = new TestBeanDirectSetter();

		final List<String> values = new ArrayList<>();
		for (int i = 0; i < randomValues; i++) {
			values.add(UUID.randomUUID().toString());
		}
		precalculatedTestValues = values.toArray(new String[0]);
	}

	@Test
	public void benchmark() {
		getLogger().info("Benchmark field access");

		testSetterGetter(warm, new TestBean(), precalculatedTestValues, testSaveValues, directGet, directSet);
		testSetterGetter(warm, new TestBean(), precalculatedTestValues, testSaveValues, mhGet, mhSet);
		testSetterGetter(warm, new TestBean(), precalculatedTestValues, testSaveValues, lambdaGet, lambdaSet);

		for (int i = 0; i < attempts; i++) {
			final long directTime = testSetterGetter(loop, new TestBean(), precalculatedTestValues, testSaveValues, directGet, directSet);
			print("DirectAccess", directTime);

			final long mhTime = testSetterGetter(loop, new TestBean(), precalculatedTestValues, testSaveValues, mhGet, mhSet);
			print("MethodHandler", mhTime);

			final long lambdaTime = testSetterGetter(loop, new TestBean(), precalculatedTestValues, testSaveValues, lambdaGet, lambdaSet);
			print("Lambda", lambdaTime);

			//			System.out.printf("direct: %.2fs, mh: %.2fs, lambda: %.2fs%n", (t1 - t0) * 1e-9, (t2 - t1) * 1e-9, (t3 - t2) * 1e-9);
		}

		getLogger().info("Benchmark field end");
	}

	private void print(String type, long time) {
		System.out.printf("" + type + ": %.2fs%n", time * 1e-9);
		//		getLogger().info("[{}] nanoseconds [{}]", type, time);
	}

	private long testSetterGetter(final int rounds, final TestBean bean, final String[] testValues, final String[] testSaveValues,
			final Getter<TestBean, String, String> getter, final Setter<TestBean, String, String> setter) {
		final int size = testValues.length;
		final long start = System.nanoTime();
		for (int i = 0; i < rounds; i++) {
			final int index = i % size;
			testSaveValues[index] = getter.getValue(bean);
			setter.setValue(bean, testValues[index]);
		}
		return System.nanoTime() - start;
	}

}
