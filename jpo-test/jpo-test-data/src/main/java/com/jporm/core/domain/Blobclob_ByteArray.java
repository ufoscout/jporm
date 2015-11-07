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
package com.jporm.core.domain;

import com.jporm.annotation.Column;
import com.jporm.annotation.Generator;
import com.jporm.annotation.GeneratorType;
import com.jporm.annotation.Id;
import com.jporm.annotation.Table;

/**
 * 
 * @author Francesco Cina
 *
 *         08/giu/2011
 */
@Table(tableName = "BLOBCLOB")
public class Blobclob_ByteArray {

    private byte[] blob;
    private byte[] clob;

    @Id
    @Generator(generatorType = GeneratorType.SEQUENCE, name = "SEQ_BLOBCLOB")
    @Column(name = "ID")
    private long index;

    public byte[] getBlob() {
        return blob;
    }

    public byte[] getClob() {
        return clob;
    }

    public long getIndex() {
        return index;
    }

    public void setBlob(final byte[] blob) {
        this.blob = blob;
    }

    public void setClob(final byte[] clob) {
        this.clob = clob;
    }

    public void setIndex(final long index) {
        this.index = index;
    }

}
