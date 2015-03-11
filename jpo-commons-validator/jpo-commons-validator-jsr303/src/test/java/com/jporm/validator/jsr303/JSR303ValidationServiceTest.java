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
package com.jporm.validator.jsr303;

import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.List;

import javax.validation.ConstraintViolationException;

import org.junit.Test;

import com.jporm.validator.BaseJSR303ValidatorTestApi;
import com.jporm.validator.ValidatorService;

/**
 *
 * <class_description>
 * <p><b>notes</b>:
 * <p>ON : Feb 27, 2013
 *
 * @author Francesco Cina'
 * @version $Revision
 */
public class JSR303ValidationServiceTest extends BaseJSR303ValidatorTestApi {

	private final ValidatorService validationService = new JSR303ValidatorService();

	@Test
	public void testBeanValidation() {
		Song song = new Song();
		song.setTitle("u"); //$NON-NLS-1$
		song.setYear(100);

		try {
			validationService.validateThrowException(song);
			fail("an exception should be thrown before"); //$NON-NLS-1$
		} catch (ConstraintViolationException e) {
			//ok
		}
	}

	@Test
	public void testCollectionValidation() {
		Song song = new Song();
		song.setTitle("u"); //$NON-NLS-1$
		song.setYear(100);

		List<Song> songs = new ArrayList<>();
		songs.add(song);

		try {
			validationService.validateThrowException(songs);
			fail("an exception should be thrown before"); //$NON-NLS-1$
		} catch (ConstraintViolationException e) {
			//ok
		}
	}

}
