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
package com.jporm.rm.session;

import org.junit.Test;

import com.jporm.commons.core.connection.impl.NullConnectionProvider;
import com.jporm.core.domain.AutoId;
import com.jporm.rm.BaseTestApi;
import com.jporm.rm.JpoRm;
import com.jporm.rm.JpoRmBuilder;

/**
 *
 * @author Francesco Cina
 *
 *         20/mag/2011
 */
public class BeanAutoRegistrationTest extends BaseTestApi {

    @Test
    public void testAutoRegisterAutoId() {
        // Use a class without register it, it should be auto registered
        final JpoRm jpOrm = JpoRmBuilder.get().build(new NullConnectionProvider());

        // SHOULD NOT THROWN EXCEPTIONS
        jpOrm.session().save(new AutoId());
    }

}
