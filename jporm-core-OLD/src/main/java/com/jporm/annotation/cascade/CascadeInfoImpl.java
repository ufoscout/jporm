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

/**
 * <class_description>
 * <p><b>notes</b>:
 * <p>ON : Mar 10, 2013
 *
 * @author  - Francesco Cina
 * @version $Revision
 */
public class CascadeInfoImpl implements CascadeInfo {

    private final boolean delete;
    private final boolean save;
    private final boolean update;

    public CascadeInfoImpl(final boolean delete, final boolean save, final boolean update) {
        this.delete = delete;
        this.save = save;
        this.update = update;

    }

    @Override
    public boolean onDelete() {
        return delete;
    }

    @Override
    public boolean onSave() {
        return save;
    }

    @Override
    public boolean onUpdate() {
        return update;
    }
}
