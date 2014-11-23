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
package com.jporm.deprecated.core.mapper.clazz;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jporm.annotation.Generator;
import com.jporm.annotation.Id;
import com.jporm.annotation.Ignore;
import com.jporm.core.inject.ServiceCatalog;
import com.jporm.core.persistor.reflection.FieldGetManipulator;
import com.jporm.core.persistor.reflection.FieldSetManipulator;
import com.jporm.core.persistor.reflection.GetManipulator;
import com.jporm.core.persistor.reflection.GetterGetManipulator;
import com.jporm.core.persistor.reflection.SetManipulator;
import com.jporm.core.persistor.reflection.SetterSetManipulator;
import com.jporm.exception.OrmConfigurationException;
import com.jporm.introspector.annotation.cache.CacheInfo;
import com.jporm.introspector.annotation.cache.CacheInfoFactory;
import com.jporm.introspector.annotation.column.ColumnInfoFactory;
import com.jporm.introspector.annotation.generator.GeneratorInfoFactory;
import com.jporm.introspector.annotation.table.TableInfo;
import com.jporm.introspector.annotation.table.TableInfoFactory;
import com.jporm.introspector.annotation.version.VersionInfoFactory;
import com.jporm.introspector.util.FieldDefaultNaming;

/**
 *
 * @author Francesco Cina
 *
 * 22/mag/2011
 */
public class ClassMapBuilderImpl<BEAN> implements ClassMapBuilder<BEAN> {

	private final Class<BEAN> mainClazz;
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	private final ServiceCatalog serviceCatalog;

	public ClassMapBuilderImpl(final Class<BEAN> clazz, final ServiceCatalog serviceCatalog) {
		this.mainClazz = clazz;
		this.serviceCatalog = serviceCatalog;
	}

	@Override
	public ClassMap<BEAN> generate() {
		this.logger.debug("generate " + ClassMap.class.getSimpleName() + " for Class " + this.mainClazz.getName()); //$NON-NLS-1$ //$NON-NLS-2$
		TableInfo tableInfo = TableInfoFactory.getTableInfo(this.mainClazz);
		this.logger.debug("Table name expected in relation with class " + this.mainClazz.getSimpleName() + ": " + tableInfo.getTableName() + " - schema: " + tableInfo.getSchemaName()); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		CacheInfo cacheInfo = CacheInfoFactory.getCacheInfo(this.mainClazz);
		if (cacheInfo.isCacheable()) {
			this.logger.debug("Cache [{}] will be used for Beans of type [{}]", cacheInfo.getCacheName(), this.mainClazz.getSimpleName()); //$NON-NLS-1$
		}
		final ClassMapImpl<BEAN> classMap = new ClassMapImpl<BEAN>(this.mainClazz, tableInfo, cacheInfo);
		this.initializeClassFields(classMap);
		this.initializeColumnNames(classMap);
		return classMap ;
	}

	private void initializeClassFields(final ClassMapImpl<BEAN> classMap) {
		final List<Method> methods = Arrays.asList( this.mainClazz.getMethods() );
		final List<Field> fields = this.getAllInheritedFields(this.mainClazz);

		for (Field field : fields) {
			if (!field.isAnnotationPresent(Ignore.class) && !Modifier.isStatic( field.getModifiers() ) ) {
				if (serviceCatalog.getTypeFactory().isWrappedType(field.getType())) {
					classMap.addClassField(this.buildClassField(classMap, field, methods, field.getType()));
				} else {
					throw new OrmConfigurationException("Field [" + field.getName() + "] of class [" + this.mainClazz.getCanonicalName() + "] is not of a valid type"); //$NON-NLS-1$ //$NON-NLS-2$
				}
			}
		}
	}


