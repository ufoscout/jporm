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
package com.jporm.validator;

import static org.junit.Assert.fail;

import javax.validation.ConstraintViolationException;

import org.junit.Test;

import com.jporm.BaseTestApi;
import com.jporm.JPO;
import com.jporm.validator.ValidatorService;
import com.jporm.validator.jsr303.JSR303ValidatorService;

/**
 *
 * <class_description>
 * <p><b>notes</b>:
 * <p>ON : Feb 27, 2013
 *
 * @author Francesco Cina'
 * @version $Revision
 */
public class JSR303ValidationServiceTest extends BaseTestApi {

    private final ValidatorService validationService = new JSR303ValidatorService();

    @Test
    public void testBeanValidation() {
        Song song = new Song();
        song.setTitle("u"); //$NON-NLS-1$
        song.setYear(100);

        try {
            validationService.validator(song).validateThrowException();
            fail("an exception should be thrown before"); //$NON-NLS-1$
        } catch (ConstraintViolationException e) {
            //ok
        }
    }

    @Test
    public void testJPOValidationError() {
        Song song = new Song();
        song.setTitle("u"); //$NON-NLS-1$
        song.setYear(100);

        JPO jpo = getJPO();
        jpo.setValidatorService(validationService);

        try {
            jpo.session().save(song);
            fail("an exception should be thrown before"); //$NON-NLS-1$
        } catch (ConstraintViolationException e) {
            //ok
        }

        try {
            jpo.session().update(song);
            fail("an exception should be thrown before"); //$NON-NLS-1$
        } catch (ConstraintViolationException e) {
            //ok
        }

        try {
            jpo.session().saveOrUpdate(song);
            fail("an exception should be thrown before"); //$NON-NLS-1$
        } catch (ConstraintViolationException e) {
            //ok
        }
    }


}
