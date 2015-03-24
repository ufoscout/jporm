/*******************************************************************************
 * Copyright 2015 Francesco Cina'
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
package com.jporm.commons.core.util;

import java.math.BigDecimal;

public class BigDecimalUtil {

	public static Boolean toBoolean(BigDecimal value) {
		return (value == null) ? null : BigDecimal.ONE.equals(value);
	}

	public static Double toDouble(BigDecimal value) {
		return (value == null) ? null : value.doubleValue();
	}

	public static Float toFloat(BigDecimal value) {
		return (value == null) ? null : value.floatValue();
	}

	public static Integer toInteger(BigDecimal value) {
		return (value == null) ? null : value.intValue();
	}

	public static Long toLong(BigDecimal value) {
		return (value == null) ? null : value.longValue();
	}
}
