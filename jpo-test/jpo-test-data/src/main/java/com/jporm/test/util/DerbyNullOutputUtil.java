/*******************************************************************************
 * Copyright 2015 Francesco Cina'
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
package com.jporm.test.util;

import java.io.OutputStream;

public class DerbyNullOutputUtil {

    public static final String NULL_DERBY_LOG = DerbyNullOutputUtil.class.getName() + ".DEV_NULL";

    public static final OutputStream DEV_NULL = new OutputStream() {
        @Override
        public void write(final int b) {
        }
    };

}
