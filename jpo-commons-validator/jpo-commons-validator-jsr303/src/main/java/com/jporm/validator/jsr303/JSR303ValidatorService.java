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

import java.util.Collection;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validation;

import com.jporm.validator.ValidatorService;

/**
 *
 * @author ufo
 *
 */
public class JSR303ValidatorService implements ValidatorService {

    private final javax.validation.Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    @Override
    public <T> void validateThrowException(final Collection<T> data) {
        validateThrowException(new JSR303ValidableCollection<T>(data));
    }

    @Override
    public <T> void validateThrowException(final T data) {
        Set<ConstraintViolation<T>> validationResult = validator.validate(data);
        if (!validationResult.isEmpty()) {
            throw new ConstraintViolationException(validationResult);
        }
    }

}
