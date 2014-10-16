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
package com.jporm.core.query.crud.executor;

import java.util.Collection;

import com.jporm.annotation.cascade.CascadeInfo;
import com.jporm.core.session.SessionImpl;
import com.jporm.exception.OrmException;
import com.jporm.query.save.SaveOrUpdateType;
import com.jporm.session.Session;

public class SaveOrUpdateStrategyFactory {

	public static SaveOrUpdateStrategy getStrategy(final SaveOrUpdateType saveOrUpdateType) {

		switch (saveOrUpdateType) {
		case SAVE: {
			return new SaveOrUpdateStrategy() {
				@Override
				public <RELATION> RELATION now(final Session session, final RELATION innerBean, final CascadeInfo cascadeInfo) {
					if (cascadeInfo.onSave()) {
						return session.save(innerBean);
					}
					return innerBean;
				}

				@Override
				public <RELATION> Collection<RELATION> now(final Session session, final Collection<RELATION> innerBeans, final CascadeInfo cascadeInfo) {
					if (cascadeInfo.onSave()) {
						return session.save(innerBeans);
					}
					return innerBeans;
				}
			};
		}
		case UPDATE: {
			return new SaveOrUpdateStrategy() {
				@Override
				public <RELATION> RELATION now(final Session session, final RELATION innerBean, final CascadeInfo cascadeInfo) {
					if (cascadeInfo.onSave()) {
						return session.update(innerBean);
					}
					return innerBean;
				}
				@Override
				public <RELATION> Collection<RELATION> now(final Session session, final Collection<RELATION> innerBeans, final CascadeInfo cascadeInfo) {
					if (cascadeInfo.onSave()) {
						return session.update(innerBeans);
					}
					return innerBeans;
				}
			};
		}
		case SAVE_OR_UPDATE: {
			return new SaveOrUpdateStrategy() {
				@Override
				public <RELATION> RELATION now(final Session session, final RELATION innerBean, final CascadeInfo cascadeInfo) {
					//TODO remove cast
					return ((SessionImpl) session).saveOrUpdate(innerBean, cascadeInfo);
				}
				@Override
				public <RELATION> Collection<RELATION> now(final Session session, final Collection<RELATION> innerBeans, final CascadeInfo cascadeInfo) {
					//TODO remove cast
					return ((SessionImpl) session).saveOrUpdate(innerBeans, cascadeInfo);
				}
			};
		}
		default:
			throw new OrmException("Unmapped type: " + saveOrUpdateType);
		}

	}

}
