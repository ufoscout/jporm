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

public class GetterSetter<BEAN, P> {

    private final Setter<BEAN, P> setter;
    private final Getter<BEAN, P> getter;

    public GetterSetter(final Getter<BEAN, P> getter, final Setter<BEAN, P> setter) {
        this.getter = getter;
        this.setter = setter;
    }

    public P get(final BEAN bean) {
        return getter.getValue(bean);
    }

    public void set(final BEAN bean, final P value) {
        setter.setValue(bean, value);
    }

}