	private <P> ClassFieldImpl<BEAN, P> buildClassField(final ClassMapImpl<BEAN> classMap, final Field field, final List<Method> methods, final Class<P> fieldClass) {
		ClassFieldImpl<BEAN, P> classField = new ClassFieldImpl<BEAN, P>(fieldClass, field.getName());
		setCommonClassField(classField, field, methods, fieldClass);

		this.logger.debug( "DB column [" + classField.getColumnInfo().getDBColumnName() + "]" + " will be associated with object field [" + classField.getFieldName() + "]"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$

		return classField;
	}

	private <P> void setCommonClassField(final ClassFieldImpl<BEAN, P> classField, final Field field, final List<Method> methods, final Class<P> fieldClass) {
		classField.setColumnInfo( ColumnInfoFactory.getColumnInfo(field) );
		classField.setIdentifier(field.isAnnotationPresent(Id.class));
		classField.setGetManipulator(this.getGetManipulator(field, methods, fieldClass));
		classField.setSetManipulator(this.getSetManipulator(field, methods, fieldClass));
		classField.setGeneratorInfo(GeneratorInfoFactory.getGeneratorInfo(field));
		classField.setVersionInfo(VersionInfoFactory.getVersionInfo(field));
	}


	private <P> GetManipulator<BEAN, P> getGetManipulator(final Field field, final List<Method> methods, final Class<P> clazz) {

		Method getter = null;
		String getterName = ""; //$NON-NLS-1$

		for (final Method method : methods) {
			if (FieldDefaultNaming.getDefaultGetterName(field.getName()).equals(method.getName())) {
				getter = method;
				getterName = method.getName();
			}
			if (FieldDefaultNaming.getDefaultBooleanGetterName(field.getName()).equals(method.getName())) {
				getter = method;
				getterName = method.getName();
			}
		}

		this.logger.debug("getter for property [" + field.getName() + "]: [" + getterName + "]"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

		if ( getter != null) {
			return new GetterGetManipulator<BEAN, P>(getter);
		}
		return new FieldGetManipulator<BEAN, P>(field);


	}

	private <P> SetManipulator<BEAN, P> getSetManipulator(final Field field, final List<Method> methods, final Class<P> clazz) {

		Method setter = null;
		String setterName = ""; //$NON-NLS-1$

		for (final Method method : methods) {
			if (FieldDefaultNaming.getDefaultSetterName(field.getName()).equals(method.getName())) {
				setter = method;
				setterName = method.getName();
			}
		}

		this.logger.debug("setter for property [" + field.getName() + "]: [" + setterName + "]"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

		if ( setter != null) {
			return new SetterSetManipulator<BEAN, P>(setter);
		}
		return new FieldSetManipulator<BEAN, P>(field);

	}

	private void initializeColumnNames(final ClassMapImpl<BEAN> classMap) {

		this.logger.debug("Start column analisys for Class " ); //$NON-NLS-1$

		final List<String> allColumnJavaNamesList = new ArrayList<String>();
		final List<String> allNotGeneratedColumnJavaNamesList = new ArrayList<String>();
		final List<String> allGeneratedColumnJavaNamesList = new ArrayList<String>();
		final List<String> allGeneratedColumnDBNamesList = new ArrayList<String>();
		final List<String> primaryKeyColumnJavaNamesList = new ArrayList<String>();
		final List<String> primaryKeyAndVersionColumnJavaNamesList = new ArrayList<String>();
		final List<String> notPrimaryKeyColumnJavaList = new ArrayList<String>();

		boolean hasGenerator = false;

		for (final Entry<String, ClassFieldImpl<BEAN, ?>> entry : classMap.getUnmodifiableFieldClassMap().entrySet()) {

			final String javaFieldName = entry.getKey();
			allColumnJavaNamesList.add(javaFieldName);

			if( entry.getValue().isIdentifier() ) {
				primaryKeyColumnJavaNamesList.add(javaFieldName);
				primaryKeyAndVersionColumnJavaNamesList.add(javaFieldName);
				this.logger.debug("Field [" + javaFieldName + "] will be used as a Primary Key field"); //$NON-NLS-1$ //$NON-NLS-2$
			} else {
				notPrimaryKeyColumnJavaList.add(javaFieldName);
				this.logger.debug("Field [" + javaFieldName + "] will be used as a normal field"); //$NON-NLS-1$ //$NON-NLS-2$
			}

			if ( entry.getValue().getGeneratorInfo().isValid() ) {
				if (!hasGenerator) {
					allGeneratedColumnJavaNamesList.add(javaFieldName);
					allGeneratedColumnDBNamesList.add(entry.getValue().getColumnInfo().getDBColumnName());
					this.logger.debug("Field [" + javaFieldName + "] is an autogenerated field"); //$NON-NLS-1$ //$NON-NLS-2$
					hasGenerator=true;
				}
				else {
					throw new OrmConfigurationException("A bean can have maximum one field annotated with @" + Generator.class.getSimpleName() + ". Error in class:[" + this.mainClazz.getCanonicalName() + "] field: [" + javaFieldName + "]"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
				}
			} else {
				allNotGeneratedColumnJavaNamesList.add(javaFieldName);
			}

			if (  entry.getValue().getVersionInfo().isVersionable() ) {
				this.logger.debug("Field [" + javaFieldName + "] is an version field"); //$NON-NLS-1$ //$NON-NLS-2$
				primaryKeyAndVersionColumnJavaNamesList.add(javaFieldName);
			}

		}

		classMap.setAllColumnJavaNames( allColumnJavaNamesList.toArray(new String[0]) );
		classMap.setAllNotGeneratedColumnJavaNames( allNotGeneratedColumnJavaNamesList.toArray(new String[0]) );
		classMap.setAllGeneratedColumnJavaNames( allGeneratedColumnJavaNamesList.toArray(new String[0]) );
		classMap.setAllGeneratedColumnDBNames( allGeneratedColumnDBNamesList.toArray(new String[0]) );
		classMap.setNotPrimaryKeyColumnJavaNames( notPrimaryKeyColumnJavaList.toArray(new String[0]) );
		classMap.setPrimaryKeyColumnJavaNames( primaryKeyColumnJavaNamesList.toArray(new String[0]) );
		classMap.setPrimaryKeyAndVersionColumnJavaNames( primaryKeyAndVersionColumnJavaNamesList.toArray(new String[0]) );
	}

	private List<Field> getAllInheritedFields(final Class<?> type) {
		final List<Field> fields = new ArrayList<Field>();
		for (Class<?> c = type; c != null; c = c.getSuperclass()) {
			fields.addAll(Arrays.asList(c.getDeclaredFields()));
		}
		return fields;
	}
}
