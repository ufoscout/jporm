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
package com.jporm;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import spike.EnumTest;
import spike.MapBenchmarkTest;
import spike.SameNameColumnsResultSetTest;

import com.jporm.cache.SimpleCacheManagerTest;
import com.jporm.cache.ehcache.EhCacheManagerTest;
import com.jporm.dialect.DetermineDBTypeTest;
import com.jporm.dialect.querytemplate.DerbyQueryTemplateTest;
import com.jporm.dialect.querytemplate.HSQLDB2QueryTemplateTest;
import com.jporm.dialect.querytemplate.MySqlQueryTemplateTest;
import com.jporm.dialect.querytemplate.Oracle10gQueryTemplateTest;
import com.jporm.domain.SampleDomainObjectOneTest;
import com.jporm.mapper.clazz.ClassDBMapReflectionTest;
import com.jporm.mapper.clazz.ClassMapperAggregatedBeanTest;
import com.jporm.mapper.clazz.ClassMapperGeneratorTest;
import com.jporm.mapper.clazz.ExtendedClassDBMapReflectionTest;
import com.jporm.persistor.PropertyPersistorCloneTest;
import com.jporm.persistor.generator.CheckValidValueTest;
import com.jporm.persistor.math.VersionMathTest;
import com.jporm.persistor.reflection.ReflectionAllAnnotationsBeanPersistorGeneratorTest;
import com.jporm.persistor.reflection.ReflectionCloneBeanPersistorGeneratorTest;
import com.jporm.persistor.reflection.ReflectionEmployerPersistorGeneratorTest;
import com.jporm.persistor.reflection.ReflectionGeneratorManipulatorTest;
import com.jporm.persistor.type.TypeFactoryEnumTest;
import com.jporm.persistor.type.TypeFactoryTest;
import com.jporm.persistor.wrapper.BooleanToBigDecimalWrapperTest;
import com.jporm.persistor.wrapper.JPOAddWrapperTest;
import com.jporm.query.NameSolverImplTest;
import com.jporm.query.SmartRenderableSqlQueryTest;
import com.jporm.query.clause.from.IFromElementTest;
import com.jporm.query.clause.order.OrderElementTest;
import com.jporm.query.clause.select.JavaRegexTest;
import com.jporm.query.clause.select.SelectTest;
import com.jporm.query.clause.where.ExpressionElementsTest;
import com.jporm.query.clause.where.ExpressionTest;
import com.jporm.query.delete.OrmDeleteTest;
import com.jporm.query.find.FindQueryTest;
import com.jporm.query.find.cache.CacheStrategyImplTest;
import com.jporm.query.update.OrmUpdateTest;
import com.jporm.script.ScriptExecutorTest;
import com.jporm.script.StreamParserTest;
import com.jporm.session.BeanAutoRegistrationTest;
import com.jporm.session.SessionCRUDTest;
import com.jporm.session.SqlExecutorsTest;
import com.jporm.util.FieldDefaultNamingTest;
import com.jporm.util.MapUtilTest;
import com.jporm.util.StringUtilTest;
import com.jporm.validator.JSR303ValidationServiceTest;
import com.jporm.validator.OvalValidationServiceTest;

/**
 * 
 * @author Francesco Cina
 * 
 *         20/mag/2011
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({
    BeanAutoRegistrationTest.class,
    BooleanToBigDecimalWrapperTest.class,
    CacheStrategyImplTest.class,
    CheckValidValueTest.class,
    ClassDBMapReflectionTest.class,
    ClassMapperAggregatedBeanTest.class,
    ClassMapperGeneratorTest.class,
    DetermineDBTypeTest.class,
    DerbyQueryTemplateTest.class,
    EhCacheManagerTest.class,
    EnumTest.class,
    ExpressionElementsTest.class,
    ExpressionTest.class,
    ExtendedClassDBMapReflectionTest.class,
    FieldDefaultNamingTest.class,
    FindQueryTest.class,
    HSQLDB2QueryTemplateTest.class,
    IFromElementTest.class,
    JPOAddWrapperTest.class,
    JSR303ValidationServiceTest.class,
    JavaRegexTest.class,
    MapBenchmarkTest.class,
    MapUtilTest.class,
    MySqlQueryTemplateTest.class,
    Oracle10gQueryTemplateTest.class,
    OrderElementTest.class,
    NameSolverImplTest.class,
    OrmDeleteTest.class,
    OrmUpdateTest.class,
    OvalValidationServiceTest.class,
    PropertyPersistorCloneTest.class,
    ReflectionAllAnnotationsBeanPersistorGeneratorTest.class,
    ReflectionCloneBeanPersistorGeneratorTest.class,
    ReflectionEmployerPersistorGeneratorTest.class,
    ReflectionGeneratorManipulatorTest.class,
    SameNameColumnsResultSetTest.class,
    SampleDomainObjectOneTest.class,
    ScriptExecutorTest.class,
    SelectTest.class,
    SessionCRUDTest.class,
    SimpleCacheManagerTest.class,
    SmartRenderableSqlQueryTest.class,
    SqlExecutorsTest.class,
    StreamParserTest.class,
    StringUtilTest.class,
    TypeFactoryEnumTest.class,
    TypeFactoryTest.class,
    VersionMathTest.class

})
public class AllTests {
    //Do nothing
}
