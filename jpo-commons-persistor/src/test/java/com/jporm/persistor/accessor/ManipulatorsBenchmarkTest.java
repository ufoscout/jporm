/*******************************************************************************
 * Copyright 2014 Francesco Cina'
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

import com.jporm.persistor.BaseTestApi;
import com.jporm.persistor.accessor.lambdametafactory.LambaMetafactoryAccessorFactory;
import com.jporm.persistor.accessor.methodhandler.MethodHandlerAccessorFactory;
import com.jporm.persistor.accessor.reflection.ReflectionAccessorFactory;

public class ManipulatorsBenchmarkTest extends BaseTestApi {

    class TestBeanDirectGetter implements Getter<TestBean, String> {
        @Override
        public String getValue(final TestBean bean) {
            return bean.getString();
        }
    }

    class TestBeanDirectSetter implements Setter<TestBean, String> {
        @Override
        public void setValue(final TestBean bean, final String value) {
            bean.setString(value);
        }
    }

    private int attempts = 1;

    private int warm = 10_000;
    private int loop = 10_000_000;
    private GetterSetter<TestBean, String> directGetSet;
    private GetterSetter<TestBean, String> reflectionGetSet;

    private GetterSetter<TestBean, String> mhGetSet;
    private GetterSetter<TestBean, String> lambdaGetSet;
    private int randomValues = 1000;

    private String[] precalculatedTestValues;

    private String[] testSaveValues = new String[randomValues];

    @Before
    public void setUp() throws NoSuchMethodException, SecurityException, NoSuchFieldException {
        Method stringSetterMethod = TestBean.class.getMethod("setString", String.class); //$NON-NLS-1$
        Method stringGetterMethod = TestBean.class.getMethod("getString"); //$NON-NLS-1$

        AccessorFactory lambdaFactory = new LambaMetafactoryAccessorFactory();
        lambdaGetSet = lambdaFactory.build(stringGetterMethod, stringSetterMethod);

        AccessorFactory mhFactory = new MethodHandlerAccessorFactory();
        mhGetSet = mhFactory.build(stringGetterMethod, stringSetterMethod);

        AccessorFactory reflectionFactory = new ReflectionAccessorFactory();
        reflectionGetSet = reflectionFactory.build(stringGetterMethod, stringSetterMethod);

        directGetSet = new GetterSetter<>(new TestBeanDirectGetter(), new TestBeanDirectSetter());

        List<String> values = new ArrayList<>();
        for (int i = 0; i < randomValues; i++) {
            values.add(UUID.randomUUID().toString());
        }
        precalculatedTestValues = values.toArray(new String[0]);
    }

    @Test
    public void testMethod() {
        getLogger().info("Benchmark field access");

        TestBean directBean = new TestBean();
        testSetterGetter(loop, directBean, precalculatedTestValues, testSaveValues, directGetSet);

        TestBean reflectionBean = new TestBean();
        testSetterGetter(warm, reflectionBean, precalculatedTestValues, testSaveValues, reflectionGetSet);

        TestBean mhBean = new TestBean();
        testSetterGetter(warm, mhBean, precalculatedTestValues, testSaveValues, mhGetSet);

        TestBean lambdaBean = new TestBean();
        testSetterGetter(warm, lambdaBean, precalculatedTestValues, testSaveValues, lambdaGetSet);

        for (int i = 0; i < attempts; i++) {
            long t0 = System.nanoTime();
            testSetterGetter(loop, directBean, precalculatedTestValues, testSaveValues, directGetSet);
            long t1 = System.nanoTime();
            testSetterGetter(loop, reflectionBean, precalculatedTestValues, testSaveValues, reflectionGetSet);
            long t2 = System.nanoTime();
            testSetterGetter(loop, mhBean, precalculatedTestValues, testSaveValues, mhGetSet);
            long t3 = System.nanoTime();
            testSetterGetter(loop, lambdaBean, precalculatedTestValues, testSaveValues, lambdaGetSet);
            long t4 = System.nanoTime();
            System.out.printf("direct: %.2fs, reflection: %.2fs, mh: %.2fs, lambda: %.2fs%n", (t1 - t0) * 1e-9, (t2 - t1) * 1e-9, (t3 - t2) * 1e-9,
                    (t4 - t3) * 1e-9);
        }

        getLogger().info("Benchmark field end");
    }

    private String testSetterGetter(final int rounds, final TestBean bean, final String[] testValues, final String[] testSaveValues,
            final GetterSetter<TestBean, String> getterSetter) {
        int size = testValues.length;
        for (int i = 0; i < rounds; i++) {
            int index = i % size;
            testSaveValues[index] = getterSetter.get(bean);
            getterSetter.set(bean, testValues[index]);
        }
        return testValues[0];
    }
}
