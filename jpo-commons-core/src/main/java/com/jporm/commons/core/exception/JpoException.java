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
package com.jporm.commons.core.exception;

/**
 *
 * @author Francesco Cina
 *
 * 21/mag/2011
 */
public class JpoException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public JpoException(final String message) {
		super(message);
	}

	public JpoException(final Exception e) {
		super(e);
	}

	public JpoException(final Throwable e) {
		super(e);
	}

	public JpoException(final String message, final Exception e) {
		super(message, e);
	}
}
