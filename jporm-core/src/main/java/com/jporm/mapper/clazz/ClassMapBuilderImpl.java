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

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jporm.annotation.FK;
import com.jporm.annotation.Generator;
import com.jporm.annotation.Id;
import com.jporm.annotation.Ignore;
import com.jporm.annotation.cache.CacheInfo;
import com.jporm.annotation.cache.CacheInfoFactory;
import com.jporm.annotation.cascade.CascadeInfoFactory;
import com.jporm.annotation.column.ColumnInfoFactory;
import com.jporm.annotation.generator.GeneratorInfoFactory;
import com.jporm.annotation.table.TableInfo;
import com.jporm.annotation.table.TableInfoFactory;
import com.jporm.annotation.version.VersionInfoFactory;
import com.jporm.exception.OrmConfigurationException;
import com.jporm.mapper.ServiceCatalog;
import com.jporm.mapper.relation.ForeignKeyImpl;
import com.jporm.mapper.relation.RelationInnerFKImpl;
import com.jporm.mapper.relation.RelationOuterFK;
import com.jporm.mapper.relation.RelationOuterFKImpl;
import com.jporm.persistor.reflection.FieldGetManipulator;
import com.jporm.persistor.reflection.FieldSetManipulator;
import com.jporm.persistor.reflection.GetManipulator;
import com.jporm.persistor.reflection.GetterGetManipulator;
import com.jporm.persistor.reflection.SetManipulator;
import com.jporm.persistor.reflection.SetterSetManipulator;
import com.jporm.util.FieldDefaultNaming;

/**
 * 
 * @author Francesco Cina
 *
 * 22/mag/2011
 */
public class ClassMapBuilderImpl<BEAN> implements ClassMapBuilder<BEAN> {

    private final Class<BEAN> mainClazz;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private ServiceCatalog serviceCatalog;

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
                    buildRelations(field, methods, classMap);
                }
            }
        }
    }

    /**
     * @param field
     * @param methods
     * @param classMap
     */
    private <P> void buildRelations(final Field field, final List<Method> methods, final ClassMapImpl<BEAN> classMap) {
        FK fk = field.getAnnotation(FK.class);
        if (fk==null) {
            classMap.getOuterRelations().add( buildOuterRelation(field, methods, classMap.getMappedClass()) );
        } else {
            classMap.getInnerRelations().add( buildInnerRelation(field, methods, classMap.getMappedClass(), classMap) );
        }
    }

    private <P> RelationInnerFKImpl<BEAN, P> buildInnerRelation(final Field field, final List<Method> methods, final Class<BEAN> mappedClass, final ClassMapImpl<BEAN> classMap) {
        Class<P> fieldClass = (Class<P>) field.getType();
        RelationInnerFKImpl<BEAN, P> classField = new RelationInnerFKImpl<BEAN, P>(fieldClass, field.getName(), CascadeInfoFactory.getCascadeInfoInfo(field));
        setCommonClassField(classField, field, methods, fieldClass);
        classField.setRelationVersusClass(fieldClass);
        classMap.addClassField( classField );
        logger.debug("Field [{}] is an inner relation versus [{}]", field.getName(), fieldClass); //$NON-NLS-1$
        return classField;
    }

    @SuppressWarnings("unchecked")
    private <P, MANIPULATOR> RelationOuterFK<BEAN, P, MANIPULATOR> buildOuterRelation(final Field field, final List<Method> methods, final Class<BEAN> clazz) {
        boolean oneToMany = Collection.class.isAssignableFrom(field.getType());

        Class<P> relationWithClass = null;
        if (oneToMany) {
            relationWithClass = (Class<P>) ((ParameterizedType) field.getGenericType()).getActualTypeArguments()[0];
        } else {
            relationWithClass = (Class<P>) field.getType();
        }
        ClassMap<P> relationWithClassMap = serviceCatalog.getOrmClassTool(relationWithClass).getClassMap();
        if (!relationWithClassMap.getFKs().hasFKVersus(clazz)) {
            throw new OrmConfigurationException("Class [" + relationWithClass + "] does not have a foreign key versus class [" + clazz + "]. Impossible to create relation.");   //$NON-NLS-1$//$NON-NLS-2$//$NON-NLS-3$
        }

        String relationFieldName = relationWithClassMap.getFKs().versus(clazz).getBeanFieldName();
        ClassField<P, Object> relationClassField = relationWithClassMap.getClassFieldByJavaName(relationFieldName);
        String javaFieldName = field.getName();
        RelationOuterFKImpl<BEAN, P, MANIPULATOR> relation = new RelationOuterFKImpl<BEAN, P, MANIPULATOR>(relationWithClass, relationClassField, javaFieldName, oneToMany, CascadeInfoFactory.getCascadeInfoInfo(field));
        relation.setGetManipulator(this.getGetManipulator(field, methods, (Class<MANIPULATOR>) null));
        relation.setSetManipulator(this.getSetManipulator(field, methods, (Class<MANIPULATOR>) null));
        logger.debug("Field [{}] is an outer relation versus [{}]", field.getName(), relationWithClassMap.getClass().getName()); //$NON-NLS-1$
        return relation;
    }

    private <P> ClassFieldImpl<BEAN, P> buildClassField(final ClassMapImpl<BEAN> classMap, final Field field, final List<Method> methods, final Class<P> fieldClass) {
        ClassFieldImpl<BEAN, P> classField = new ClassFieldImpl<BEAN, P>(fieldClass, field.getName());
        setCommonClassField(classField, field, methods, fieldClass);

        this.logger.debug( "DB column [" + classField.getColumnInfo().getDBColumnName() + "]" + " will be associated with object field [" + classField.getFieldName() + "]"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
        checkFKs(classMap, field);

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

    /**
     * @param classMap
     * @param field
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    private <VERSUS_CLASS> void checkFKs(final ClassMapImpl<BEAN> classMap, final Field field) {
        FK fk = field.getAnnotation(FK.class);
        if (fk!=null) {
            logger.debug("Field [{}] is a Foreign Key versus [{}]", field.getName(), fk.references()); //$NON-NLS-1$
            classMap.getFKs().addFK(new ForeignKeyImpl(field.getName(), fk.references()));
        }
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
