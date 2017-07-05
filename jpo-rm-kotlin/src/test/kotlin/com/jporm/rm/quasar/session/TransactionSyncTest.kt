/*******************************************************************************
 * Copyright 2015 Francesco Cina'

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
package com.jporm.rm.quasar.session

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue

import java.util.UUID
import java.util.concurrent.atomic.AtomicLong

import org.junit.Test

import com.jporm.rm.quasar.RmKotlinTestBase
import com.jporm.test.domain.section08.CommonUser

class TransactionSyncTest : RmKotlinTestBase() {

    @Test
    @Throws(Throwable::class)
    fun failing_transaction_should_be_rolledback_at_the_end() {
        val jpo = newJpo()

        val firstUserId = AtomicLong()
        //
        try {
            jpo.tx().execute({ txSession ->
                val user = CommonUser()
                user.setFirstname(UUID.randomUUID().toString())
                user.setLastname(UUID.randomUUID().toString())

                val firstUser = txSession.save(user)
                assertNotNull(firstUser)
                assertNotNull(firstUser.getId())
                firstUserId.set(firstUser.getId())

                assertTrue(txSession.findById(CommonUser::class.java, firstUserId.get()).exist())

                // This action should fail because the object does not provide
                // all the mandatory fields
                val failingUser = CommonUser()
                txSession.save(failingUser)
            })
        } catch (e: Exception) {
            assertFalse(jpo.tx({ session -> session.findById(CommonUser::class.java, firstUserId.get()).exist() }))
        }

    }

    @Test
    @Throws(Throwable::class)
    fun transaction_should_be_committed_at_the_end() {
        val jpo = newJpo()

        val newUser = jpo.tx().execute({ txSession ->
            val user = CommonUser()
            user.setFirstname(UUID.randomUUID().toString())
            user.setLastname(UUID.randomUUID().toString())

            txSession.save(user)
        })
        assertNotNull(newUser)

        val optionalFoundUser = jpo.tx({ session -> session.findById(CommonUser::class.java, newUser.getId()).fetchOneOptional() })
        assertTrue(optionalFoundUser.isPresent())
        assertEquals(newUser.getFirstname(), optionalFoundUser.get().getFirstname())

    }

}
