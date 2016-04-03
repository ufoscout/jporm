/*******************************************************************************
 * Copyright 2016 Francesco Cina'
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
package com.jporm.commons.core.function;

import static org.junit.Assert.assertEquals;

import java.util.concurrent.atomic.AtomicReference;

import org.junit.Test;

import com.jporm.commons.core.BaseCommonsCoreTestApi;

public class IntBiFunctionTest extends BaseCommonsCoreTestApi {

    @Test
    public void test() {

        AtomicReference<String> result = new AtomicReference<String>(null);

        IntBiFunction<Integer, String> function = (one, two) -> "-" + one + "-" + two;
        IntBiConsumer<Integer> composed = function.andThen((String text) -> result.set(text+text));

        composed.accept(1, 2);
        assertEquals("-1-2-1-2", result.get() );

        composed.accept(-321, 0);
        assertEquals("--321-0--321-0", result.get() );
    }

}
