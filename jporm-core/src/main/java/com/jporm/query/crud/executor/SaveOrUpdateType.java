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
package com.jporm.query.crud.executor;

import java.util.Collection;

import com.jporm.annotation.cascade.CascadeInfo;
import com.jporm.session.SessionImpl;

/**
 * <class_description>
 * <p><b>notes</b>:
 * <p>ON : Mar 10, 2013
 *
 * @author  - Francesco Cina
 * @version $Revision
 */
public enum SaveOrUpdateType {

    SAVE( new SaveOrUpdateStrategy() {
        @Override
        public <RELATION> RELATION now(final SessionImpl session, final RELATION innerBean, final boolean cascade, final CascadeInfo cascadeInfo) {
            if (cascadeInfo.onSave()) {
                return session.save(innerBean).cascade(cascade).now();
            }
            return innerBean;
        }
        @Override
        public <RELATION> Collection<RELATION> now(final SessionImpl session, final Collection<RELATION> innerBeans, final boolean cascade, final CascadeInfo cascadeInfo) {
            if (cascadeInfo.onSave()) {
                return session.save(innerBeans).cascade(cascade).now();
            }
            return innerBeans;
        }
    }),
    UPDATE( new SaveOrUpdateStrategy() {
        @Override
        public <RELATION> RELATION now(final SessionImpl session, final RELATION innerBean, final boolean cascade, final CascadeInfo cascadeInfo) {
            if (cascadeInfo.onSave()) {
                return session.update(innerBean).cascade(cascade).now();
            }
            return innerBean;
        }
        @Override
        public <RELATION> Collection<RELATION> now(final SessionImpl session, final Collection<RELATION> innerBeans, final boolean cascade, final CascadeInfo cascadeInfo) {
            if (cascadeInfo.onSave()) {
                return session.update(innerBeans).cascade(cascade).now();
            }
            return innerBeans;
        }
    }),
    SAVE_OR_UPDATE( new SaveOrUpdateStrategy() {
        @Override
        public <RELATION> RELATION now(final SessionImpl session, final RELATION innerBean, final boolean cascade, final CascadeInfo cascadeInfo) {
            return session.saveOrUpdate(innerBean, cascadeInfo).cascade(cascade).now();
        }
        @Override
        public <RELATION> Collection<RELATION> now(final SessionImpl session, final Collection<RELATION> innerBeans, final boolean cascade, final CascadeInfo cascadeInfo) {
            return session.saveOrUpdate(innerBeans, cascadeInfo).cascade(cascade).now();
        }
    });

    private final SaveOrUpdateStrategy strategy;

    SaveOrUpdateType(final SaveOrUpdateStrategy strategy) {
        this.strategy = strategy;
    }

    /**
     * @return the strategy
     */
    public SaveOrUpdateStrategy getStrategy() {
        return strategy;
    }

}
