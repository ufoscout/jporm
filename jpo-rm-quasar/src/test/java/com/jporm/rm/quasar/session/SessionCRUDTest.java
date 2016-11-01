/*******************************************************************************
 * Copyright 2013 Francesco Cina'
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
/* ----------------------------------------------------------------------------
 *     PROJECT : JPOrm
 *
 *  CREATED BY : Francesco Cina'
 *          ON : Feb 14, 2013
 * ----------------------------------------------------------------------------
 */
package com.jporm.rm.quasar.session;

import static org.junit.Assert.assertEquals;

import java.util.Date;

import org.junit.Assert;
import org.junit.Test;

import com.jporm.core.domain.AutoId;
import com.jporm.rm.quasar.RmQuasarTestBase;

/**
 * <class_description>
 * <p>
 * <b>notes</b>:
 * <p>
 * ON : Feb 14, 2013
 *
 * @author Francesco Cina'
 * @version $Revision
 */
public class SessionCRUDTest extends RmQuasarTestBase {

    @Test
    public void testSaveOrUpdateWithConditionGenerator() {

        newJpo().tx(session -> {

            AutoId autoId = new AutoId();
            final String value = "value for test " + new Date().getTime(); //$NON-NLS-1$
            autoId.setValue(value);

            autoId = session.saveOrUpdate(autoId);
            final Integer newId = autoId.getId();

            Assert.assertTrue(session.findById(AutoId.class, newId).fetchRowCount() > 0);

            assertEquals(value, session.findById(AutoId.class, newId).fetchOneOptional().get().getValue());

            final String newValue = "new value for test " + new Date().getTime(); //$NON-NLS-1$
            autoId.setValue(newValue);

            autoId = session.saveOrUpdate(autoId);

            Assert.assertEquals(newId, autoId.getId());
            assertEquals(newValue, session.findById(AutoId.class, newId).fetchOneOptional().get().getValue());

            Assert.assertTrue(session.delete(autoId) == 1);
            Assert.assertFalse(session.findById(AutoId.class, newId).fetchRowCount() > 0);

        });

    }

}
