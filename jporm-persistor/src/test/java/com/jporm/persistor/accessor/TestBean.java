/*******************************************************************************
 * Copyright 2014 Francesco Cina'
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
package com.jporm.persistor.accessor;

@SuppressWarnings("unused")
public class TestBean {

	private String privateString = "privateValue";

	public long publicLongPrimitive;
	public Long publicLong;
	private String string;
	private int intPrimitive;
	private Integer integer;

	public String getString() {
		return string;
	}
	public void setString(final String string) {
		this.string = string;
	}
	public int getIntPrimitive() {
		return intPrimitive;
	}
	public void setIntPrimitive(final int intPrimitive) {
		this.intPrimitive = intPrimitive;
	}
	public Integer getInteger() {
		return integer;
	}
	public void setInteger(final Integer integer) {
		this.integer = integer;
	}
	private Integer getIntegerPrivate() {
		return integer;
	}
	private void setIntegerPrivate(final Integer integer) {
		this.integer = integer;
	}
}