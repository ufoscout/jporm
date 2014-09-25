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
package com.jporm.domain;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.jporm.BaseTestApi;
import com.jporm.mapper.NullServiceCatalog;
import com.jporm.mapper.clazz.ClassMap;
import com.jporm.mapper.clazz.ClassMapBuilderImpl;
import com.jporm.persistor.OrmPersistor;
import com.jporm.persistor.PersistorGeneratorImpl;
import com.jporm.persistor.type.TypeFactory;

/**
 * 
 * @author ufo
 *
 */
public class SampleDomainObjectOneTest extends BaseTestApi {

    @Test
    public void testClassMapper() throws Exception {
        final ClassMap<SampleDomainObjectOne> classDBMap = new ClassMapBuilderImpl<SampleDomainObjectOne>(SampleDomainObjectOne.class, new NullServiceCatalog()).generate();
        OrmPersistor<SampleDomainObjectOne> generator = new PersistorGeneratorImpl<SampleDomainObjectOne>(new NullServiceCatalog(), classDBMap, new TypeFactory()).generate();

        SampleDomainObjectOne entity = new SampleDomainObjectOne();
        entity.setUserId( 1l );
        entity.setUpdateLock( 0l );
        entity.setTypeId( "typeIdValue" ); //$NON-NLS-1$

        String[] columns = classDBMap.getPrimaryKeyAndVersionColumnJavaNames();
        Object[] values = generator.getPropertyValues(columns, entity);

        getLogger().info( "Columns names {}", Arrays.asList(columns));
        getLogger().info( "Columns values {}", Arrays.asList(values));
        
        List<String> columnList = Arrays.asList(columns);
        assertTrue(columnList.contains( "typeId" ));
        assertTrue(columnList.contains( "userId" ));
        assertTrue(columnList.contains( "updateLock" ));
        assertEquals( 3 , columnList.size() );
        
        Map<String, Object> valueMap = new HashMap<String, Object>();
        valueMap.put(columns[0], values[0]);
        valueMap.put(columns[1], values[1]);
        valueMap.put(columns[2], values[2]);
        
        assertEquals( Long.valueOf(1) , valueMap.get("userId") );
        assertEquals( Long.valueOf(0) , valueMap.get("updateLock") );
        assertEquals( "typeIdValue" , valueMap.get("typeId") );

    }

}
