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

import java.util.Optional;

@SuppressWarnings("unused")
public class TestBean {

	private final String privateString = "privateValue";

	public long publicLongPrimitive;
	public Long publicLong;
	public String string;
	private int intPrimitive;
	private Integer integer;
	private String address;
	public Optional<String> hobby;
	private Optional<String> nickname;
	private Optional<String> car;

	public Integer getInteger() {
		return integer;
	}

	private Integer getIntegerPrivate() {
		return integer;
	}

	public int getIntPrimitive() {
		return intPrimitive;
	}

	public String getString() {
		return string;
	}

	public void setInteger(final Integer integer) {
		this.integer = integer;
	}

	private void setIntegerPrivate(final Integer integer) {
		this.integer = integer;
	}

	public void setIntPrimitive(final int intPrimitive) {
		this.intPrimitive = intPrimitive;
	}

	public void setString(final String string) {
		this.string = string;
	}

	/**
	 * @return the address
	 */
	public String getAddress() {
		return address;
	}

	/**
	 * @param address the address to set
	 */
	public TestBean withAddress(String address) {
		final TestBean result = new TestBean();
		result.address = address;
		return result;
	}

	/**
	 * @return the nickname
	 */
	public Optional<String> getNickname() {
		return nickname;
	}

	/**
	 * @param nickname the nickname to set
	 */
	public void setNickname(Optional<String> nickname) {
		this.nickname = nickname;
	}

	/**
	 * @return the car
	 */
	public Optional<String> getCar() {
		return car;
	}

	/**
	 * @param car the car to set
	 */
	public TestBean withCar(Optional<String> car) {
		final TestBean result = new TestBean();
		result.car = car;
		return result;
	}

}