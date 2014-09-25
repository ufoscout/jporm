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
package com.jporm.mapper.clazz;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import com.jporm.BaseTestApi;
import com.jporm.domain.section08.AggregatedUser;
import com.jporm.domain.section08.AggregatedUserJob;
import com.jporm.domain.section08.AggregatedUserSingleJob;
import com.jporm.domain.section08.User;
import com.jporm.domain.section08.UserAddress;
import com.jporm.exception.OrmConfigurationException;
import com.jporm.mapper.clazz.ClassMap;
import com.jporm.mapper.clazz.ClassMapBuilderImpl;
import com.jporm.mapper.relation.RelationOuterFK;

/**
 * 
 * @author Francesco Cina
 *
 * 01/giu/2011
 */
public class ClassMapperAggregatedBeanTest extends BaseTestApi {

    @Test
    public <P> void testClassMapperAggregatedUserManyJobs() throws IllegalArgumentException {
        final ClassMap<AggregatedUser> classMapper = new ClassMapBuilderImpl<AggregatedUser>(AggregatedUser.class, getJPO().getServiceCatalog()).generate();
        assertNotNull(classMapper);

        assertEquals("USERS", classMapper.getTableInfo().getTableNameWithSchema()); //$NON-NLS-1$
        assertEquals(4 , classMapper.getAllColumnJavaNames().length );

        assertFalse(classMapper.getCacheInfo().isCacheable());
        assertEquals("", classMapper.getCacheInfo().getCacheName()); //$NON-NLS-1$
        assertEquals("", classMapper.getCacheInfo().cacheToUse("")); //$NON-NLS-1$ //$NON-NLS-2$
        assertEquals("ANOTHER_CACHE_NAME", classMapper.getCacheInfo().cacheToUse("ANOTHER_CACHE_NAME")); //$NON-NLS-1$ //$NON-NLS-2$

        String listFieldName = "jobs"; //$NON-NLS-1$
        assertFalse( Arrays.asList(classMapper.getAllColumnJavaNames()).contains( listFieldName ) );

        assertFalse( classMapper.getOuterRelations().isEmpty() );
        assertEquals( 1, classMapper.getOuterRelations().size() );
        assertTrue( classMapper.getOuterRelations().get(0).isOneToMany() );
        assertTrue( classMapper.getOuterRelations().get(0).getCascadeInfo().onDelete() );
        assertTrue( classMapper.getOuterRelations().get(0).getCascadeInfo().onSave() );
        assertTrue( classMapper.getOuterRelations().get(0).getCascadeInfo().onUpdate() );

        RelationOuterFK<AggregatedUser, AggregatedUserJob, List<AggregatedUserJob>> relation = (RelationOuterFK<AggregatedUser, AggregatedUserJob, List<AggregatedUserJob>>) classMapper.getOuterRelations().get(0);
        assertEquals( AggregatedUserJob.class , relation.getRelationWithClass() );
        assertEquals( listFieldName, relation.getJavaFieldName() );

        AggregatedUser aggregatedUser = new AggregatedUser();
        aggregatedUser.setJobs(null);
        assertNull( relation.getGetManipulator().getValue(aggregatedUser) );
        relation.getSetManipulator().setValue( aggregatedUser, new ArrayList<AggregatedUserJob>() );
        assertNotNull( relation.getGetManipulator().getValue(aggregatedUser) );

    }

    @Test
    public <P> void testClassMapperAggregatedUserSingleJob() throws IllegalArgumentException {
        final ClassMap<AggregatedUserSingleJob> classMapper = new ClassMapBuilderImpl<AggregatedUserSingleJob>(AggregatedUserSingleJob.class, getJPO().getServiceCatalog()).generate();
        assertNotNull(classMapper);

        assertEquals("USERS", classMapper.getTableInfo().getTableNameWithSchema()); //$NON-NLS-1$
        assertEquals(4 , classMapper.getAllColumnJavaNames().length );

        assertTrue(classMapper.getCacheInfo().isCacheable());
        assertEquals("TEST_CACHE_NAME", classMapper.getCacheInfo().getCacheName()); //$NON-NLS-1$
        assertEquals("TEST_CACHE_NAME", classMapper.getCacheInfo().cacheToUse("")); //$NON-NLS-1$ //$NON-NLS-2$
        assertEquals("ANOTHER_CACHE_NAME", classMapper.getCacheInfo().cacheToUse("ANOTHER_CACHE_NAME")); //$NON-NLS-1$ //$NON-NLS-2$

        String relationFieldName = "job"; //$NON-NLS-1$
        assertFalse( Arrays.asList(classMapper.getAllColumnJavaNames()).contains( relationFieldName ) );

        assertFalse( classMapper.getOuterRelations().isEmpty() );
        assertEquals( 1, classMapper.getOuterRelations().size() );
        assertFalse( classMapper.getOuterRelations().get(0).isOneToMany() );
        assertFalse( classMapper.getOuterRelations().get(0).getCascadeInfo().onDelete() );
        assertFalse( classMapper.getOuterRelations().get(0).getCascadeInfo().onSave() );
        assertFalse( classMapper.getOuterRelations().get(0).getCascadeInfo().onUpdate() );

        RelationOuterFK<AggregatedUserSingleJob, AggregatedUserJob, AggregatedUserJob> relation = (RelationOuterFK<AggregatedUserSingleJob, AggregatedUserJob, AggregatedUserJob>) classMapper.getOuterRelations().get(0);
        assertEquals( AggregatedUserJob.class , relation.getRelationWithClass() );
        assertEquals( relationFieldName, relation.getJavaFieldName() );

        AggregatedUserSingleJob aggregatedUser = new AggregatedUserSingleJob();
        aggregatedUser.setJob(null);
        assertNull( relation.getGetManipulator().getValue(aggregatedUser) );
        relation.getSetManipulator().setValue( aggregatedUser, new AggregatedUserJob() );
        assertNotNull( relation.getGetManipulator().getValue(aggregatedUser) );

    }

    @Test
    public <P> void testClassMapperGeneratorFKs() {
        final ClassMap<AggregatedUserJob> userJobClassMapper = new ClassMapBuilderImpl<AggregatedUserJob>(AggregatedUserJob.class, getJPO().getServiceCatalog()).generate();
        assertNotNull(userJobClassMapper);

        assertTrue( User.class.isAssignableFrom(AggregatedUser.class));

        assertTrue( userJobClassMapper.getFKs().hasFKVersus(User.class) );
        assertEquals("userId" , userJobClassMapper.getFKs().versus(User.class).getBeanFieldName() ); //$NON-NLS-1$
        assertTrue( userJobClassMapper.getFKs().hasFKVersus(AggregatedUser.class) );
        assertEquals("userId" , userJobClassMapper.getFKs().versus(AggregatedUser.class).getBeanFieldName() ); //$NON-NLS-1$

        assertFalse( userJobClassMapper.getFKs().hasFKVersus(UserAddress.class) );
        try {
            userJobClassMapper.getFKs().versus(UserAddress.class);
            fail("An exception should be thrown before"); //$NON-NLS-1$
        } catch (OrmConfigurationException e) {
            //ok
        }
    }

}
