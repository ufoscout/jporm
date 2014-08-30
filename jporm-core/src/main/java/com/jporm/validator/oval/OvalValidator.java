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
package com.jporm.validator.oval;

import java.util.List;

import net.sf.oval.ConstraintViolation;
import net.sf.oval.exception.ConstraintsViolatedException;

import com.jporm.validator.Validator;

/**
 * 
 * <class_description>
 * <p><b>notes</b>:
 * <p>ON : Feb 27, 2013
 *
 * @author Francesco Cina'
 * @version $Revision
 */
public class OvalValidator<T> implements Validator<T> {

    private final T data;
    private final net.sf.oval.Validator validator;

    public OvalValidator(final T data, final net.sf.oval.Validator validator) {
        this.data = data;
        this.validator = validator;
    }

    @Override
    public void validateThrowException() {
        List<ConstraintViolation> violations = this.validator.validate(this.data);
        if (!violations.isEmpty()) {
            throw new ConstraintsViolatedException(violations);
        }
    }

}
