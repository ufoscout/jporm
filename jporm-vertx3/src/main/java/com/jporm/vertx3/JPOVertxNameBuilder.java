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

public class JPOVertxNameBuilder {

	private final static String CONSUMER_NAME_TRANSACTION_SUFFIX = "TRANSACTION";
	private final static String CONSUMER_NAME_TRANSACTION_VOID_SUFFIX = "TRANSACTION_VOID";
	private final String instanceId;
	private final String defaultCodecName = NullMessageCodec.NAME;
	private final String consumerNameTransaction;
	private final String consumerNameTransactionVoid;

	JPOVertxNameBuilder(final String instanceId) {
		this.instanceId = instanceId;
		consumerNameTransaction = instanceId + CONSUMER_NAME_TRANSACTION_SUFFIX;
		consumerNameTransactionVoid = instanceId + CONSUMER_NAME_TRANSACTION_VOID_SUFFIX;
	}

	public String getInstanceId() {
		return instanceId;
	}

	public String getDefaultCodecName() {
		return defaultCodecName;
	}

	public String getConsumerNameTransaction() {
		return consumerNameTransaction;
	}

	public String getConsumerNameTransactionVoid() {
		return consumerNameTransactionVoid;
	}

}
