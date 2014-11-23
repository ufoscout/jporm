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
package com.jporm.transactional;

import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

public class H2TransactionalExecutor implements ITransactionalExecutor {

	@Override
	@Transactional (rollbackFor=Throwable.class)
	public void exec(final ITransactionalCode code) throws Exception {
		code.exec();
	}

	@Override
	@Transactional (readOnly=true)
	public void execReadOnly(final ITransactionalCode code) throws Exception {
		code.exec();
	}

	@Override
	@Transactional (rollbackFor=Throwable.class, isolation=Isolation.SERIALIZABLE)
	public void execSerializable(final ITransactionalCode code) throws Exception {
		code.exec();
	}


	@Override
	@Transactional (rollbackFor=MyException.class)
	public void execRollbackForMyException(final ITransactionalCode code) throws Exception {
		code.exec();
	}

}
