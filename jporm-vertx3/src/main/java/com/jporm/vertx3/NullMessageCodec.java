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
package com.jporm.vertx3;

import io.vertx.core.buffer.Buffer;
import io.vertx.core.eventbus.MessageCodec;

public class NullMessageCodec<T> implements MessageCodec<T, T>{

	public static final String NAME = NullMessageCodec.class.getName();

	@Override
	public void encodeToWire(final Buffer buffer, final T s) {
		throw new RuntimeException("No consumer available to send the object through the network");
	}

	@Override
	public T decodeFromWire(final int pos, final Buffer buffer) {
		throw new RuntimeException("No consumer available to send the object through the network");// TODO Auto-generated method stub
	}

	@Override
	public T transform(final T s) {
		return s;
	}

	@Override
	public String name() {
		return NAME;
	}

	@Override
	public byte systemCodecID() {
		return -1;
	}

}
