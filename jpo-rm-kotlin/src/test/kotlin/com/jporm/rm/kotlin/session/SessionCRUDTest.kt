/*******************************************************************************
 * Copyright 2013 Francesco Cina'

 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at

 * http://www.apache.org/licenses/LICENSE-2.0

 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
/* ----------------------------------------------------------------------------
 *     PROJECT : JPOrm
 *
 *  CREATED BY : Francesco Cina'
 *          ON : Feb 14, 2013
 * ----------------------------------------------------------------------------
 */
package com.jporm.rm.kotlin.session

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue

import java.util.Date

import org.junit.Test

import com.jporm.core.domain.AutoId
import com.jporm.rm.kotlin.BaseTestApi

/**
 * <class_description>
 *
 *
 * **notes**:
 *
 *
 * ON : Feb 14, 2013

 * @author Francesco Cina'
 * *
 * @version $Revision
</class_description> */
class SessionCRUDTest : BaseTestApi() {

    @Test
    fun testSaveOrUpdateWithConditionGenerator() {
        jpo.tx().execute<Any> { session: Session ->

            var autoId = AutoId()
            val value = "value for test " + Date().time //$NON-NLS-1$
            autoId.value = value

            autoId = session.saveOrUpdate(autoId)
            val newId = autoId.id

            assertTrue(session.findById(AutoId::class.java, newId).fetchRowCount() > 0)

            assertEquals(value, session.findById(AutoId::class.java, newId).fetchOneOptional().get().value)

            val newValue = "new value for test " + Date().time //$NON-NLS-1$
            autoId.value = newValue

            autoId = session.saveOrUpdate(autoId)

            assertEquals(newId, autoId.id)
            assertEquals(newValue, session.findById(AutoId::class.java, newId).fetchOneOptional().get().value)

            session.delete(autoId)
            assertFalse(session.findById(AutoId::class.java, newId).fetchRowCount() > 0)

            null
        }

    }

}
