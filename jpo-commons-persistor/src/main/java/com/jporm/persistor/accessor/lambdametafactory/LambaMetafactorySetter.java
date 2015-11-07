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
package com.jporm.persistor.accessor.lambdametafactory;

import java.lang.invoke.CallSite;
import java.lang.invoke.LambdaMetafactory;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.function.BiConsumer;

import com.jporm.persistor.accessor.Setter;

/**
 *
 * Get the value of a field using the related getter method
 *
 * @author Francesco Cina'
 *
 *         Mar 31, 2012
 */
public class LambaMetafactorySetter<BEAN, P> implements Setter<BEAN, P> {

    private BiConsumer<BEAN, P> consumer;

    public LambaMetafactorySetter(final Field field) {
        try {
            field.setAccessible(true);
            MethodHandles.Lookup caller = MethodHandles.lookup();
            build(caller, caller.unreflectSetter(field));
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public LambaMetafactorySetter(final Method setterMethod) {
        try {
            setterMethod.setAccessible(true);
            MethodHandles.Lookup caller = MethodHandles.lookup();
            build(caller, caller.unreflect(setterMethod));
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    private void build(final MethodHandles.Lookup caller, final MethodHandle methodHandle) {
        try {
            MethodType func = methodHandle.type();
            CallSite site = LambdaMetafactory.metafactory(caller, "accept", MethodType.methodType(BiConsumer.class),
                    MethodType.methodType(Void.TYPE, Object.class, Object.class), methodHandle, MethodType.methodType(Void.TYPE, func.parameterArray()));

            MethodHandle factory = site.getTarget();
            consumer = (BiConsumer<BEAN, P>) factory.invoke();

        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void setValue(final BEAN bean, final P value) {
        try {
            consumer.accept(bean, value);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

}
