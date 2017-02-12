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

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map.Entry;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jporm.annotation.Column;
import com.jporm.annotation.Generator;
import com.jporm.annotation.GeneratorType;
import com.jporm.annotation.Id;
import com.jporm.annotation.Ignore;
import com.jporm.annotation.Version;
import com.jporm.annotation.exception.JpoWrongAnnotationException;
import com.jporm.annotation.introspector.column.AnnotationColumnInfo;
import com.jporm.annotation.introspector.column.InferedColumnName;
import com.jporm.annotation.introspector.generator.GeneratorInfoImpl;
import com.jporm.annotation.introspector.table.TableInfo;
import com.jporm.annotation.introspector.table.TableInfoFactory;
import com.jporm.annotation.introspector.version.VersionInfoImpl;
import com.jporm.annotation.mapper.FieldDefaultNaming;
import com.jporm.annotation.mapper.ReflectionUtils;

/**
 *
 * @author Francesco Cina
 *
 *         22/mag/2011
 */
public class ClassDescriptorBuilderImpl<BEAN> implements ClassDescriptorBuilder<BEAN> {

	private final Class<BEAN> mainClazz;
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	private final List<String> ignoredFieldNames;

	public ClassDescriptorBuilderImpl(final Class<BEAN> clazz) {
		this(clazz, Collections.emptyList());
	}

	public ClassDescriptorBuilderImpl(final Class<BEAN> clazz, List<String> ignoredFieldNames) {
		this.mainClazz = clazz;
		this.ignoredFieldNames = ignoredFieldNames;
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

	private <P> Optional<Method> getGetter(final Field field, final List<Method> methods) {
		Method getter = null;
		String getterName = "";
		final List<String> validNames = Arrays.asList(
				FieldDefaultNaming.getDefaultGetterName(field.getName()),
				FieldDefaultNaming.getDefaultBooleanGetterName(field.getName()),
				field.getName()
				);
		for (final Method method : methods) {
			if (validNames.contains(method.getName()) && isValidGetter(field, method)) {
				getter = method;
				getterName = method.getName();
				break;
			}
		}
		this.logger.debug("getter for property [" + field.getName() + "]: [" + getterName + "]"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		return Optional.ofNullable(getter);
	}

	private boolean isValidGetter(Field field, Method method) {
		return field.getType().isAssignableFrom(method.getReturnType()) && method.getParameterTypes().length == 0;
	}

	private Optional<Method> getSetter(final Field field, final List<Method> methods, final Class<BEAN> clazz) {
		Method setter = null;
		String setterName = "";
		final List<String> validNames = Arrays.asList(
				FieldDefaultNaming.getDefaultSetterName(field.getName()),
				FieldDefaultNaming.getDefaultWitherName(field.getName()),
				field.getName()
				);
		for (final Method method : methods) {
			if (validNames.contains(method.getName()) && isValidSetter(field, method, clazz)) {
				setter = method;
				setterName = method.getName();
				break;
			}
		}

		this.logger.debug("setter for property [" + field.getName() + "]: [" + setterName + "]"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		return Optional.ofNullable(setter);
	}

	private <P> boolean isValidSetter(final Field field, Method method, final Class<P> clazz) {
		final Class<?>[] params = method.getParameterTypes();
		final Class<?> returnType = method.getReturnType();
		return params.length == 1 && params[0].isAssignableFrom(field.getType()) && (returnType.equals(Void.TYPE) || clazz.isAssignableFrom(returnType) );
	}

	private void initializeClassFields(final ClassDescriptorImpl<BEAN> classMap) {
		final List<Method> methods = Arrays.asList(this.mainClazz.getMethods());
		final List<Field> fields = ReflectionUtils.getAllInheritedFields(this.mainClazz);

		for (final Field field : fields) {
			if (!ReflectionUtils.isStatic(field) && !ignoredFieldNames.contains(field.getName())) {
				final FieldDescriptorImpl<BEAN, ?> classField = this.buildClassField(classMap, field, methods, field.getType());
				if (!classField.isIgnored()) {
					classMap.addClassField(classField);
				}
			}
		}
	}

	@SuppressWarnings("rawtypes")
	private <P> FieldDescriptorImpl<BEAN, P> buildClassField(final ClassDescriptorImpl<BEAN> classMap, final Field field, final List<Method> methods,
			final Class<P> fieldClass) {

		FieldDescriptorImpl<BEAN, P> classField;
		final Type type = field.getGenericType();
		if (type instanceof ParameterizedType) {
			final ParameterizedType ptype = (ParameterizedType) type;
			classField = new FieldDescriptorImpl<>(field, field.getName(), (Class) ptype.getRawType());
			classField.setGenericArgumentType(Optional.ofNullable((Class) ptype.getActualTypeArguments()[0]));
		} else {
			classField = new FieldDescriptorImpl<>(field, field.getName(), (Class) field.getType());
		}

		classField.setGetter(getGetter(field, methods));
		classField.setSetter(getSetter(field, methods, classMap.getMappedClass()));
		setIgnored(classField);
		setColumnInfo(classField);
		setIdentifier(classField);
		setGeneratorInfo(classField);
		setVersionInfo(classField);

		this.logger.debug("DB column [" + classField.getColumnInfo().getDBColumnName() + "]" + " will be associated with object field [" //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
				+ classField.getFieldName() + "]"); //$NON-NLS-1$

		return classField;
	}

	private <P> void setIgnored(FieldDescriptorImpl<BEAN, P> classField) {
		classField.setIgnored(findAnnotation(classField, Ignore.class).isPresent());
	}

	private <P> void setColumnInfo(FieldDescriptorImpl<BEAN, P> classField) {
		classField.setColumnInfo(new InferedColumnName(classField.getFieldName()));
		findAnnotation(classField, Column.class)
		.ifPresent(column -> {
			classField.setColumnInfo(new AnnotationColumnInfo(column.name()));
		});
	}

	private <P> void setIdentifier(FieldDescriptorImpl<BEAN, P> classField) {
		classField.setIdentifier(findAnnotation(classField, Id.class).isPresent());
	}

	private <P> void setGeneratorInfo(FieldDescriptorImpl<BEAN, P> classField) {
		classField.setGeneratorInfo(new GeneratorInfoImpl(GeneratorType.NONE, "", false));
		final Optional<Generator> generator = findAnnotation(classField, Generator.class);
		if (generator.isPresent()) {
			classField.setGeneratorInfo(new GeneratorInfoImpl(generator.get().generatorType(), generator.get().name(), true));
		}
	}

	private <P> void setVersionInfo(FieldDescriptorImpl<BEAN, P> classField) {
		classField.setVersionInfo(new VersionInfoImpl(findAnnotation(classField, Version.class).isPresent()));
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

	private <A extends Annotation, P> Optional<A> findAnnotation(FieldDescriptorImpl<BEAN, P> classField, Class<A> annotationType) {
		return ReflectionUtils.findAnnotation(classField.getRawType(), classField.getField(), classField.getGetter(), classField.getSetter(), annotationType);
	}

}
