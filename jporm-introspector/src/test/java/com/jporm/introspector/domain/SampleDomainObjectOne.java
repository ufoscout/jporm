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
package com.jporm.introspector.domain;

import com.jporm.annotation.Column;
import com.jporm.annotation.Id;
import com.jporm.annotation.Table;
import com.jporm.annotation.Version;

//formatter:off
/**
 * <class_description>
 * <p><b>notes</b>:
 * <p>ON : 3-aug.-2012
 *
 * @author  - vanroyni
 * @version $Revision
 */
//formatter:on
@Table(tableName = "UM_NOTIFICATION_PREFS")
public class SampleDomainObjectOne {

    @Id
    private Long userId;

    @Id
    @Column(name = "notification_type_id")
    private String typeId;
    private Long languageId;
    private String frequencyId;
    private Long emailId;

    private boolean subscribed;

    @Version
    private Long updateLock;

    /**
     * @return the id
     */
    public Long getUserId() {
        return userId;
    }

    /**
     * @param id
     *           the id to set
     */
    public void setUserId(final Long userId) {
        this.userId = userId;
    }

    /**
     * @return the typeId
     */
    public String getTypeId() {
        return typeId;
    }

    /**
     * @param typeId
     *           the typeId to set
     */
    public void setTypeId(final String typeId) {
        this.typeId = typeId;
    }

    /**
     * @return the languageId
     */
    public Long getLanguageId() {
        return languageId;
    }

    /**
     * @param languageId
     *           the languageId to set
     */
    public void setLanguageId(final Long languageId) {
        this.languageId = languageId;
    }

    /**
     * @return the frequencyId
     */
    public String getFrequencyId() {
        return frequencyId;
    }

    /**
     * @param frequencyId
     *           the frequencyId to set
     */
    public void setFrequencyId(final String frequencyId) {
        this.frequencyId = frequencyId;
    }

    /**
     * @return the subscribed
     */
    public boolean isSubscribed() {
        return subscribed;
    }

    /**
     * @param subscribed
     *           the subscribed to set
     */
    public void setSubscribed(final boolean subscribed) {
        this.subscribed = subscribed;
    }

    /**
     * @return the emailId
     */
    public Long getEmailId() {
        return emailId;
    }

    /**
     * @param emailId
     *           the emailId to set
     */
    public void setEmailId(final Long emailId) {
        this.emailId = emailId;
    }

    /**
     * @return the updateLock
     */
    public Long getUpdateLock() {
        return updateLock;
    }

    /**
     * @param updateLock
     *           the updateLock to set
     */
    public void setUpdateLock(final Long updateLock) {
        this.updateLock = updateLock;
    }

}
