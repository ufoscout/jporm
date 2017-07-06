/*******************************************************************************
 * Copyright 2013 Francesco Cina'

 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at

 * http://www.apache.org/licenses/LICENSE-2.0

 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.jporm.rm.kotlin.session

import com.jporm.annotation.mapper.clazz.ClassDescriptor
import com.jporm.commons.core.exception.JpoException
import com.jporm.commons.core.inject.ClassTool
import com.jporm.commons.core.inject.ServiceCatalog
import com.jporm.commons.core.query.SqlFactory
import com.jporm.commons.core.query.cache.SqlCache
import com.jporm.persistor.generator.Persistor
import com.jporm.rm.kotlin.query.delete.CustomDeleteQuery
import com.jporm.rm.kotlin.query.delete.CustomDeleteQueryImpl
import com.jporm.rm.kotlin.query.delete.DeleteQueryImpl
import com.jporm.rm.kotlin.query.delete.DeleteQueryListDecorator
import com.jporm.rm.kotlin.query.find.*
import com.jporm.rm.kotlin.query.save.*
import com.jporm.rm.kotlin.query.update.CustomUpdateQuery
import com.jporm.rm.kotlin.query.update.UpdateQuery
import com.jporm.rm.kotlin.query.update.UpdateQueryImpl
import com.jporm.rm.kotlin.session.script.ScriptExecutorImpl
import com.jporm.rm.kotlin.query.update.CustomUpdateQueryImpl
import com.jporm.sql.dialect.DBProfile

import java.util.Arrays
import java.util.stream.Collectors

/**

 * @author Francesco Cina
 * *
 * *         27/giu/2011
 */
class SessionImpl(private val serviceCatalog: ServiceCatalog, private val dbType: DBProfile, sqlExecutor: SqlExecutor, private val sqlCache: SqlCache, private val sqlFactory: SqlFactory) : Session {
    private val sqlSession: SqlSession

    init {
        sqlSession = SqlSessionImpl(sqlExecutor, sqlFactory.sqlDsl)
    }

    @Throws(JpoException::class)
    override fun <BEAN> delete(bean: BEAN): Int {
        return delete(Arrays.asList(bean))
    }

    @Throws(JpoException::class)
    override fun <BEAN> delete(clazz: Class<BEAN>): CustomDeleteQuery {
        val delete = CustomDeleteQueryImpl(sqlFactory.deleteFrom(clazz), sql().executor())
        return delete
    }

    @Throws(JpoException::class)
    override fun <BEAN> delete(beans: Collection<BEAN>): Int {
        val queryList = DeleteQueryListDecorator()
        val beansByClass = beans.stream().collect<Map<Class<*>, List<BEAN>>, Any>(Collectors.groupingBy<BEAN, Class<*>>(Function<BEAN, Class<*>> { it.javaClass }))
        beansByClass.forEach { clazz, classBeans ->
            val typedClass = clazz as Class<BEAN>
            queryList.add(DeleteQueryImpl(classBeans, typedClass, serviceCatalog.classToolMap.get(typedClass), sqlCache, sql().executor(), dbType))
        }
        return queryList.execute()
    }

    @Throws(JpoException::class)
    override fun <BEAN> find(clazz: Class<BEAN>): CustomFindQuery<BEAN> {
        return find(clazz, clazz.simpleName)
    }

    @Throws(JpoException::class)
    private fun <BEAN> find(clazz: Class<BEAN>, descriptor: ClassDescriptor<BEAN>, pks: Array<String>, pkFieldValues: Array<Any>): FindQuery<BEAN> {
        val findQuery = FindQueryImpl(clazz, pkFieldValues, serviceCatalog.classToolMap.get(clazz), sql().executor(), sqlFactory, sqlCache)
        return findQuery
    }

    @Throws(JpoException::class)
    override fun <BEAN> find(clazz: Class<BEAN>, alias: String): CustomFindQuery<BEAN> {
        val query = CustomFindQueryImpl(clazz, alias, serviceCatalog.classToolMap.get(clazz), sql().executor(), sqlFactory)
        return query
    }

    override fun <BEAN> find(vararg selectFields: String): CustomResultFindQueryBuilder {
        return CustomResultFindQueryBuilderImpl(selectFields, sql().executor(), sqlFactory)
    }

    @Throws(JpoException::class)
    override fun <BEAN> findById(clazz: Class<BEAN>, value: Any): FindQuery<BEAN> {
        val ormClassTool = serviceCatalog.classToolMap.get(clazz)
        val descriptor = ormClassTool.descriptor
        val pks = descriptor.primaryKeyColumnJavaNames
        return this.find(clazz, descriptor, pks, arrayOf(value))
    }

    @Throws(JpoException::class)
    override fun <BEAN> findByModelId(model: BEAN): FindQuery<BEAN> {
        val modelClass = model.javaClass as Class<BEAN>
        val ormClassTool = serviceCatalog.classToolMap.get(modelClass)
        val descriptor = ormClassTool.descriptor
        val pks = descriptor.primaryKeyColumnJavaNames
        val values = ormClassTool.persistor.getPropertyValues(pks, model)
        return find(modelClass, descriptor, pks, values)
    }

    override fun <BEAN> save(bean: BEAN): BEAN {
        return saveQuery(bean).execute()[0]
    }

    @Throws(JpoException::class)
    override fun <BEAN> save(clazz: Class<BEAN>, vararg fields: String): CustomSaveQuery {
        val update = CustomSaveQueryImpl<Any>(sqlFactory.insertInto(clazz, fields), sql().executor())
        return update
    }

