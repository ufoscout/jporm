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
package com.jporm.annotation.mapper.clazz;

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
import com.jporm.annotation.exception.JpoWrongAnnotationException;
import com.jporm.annotation.introspector.column.ColumnInfoFactory;
import com.jporm.annotation.introspector.generator.GeneratorInfoFactory;
import com.jporm.annotation.introspector.table.TableInfo;
import com.jporm.annotation.introspector.table.TableInfoFactory;
import com.jporm.annotation.introspector.version.VersionInfoFactory;
import com.jporm.annotation.mapper.FieldDefaultNaming;

/**
 *
 * @author Francesco Cina
 *
 *         22/mag/2011
 */
public class ClassDescriptorBuilderImpl<BEAN> implements ClassDescriptorBuilder<BEAN> {

	private final Class<BEAN> mainClazz;
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	public ClassDescriptorBuilderImpl(final Class<BEAN> clazz) {
		this.mainClazz = clazz;
	}

	@Override
	public ClassDescriptor<BEAN> build() {
		this.logger.debug("generate " + ClassDescriptor.class.getSimpleName() + " for Class " + this.mainClazz.getName()); //$NON-NLS-1$ //$NON-NLS-2$
		final TableInfo tableInfo = TableInfoFactory.getTableInfo(this.mainClazz);
		this.logger.debug("Table name expected in relation with class " + this.mainClazz.getSimpleName() + ": " + tableInfo.getTableName() + " - schema: " //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
				+ tableInfo.getSchemaName());
		final ClassDescriptorImpl<BEAN> classMap = new ClassDescriptorImpl<>(this.mainClazz, tableInfo);
		this.initializeClassFields(classMap);
		this.initializeColumnNames(classMap);
		return classMap;
	}

	private <P> FieldDescriptorImpl<BEAN, P> buildClassField(final ClassDescriptorImpl<BEAN> classMap, final Field field, final List<Method> methods,
			final Class<P> fieldClass) {
		final FieldDescriptorImpl<BEAN, P> classField = new FieldDescriptorImpl<>(field, fieldClass);
		setCommonClassField(classField, field, methods, fieldClass);

		this.logger.debug("DB column [" + classField.getColumnInfo().getDBColumnName() + "]" + " will be associated with object field [" //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
				+ classField.getFieldName() + "]"); //$NON-NLS-1$

		return classField;
	}

	private List<Field> getAllInheritedFields(final Class<?> type) {
		final List<Field> fields = new ArrayList<>();
		for (Class<?> c = type; c != null; c = c.getSuperclass()) {
			fields.addAll(Arrays.asList(c.getDeclaredFields()));
		}
		return fields;
	}

