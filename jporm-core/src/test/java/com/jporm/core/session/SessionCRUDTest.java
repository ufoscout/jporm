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
/* ----------------------------------------------------------------------------
 *     PROJECT : JPOrm
 *
 *  CREATED BY : Francesco Cina'
 *          ON : Feb 14, 2013
 * ----------------------------------------------------------------------------
 */
package com.jporm.core.session;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Date;

import org.junit.Test;

import com.jporm.core.BaseTestApi;
import com.jporm.core.domain.AutoId;
import com.jporm.session.Session;
import com.jporm.transaction.TransactionCallback;

/**
 * <class_description>
 * <p><b>notes</b>:
 * <p>ON : Feb 14, 2013
 *
 * @author Francesco Cina'
 * @version $Revision
 */
public class SessionCRUDTest extends BaseTestApi {

	@Test
	public void testSaveOrUpdateWithConditionGenerator() {
		getJPO().session().txNow(new TransactionCallback<Void>() {

			@Override
			public Void doInTransaction(final Session session) {

				AutoId autoId = new AutoId();
				final String value = "value for test " + new Date().getTime(); //$NON-NLS-1$
				autoId.setValue(value);

				autoId = session.saveOrUpdate(autoId);
				final Integer newId = autoId.getId();

				assertTrue( session.find(AutoId.class, newId).exist() );

				assertEquals(value, session.find(AutoId.class, newId).getOptional().get().getValue());

				final String newValue = "new value for test " + new Date().getTime(); //$NON-NLS-1$
				autoId.setValue(newValue);

				autoId = session.saveOrUpdate(autoId);

				assertEquals(newId, autoId.getId());
				assertEquals(newValue, session.find(AutoId.class, newId).getOptional().get().getValue());

				session.delete(autoId);
				assertFalse( session.find(AutoId.class, newId).exist() );

				return null;
			}
		});

	}

}