    @Throws(JpoException::class)
    override fun <BEAN> save(beans: Collection<BEAN>): List<BEAN> {
        return saveQuery(beans).execute()
    }

    @Throws(JpoException::class)
    override fun <BEAN> saveOrUpdate(bean: BEAN): BEAN {
        return saveOrUpdateQuery(bean).execute()[0]
    }

    @Throws(JpoException::class)
    override fun <BEAN> saveOrUpdate(beans: Collection<BEAN>): List<BEAN> {
        serviceCatalog.validatorService.validateThrowException(beans)

        val queryList = SaveOrUpdateQueryListDecorator<BEAN>()
        val beansByClass = beans.stream().collect<Map<Class<*>, List<BEAN>>, Any>(Collectors.groupingBy<BEAN, Class<*>>(Function<BEAN, Class<*>> { it.javaClass }))
        beansByClass.forEach { clazz, classBeans ->
            val clazzBean = clazz as Class<BEAN>
            val persistor = serviceCatalog.classToolMap.get(clazzBean).persistor
            classBeans.forEach { classBean -> queryList.add(saveOrUpdateQuery(classBean, persistor)) }
        }
        return queryList.execute()
    }

    @Throws(JpoException::class)
    private fun <BEAN> saveOrUpdateQuery(bean: BEAN): SaveOrUpdateQuery<BEAN> {
        serviceCatalog.validatorService.validateThrowException(bean)
        val clazz = bean.javaClass as Class<BEAN>
        val ormClassTool = serviceCatalog.classToolMap.get(clazz)
        return saveOrUpdateQuery(bean, ormClassTool.persistor)
    }

    private fun <BEAN> saveOrUpdateQuery(bean: BEAN, persistor: Persistor<BEAN>): SaveOrUpdateQuery<BEAN> {
        if (toBeSaved(bean, persistor)) {
            return saveQuery(bean)
        }
        return updateQuery(bean)
    }

    private fun <BEAN> saveQuery(bean: BEAN): SaveQuery<BEAN> {
        serviceCatalog.validatorService.validateThrowException(bean)
        val clazz = bean.javaClass as Class<BEAN>
        return SaveQueryImpl(Arrays.asList(bean), clazz, serviceCatalog.classToolMap.get(clazz), sqlCache, sql().executor(), sqlFactory, dbType)
    }

    @Throws(JpoException::class)
    private fun <BEAN> saveQuery(beans: Collection<BEAN>): SaveOrUpdateQuery<BEAN> {
        serviceCatalog.validatorService.validateThrowException(beans)
        val queryList = SaveOrUpdateQueryListDecorator<BEAN>()
        val beansByClass = beans.stream().collect<Map<Class<*>, List<BEAN>>, Any>(Collectors.groupingBy<BEAN, Class<*>>(Function<BEAN, Class<*>> { it.javaClass }))
        beansByClass.forEach { clazz, classBeans ->
            val typedClass = clazz as Class<BEAN>
            queryList.add(SaveQueryImpl(classBeans, typedClass, serviceCatalog.classToolMap.get(typedClass), sqlCache, sql().executor(), sqlFactory, dbType))
        }
        return queryList
    }

    @Throws(JpoException::class)
    override fun scriptExecutor(): ScriptExecutor {
        return ScriptExecutorImpl(this)
    }

    @Throws(JpoException::class)
    override fun sql(): SqlSession {
        return sqlSession
    }

    /**
     * Returns whether a bean has to be saved. Otherwise it has to be updated
     * because it already exists.

     * @return
     */
    private fun <BEAN> toBeSaved(bean: BEAN, persistor: Persistor<BEAN>): Boolean {
        if (persistor.hasGenerator()) {
            return persistor.useGenerators(bean)
        } else {
            return !findByModelId(bean).exist()
        }
    }

    @Throws(JpoException::class)
    override fun <BEAN> update(bean: BEAN): BEAN {
        return updateQuery(bean).execute()[0]
    }

    @Throws(JpoException::class)
    override fun <BEAN> update(clazz: Class<BEAN>): CustomUpdateQuery {
        val update = CustomUpdateQueryImpl(sqlFactory.update(clazz), sql().executor())
        return update
    }

    @Throws(JpoException::class)
    override fun <BEAN> update(beans: Collection<BEAN>): List<BEAN> {
        serviceCatalog.validatorService.validateThrowException(beans)
        val queryList = SaveOrUpdateQueryListDecorator<BEAN>()
        val beansByClass = beans.stream().collect<Map<Class<*>, List<BEAN>>, Any>(Collectors.groupingBy<BEAN, Class<*>>(Function<BEAN, Class<*>> { it.javaClass }))
        beansByClass.forEach { clazz, classBeans ->
            val typedClass = clazz as Class<BEAN>
            queryList.add(UpdateQueryImpl(classBeans, typedClass, serviceCatalog.classToolMap.get(typedClass), sqlCache, sql().executor(), dbType))
        }
        return queryList.execute()
    }

    @Throws(JpoException::class)
    private fun <BEAN> updateQuery(bean: BEAN): UpdateQuery<BEAN> {
        serviceCatalog.validatorService.validateThrowException(bean)
        val typedClass = bean.javaClass as Class<BEAN>
        return UpdateQueryImpl(Arrays.asList(bean), typedClass, serviceCatalog.classToolMap.get(typedClass), sqlCache, sql().executor(), dbType)
    }

}
