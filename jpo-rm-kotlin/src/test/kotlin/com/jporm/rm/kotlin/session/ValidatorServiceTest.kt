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
package com.jporm.rm.kotlin.session

import org.junit.Assert.fail

import java.util.ArrayList
import java.util.Arrays

import javax.validation.ConstraintViolationException
import javax.validation.constraints.Min
import javax.validation.constraints.NotNull
import javax.validation.constraints.Size

import org.junit.Test

import com.jporm.rm.kotlin.BaseTestApi
import com.jporm.rm.kotlin.JpoRm
import com.jporm.rm.kotlin.JpoRmBuilder
import com.jporm.validator.ValidatorService
import com.jporm.validator.jsr303.JSR303ValidatorService

/**

 * <class_description>
 *
 *
 * **notes**:
 *
 *
 * ON : Feb 27, 2013

 * @author Francesco Cina'
 * *
 * @version $Revision
</class_description> */
class ValidatorServiceTest : BaseTestApi() {

    class Song {
        var id: Long? = null
        var lyricId: Long? = null

        @NotNull(message = "notNull")
        @Size(min = 3, message = "minLenght3")
        var title: String? = null

        @NotNull(message = "notNull")
        @Size(min = 3, message = "minLenght3")
        var artist: String? = null
        // @Size(min = 4, message="minLenght4")
        @Min(value = 1900, message = "minSize1900")
        var year: Int? = null

    }

    private val validationService = JSR303ValidatorService()

    @Test
    fun testBeanValidation() {
        val song = Song()
        song.title = "u" //$NON-NLS-1$
        song.year = 100

        try {
            validationService.validateThrowException(song)
            fail("an exception should be thrown before") //$NON-NLS-1$
        } catch (e: ConstraintViolationException) {
            // ok
        }

    }

    @Test
    fun testCollectionValidation() {
        val song = Song()
        song.title = "u" //$NON-NLS-1$
        song.year = 100

        val songs = ArrayList<Song>()
        songs.add(song)

        try {
            validationService.validateThrowException(songs)
            fail("an exception should be thrown before") //$NON-NLS-1$
        } catch (e: ConstraintViolationException) {
            // ok
        }

    }

    @Test
    fun testJPOValidationError() {
        val song = Song()
        song.title = "u" //$NON-NLS-1$
        song.year = 100

        val jpo = JpoRmBuilder.get().setValidatorService(validationService).build(h2DataSource)

        try {
            jpo.tx { session -> session.save(song) }
            fail("an exception should be thrown before") //$NON-NLS-1$
        } catch (e: ConstraintViolationException) {
            // ok
        }

        try {
            jpo.tx { session -> session.save(Arrays.asList(song)) }
            fail("an exception should be thrown before") //$NON-NLS-1$
        } catch (e: ConstraintViolationException) {
            // ok
        }

        try {
            jpo.tx { session -> session.update(song) }
            fail("an exception should be thrown before") //$NON-NLS-1$
        } catch (e: ConstraintViolationException) {
            // ok
        }

        try {
            jpo.tx { session -> session.update(Arrays.asList(song)) }
            fail("an exception should be thrown before") //$NON-NLS-1$
        } catch (e: ConstraintViolationException) {
            // ok
        }

        try {
            jpo.tx { session -> session.saveOrUpdate(song) }
            fail("an exception should be thrown before") //$NON-NLS-1$
        } catch (e: ConstraintViolationException) {
            // ok
        }

        try {

            jpo.tx { session -> session.saveOrUpdate(Arrays.asList(song)) }
            fail("an exception should be thrown before") //$NON-NLS-1$
        } catch (e: ConstraintViolationException) {
            // ok
        }

    }

}
