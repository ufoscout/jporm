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
package com.jporm.annotation.cascade;

import com.jporm.annotation.Cascade;

/**
 * <class_description>
 * <p><b>notes</b>:
 * <p>ON : Mar 10, 2013
 *
 * @author  - Francesco Cina
 * @version $Revision
 */
public enum CascadeType {

    /**
     * Cascade delete, save and update actions. This is the default behavior if the
     * {@link Cascade} annotation is not specified.
     */
    ALWAYS(       new CascadeInfoImpl(true, true, true)),
    /**
     * Cascade only delete actions.
     */
    DELETE(       new CascadeInfoImpl(true, false, false)),
    /**
     * Cascade delete and save actions.
     */
    DELETE_SAVE(  new CascadeInfoImpl(true, true, false)),
    /**
     * Cascade delete and update actions.
     */
    DELETE_UPDATE(new CascadeInfoImpl(true, false, true)),
    /**
     * Cascade only save actions.
     */
    SAVE(         new CascadeInfoImpl(false, true, false)),
    /**
     * Cascade save and update actions.
     */
    SAVE_UPDATE(  new CascadeInfoImpl(false, true, true)),
    /**
     * Cascade only update actions.
     */
    UPDATE(       new CascadeInfoImpl(false, false, true)),
    /**
     * Cascade is disabled.
     */
    NEVER(        new CascadeInfoImpl(false, false, false));

    private final CascadeInfo info;

    CascadeType(final CascadeInfo info) {
        this.info = info;
    }

    public CascadeInfo getInfo() {
        return info;
    }

}