	private <P> Method getGetter(final Field field, final List<Method> methods, final Class<P> clazz) {
		Method getter = null;
		String getterName = "";

		for (final Method method : methods) {
			if (FieldDefaultNaming.getDefaultGetterName(field.getName()).equals(method.getName())) {
				getter = method;
				getterName = method.getName();
				break;
			}
			if (FieldDefaultNaming.getDefaultBooleanGetterName(field.getName()).equals(method.getName())) {
				getter = method;
				getterName = method.getName();
				break;
			}
			if (field.getName().equals(method.getName())) {
				getter = method;
				getterName = method.getName();
				break;
			}
		}
		this.logger.debug("getter for property [" + field.getName() + "]: [" + getterName + "]"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		return getter;
	}

	private <P> Method getSetter(final Field field, final List<Method> methods, final Class<P> clazz) {
		Method setter = null;
		String setterName = ""; //$NON-NLS-1$

		for (final Method method : methods) {
			if (FieldDefaultNaming.getDefaultSetterName(field.getName()).equals(method.getName())) {
				setter = method;
				setterName = method.getName();
				break;
			}
			if (FieldDefaultNaming.getDefaultWitherName(field.getName()).equals(method.getName())) {
				setter = method;
				setterName = method.getName();
				break;
			}
		}

		this.logger.debug("setter for property [" + field.getName() + "]: [" + setterName + "]"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		return setter;
	}

	private void initializeClassFields(final ClassDescriptorImpl<BEAN> classMap) {
		final List<Method> methods = Arrays.asList(this.mainClazz.getMethods());
		final List<Field> fields = this.getAllInheritedFields(this.mainClazz);

		for (final Field field : fields) {
			if (!field.isAnnotationPresent(Ignore.class) && !Modifier.isStatic(field.getModifiers())) {
				classMap.addClassField(this.buildClassField(classMap, field, methods, field.getType()));
			}
		}
	}

	private void initializeColumnNames(final ClassDescriptorImpl<BEAN> classMap) {

		this.logger.debug("Start column analisys for Class "); //$NON-NLS-1$

		final List<String> allColumnJavaNamesList = new ArrayList<>();
		final List<String> allNotGeneratedColumnJavaNamesList = new ArrayList<>();
		final List<String> allGeneratedColumnJavaNamesList = new ArrayList<>();
		final List<String> allGeneratedColumnDBNamesList = new ArrayList<>();
		final List<String> primaryKeyColumnJavaNamesList = new ArrayList<>();
		final List<String> primaryKeyAndVersionColumnJavaNamesList = new ArrayList<>();
		final List<String> notPrimaryKeyColumnJavaList = new ArrayList<>();

		boolean hasGenerator = false;

		for (final Entry<String, FieldDescriptorImpl<BEAN, ?>> entry : classMap.getUnmodifiableFieldClassMap().entrySet()) {

			final String javaFieldName = entry.getKey();
			allColumnJavaNamesList.add(javaFieldName);

			if (entry.getValue().isIdentifier()) {
				primaryKeyColumnJavaNamesList.add(javaFieldName);
				primaryKeyAndVersionColumnJavaNamesList.add(javaFieldName);
				this.logger.debug("Field [" + javaFieldName + "] will be used as a Primary Key field"); //$NON-NLS-1$ //$NON-NLS-2$
			} else {
				notPrimaryKeyColumnJavaList.add(javaFieldName);
				this.logger.debug("Field [" + javaFieldName + "] will be used as a normal field"); //$NON-NLS-1$ //$NON-NLS-2$
			}

			if (entry.getValue().getGeneratorInfo().isValid()) {
				if (!hasGenerator) {
					allGeneratedColumnJavaNamesList.add(javaFieldName);
					allGeneratedColumnDBNamesList.add(entry.getValue().getColumnInfo().getDBColumnName());
					this.logger.debug("Field [" + javaFieldName + "] is an autogenerated field"); //$NON-NLS-1$ //$NON-NLS-2$
					hasGenerator = true;
				} else {
					throw new JpoWrongAnnotationException("A bean can have maximum one field annotated with @" + Generator.class.getSimpleName() //$NON-NLS-1$
							+ ". Error in class:[" + this.mainClazz.getCanonicalName() + "] field: [" + javaFieldName + "]"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
				}
			} else {
				allNotGeneratedColumnJavaNamesList.add(javaFieldName);
			}

			if (entry.getValue().getVersionInfo().isVersionable()) {
				this.logger.debug("Field [" + javaFieldName + "] is an version field"); //$NON-NLS-1$ //$NON-NLS-2$
				primaryKeyAndVersionColumnJavaNamesList.add(javaFieldName);
			}

		}

		classMap.setAllColumnJavaNames(allColumnJavaNamesList.toArray(new String[0]));
		classMap.setAllNotGeneratedColumnJavaNames(allNotGeneratedColumnJavaNamesList.toArray(new String[0]));
		classMap.setAllGeneratedColumnJavaNames(allGeneratedColumnJavaNamesList.toArray(new String[0]));
		classMap.setAllGeneratedColumnDBNames(allGeneratedColumnDBNamesList.toArray(new String[0]));
		classMap.setNotPrimaryKeyColumnJavaNames(notPrimaryKeyColumnJavaList.toArray(new String[0]));
		classMap.setPrimaryKeyColumnJavaNames(primaryKeyColumnJavaNamesList.toArray(new String[0]));
		classMap.setPrimaryKeyAndVersionColumnJavaNames(primaryKeyAndVersionColumnJavaNamesList.toArray(new String[0]));
	}

	private <P> void setCommonClassField(final FieldDescriptorImpl<BEAN, P> classField, final Field field, final List<Method> methods,
			final Class<P> fieldClass) {
		classField.setColumnInfo(ColumnInfoFactory.getColumnInfo(field));
		classField.setIdentifier(field.isAnnotationPresent(Id.class));
		classField.setGeneratorInfo(GeneratorInfoFactory.getGeneratorInfo(field));
		classField.setVersionInfo(VersionInfoFactory.getVersionInfo(field));
		classField.setGetter(getGetter(field, methods, fieldClass));
		classField.setSetter(getSetter(field, methods, fieldClass));
	}
}
