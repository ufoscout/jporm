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
package spike;

import static org.junit.Assert.assertEquals;

import java.lang.invoke.CallSite;
import java.lang.invoke.LambdaMetafactory;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Supplier;

import org.junit.Test;

@SuppressWarnings("rawtypes")
public class LambdaBeanAccessAtRuntimeTest {

    @SuppressWarnings("unused")
    private static class SimpleBean {
        private static Object STATIC_OBJECT = "myCustomStaticObject";

        public static Object getStaticObj() {
            return STATIC_OBJECT;
        }

        public static void setStaticObj(final Object obj) {
            STATIC_OBJECT = obj;
        }

        private final Long privateLong = 12345l;
        private final Integer integer = Integer.valueOf(0);
        private String value = "stringValue";

        private final int intPrimitive = 98765;

        private Object obj = "myCustomObject";

        public Integer getInteger() {
            return integer;
        }

        public int getIntPrimitive() {
            return intPrimitive;
        }

        public Object getObj() {
            return obj;
        }

        public String getValue() {
            return value;
        }

        public void setObj(final Object obj) {
            this.obj = obj;
        }

        public void setValue(final String value) {
            this.value = value;
        }
    }

    @Test
    public void accessInstanceMethodWithFunction() throws Throwable {

        SimpleBean simpleBeanInstance = new SimpleBean();

        MethodHandles.Lookup caller = MethodHandles.lookup();
        MethodType getter = MethodType.methodType(Object.class);

        MethodHandle target = caller.findVirtual(SimpleBean.class, "getObj", getter);
        MethodType func = target.type();
        System.out.println(func);
        System.out.println(func.generic());
        CallSite site = LambdaMetafactory.metafactory(caller, "apply", MethodType.methodType(Function.class), func.generic(), target, func);

        MethodHandle factory = site.getTarget();
        Function r = (Function) factory.invoke();
        assertEquals("myCustomObject", r.apply(simpleBeanInstance));

    }

    @Test
    public void accessInstancePrivateField() throws Throwable {
        MethodHandles.Lookup caller = MethodHandles.lookup();
        Field reflected = SimpleBean.class.getDeclaredField("privateLong");
        reflected.setAccessible(true);
        MethodHandle methodHandle = caller.unreflectGetter(reflected);

        assertEquals(12345l, methodHandle.invoke(new SimpleBean()));

        // Not possible to handle private methods with LambdaMetafactory

    }

    @Test
    public void accessObjectInstanceMethod() throws Throwable {

        SimpleBean simpleBeanInstance = new SimpleBean();

        MethodHandles.Lookup caller = MethodHandles.lookup();

        Method reflected = SimpleBean.class.getDeclaredMethod("getObj");
        MethodHandle methodHandle = caller.unreflect(reflected);

        CallSite site = LambdaMetafactory.metafactory(caller, "get", MethodType.methodType(Supplier.class, SimpleBean.class),
                MethodType.methodType(Object.class), methodHandle, MethodType.methodType(Object.class));

        MethodHandle factory = site.getTarget();
        factory = factory.bindTo(simpleBeanInstance);
        Supplier r = (Supplier) factory.invoke();
        assertEquals(simpleBeanInstance.getObj(), r.get());
    }

    @Test
    public void accessPrimitiveInstanceMethod() throws Throwable {

        SimpleBean simpleBeanInstance = new SimpleBean();

        MethodHandles.Lookup caller = MethodHandles.lookup();

        Method reflected = SimpleBean.class.getDeclaredMethod("getIntPrimitive");
        MethodHandle methodHandle = caller.unreflect(reflected);

        CallSite site = LambdaMetafactory.metafactory(caller, "get", MethodType.methodType(Supplier.class, SimpleBean.class),
                MethodType.methodType(Object.class), methodHandle, MethodType.methodType(int.class));

        MethodHandle factory = site.getTarget();
        factory = factory.bindTo(simpleBeanInstance);
        Supplier r = (Supplier) factory.invoke();
        assertEquals(simpleBeanInstance.getIntPrimitive(), r.get());

    }

    @Test
    public void accessStaticMethod() throws Throwable {
        MethodHandles.Lookup caller = MethodHandles.lookup();
        Method reflected = SimpleBean.class.getDeclaredMethod("getStaticObj");
        MethodHandle methodHandle = caller.unreflect(reflected);
        CallSite site = LambdaMetafactory.metafactory(caller, "get", MethodType.methodType(Supplier.class), MethodType.methodType(Object.class), methodHandle,
                MethodType.methodType(Object.class));
        MethodHandle factory = site.getTarget();
        Supplier r = (Supplier) factory.invoke();
        assertEquals(SimpleBean.getStaticObj(), r.get());
    }

    @Test
    public void accessStringInstanceMethod() throws Throwable {

        SimpleBean simpleBeanInstance = new SimpleBean();

        MethodHandles.Lookup caller = MethodHandles.lookup();

        Method reflected = SimpleBean.class.getDeclaredMethod("getValue");
        MethodHandle methodHandle = caller.unreflect(reflected);

        CallSite site = LambdaMetafactory.metafactory(caller, "get", MethodType.methodType(Supplier.class, SimpleBean.class),
                MethodType.methodType(Object.class), methodHandle, MethodType.methodType(String.class));

        MethodHandle factory = site.getTarget();
        factory = factory.bindTo(simpleBeanInstance);
        Supplier r = (Supplier) factory.invoke();
        assertEquals(simpleBeanInstance.getValue(), r.get());

    }

    @Test
    public void accessStringInstanceSetterMethodAttemptTwo() throws Throwable {

        SimpleBean simpleBeanInstance = new SimpleBean();

        MethodHandles.Lookup caller = MethodHandles.lookup();

        MethodType setter = MethodType.methodType(Void.TYPE, Object.class);
        MethodHandle target = caller.findVirtual(SimpleBean.class, "setObj", setter);

        // target.invoke(simpleBeanInstance, "newStringValue");
        // assertEquals( "newStringValue" , simpleBeanInstance.getObj() );

        MethodType func = target.type(); // MethodType.methodType(Void.TYPE,
                                         // SimpleBean.class, String.class),
        System.out.println(func);
        System.out.println(func.generic());
        System.out.println(MethodType.methodType(Void.TYPE, SimpleBean.class, Object.class));

        CallSite site = LambdaMetafactory.metafactory(caller, "accept", MethodType.methodType(BiConsumer.class),
                MethodType.methodType(Void.TYPE, Object.class, Object.class), target, func);

        MethodHandle factory = site.getTarget();
        BiConsumer r = (BiConsumer) factory.invoke();
        r.accept(simpleBeanInstance, "newCustomObject");

        assertEquals("newCustomObject", simpleBeanInstance.getObj());

    }
}
