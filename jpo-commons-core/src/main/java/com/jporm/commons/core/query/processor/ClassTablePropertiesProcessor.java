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
package com.jporm.commons.core.query.processor;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.jporm.annotation.exception.JpoWrongPropertyNameException;
import com.jporm.annotation.mapper.clazz.ClassDescriptor;
import com.jporm.commons.core.inject.ClassToolMap;
import com.jporm.sql.query.processor.TableName;
import com.jporm.sql.query.processor.TableNameImpl;
import com.jporm.sql.query.processor.TablePropertiesProcessor;

/**
 *
 * @author Francesco Cina
 *
 *         22/giu/2011
 */
public class ClassTablePropertiesProcessor implements TablePropertiesProcessor<Class<?>> {

    // public static String FIND_ALL_PROPERTY_PATTERN =
    // "[a-zA-Z_0-9]+[\\.][a-zA-Z_0-9]+[\\.][a-zA-Z_0-9]+|[a-zA-Z_0-9]+[\\.][a-zA-Z_0-9]+";
    // //$NON-NLS-1$

    private static String EXPRESSIONS = ",|=|<|>| like | in | not | or | and ";

    public static String FIND_ALL_PROPERTY_PATTERN = "(?<=[(]+|^[ ]*|" + EXPRESSIONS + ")[\\s]*[a-zA-Z]+[\\.a-zA-Z_0-9]*(?!'|\"|[(\\.a-zA-Z_0-9]+)";
//    private static final Set<String> KEYWORDS = new TreeSet<>(Arrays.asList("ORDER", "BY", "SELECT", "null"));
    private static Pattern patternProperties = Pattern.compile(FIND_ALL_PROPERTY_PATTERN, Pattern.CASE_INSENSITIVE);

    private static final int MAX_ALIAS_LENGHT = 25;
    private static final String SEPARATOR = "_"; //$NON-NLS-1$

    private final Map<String, ClassDescriptor<?>> registeredClass = new ConcurrentHashMap<String, ClassDescriptor<?>>();
    private final Map<String, String> normalizedAliases = new ConcurrentHashMap<String, String>();
    private final List<String> dynamicAliases = new ArrayList<>();
    private String defaultAlias = null;
    private int registeredClassCount = 0;
    private final boolean alwaysResolveWithoutAlias;
    private final PropertiesFactory propertiesFactory;
    private final ClassToolMap classDescriptorMap;
    /**
     *
     * @param serviceCatalog
     * @param alwaysResolveWithoutAlias
     *            If set to true always resolves the properties name without
     *            prepend the table name alias, even if the solvePropertyName is
     *            called
     */
    public ClassTablePropertiesProcessor(final ClassToolMap classDescriptorMap, final PropertiesFactory propertiesFactory, final boolean alwaysResolveWithoutAlias) {
        this.classDescriptorMap = classDescriptorMap;
        this.propertiesFactory = propertiesFactory;
        this.alwaysResolveWithoutAlias = alwaysResolveWithoutAlias;
    }

    private String getDbColumn(final String alias, final String field) {
        String dbColumn = registeredClass.get(alias).getFieldDescriptorByJavaName(field).getColumnInfo().getDBColumnName();
        if (dbColumn.isEmpty()) {
            throw new JpoWrongPropertyNameException("Field with name [" + field + "] is not present or ignored for alias [" + alias + "]"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
        }
        return dbColumn;
    }

    private String normalizeAlias(final String alias, final Integer classId) {
        String normalized = alias;
        if (normalized.length() > MAX_ALIAS_LENGHT) {
            normalized = normalized.substring(0, MAX_ALIAS_LENGHT);
        }
        return normalized + SEPARATOR + classId;
    }


    @Override
    public TableName getTableName(Class<?> source) {
        return getTableName(source, source.getSimpleName());
    }

    @Override
    public TableName getTableName(Class<?> source, String alias) {
        ClassDescriptor<?> classDescriptor = classDescriptorMap.get(source).getDescriptor();
        String normalizedAlias = register(source, alias, classDescriptor);
        String table = classDescriptor.getTableInfo().getTableNameWithSchema();
        return new TableNameImpl(table, normalizedAlias);
    }

    private String register(final Class<?> clazz, String alias, final ClassDescriptor<?> classDescriptor) {
        if ((alias == null) || alias.isEmpty()) {
            alias = clazz.getSimpleName();
        }
        Integer classId = registeredClassCount++;
        registeredClass.put(alias, classDescriptor);
        String normalizedAlias = normalizeAlias(alias, classId);
        normalizedAliases.put(alias, normalizedAlias);
        if (defaultAlias == null) {
            defaultAlias = alias;
        }
        return normalizedAlias;
    }

    @Override
    public void solveAllPropertyNames(final String input, final StringBuilder outputBuilder) {
        final Matcher m = patternProperties.matcher(input);
        int beginIndex = 0;

        while (m.find()) {
            outputBuilder.append(input.substring(beginIndex, m.start()));
            outputBuilder.append(solvePropertyName(m.group().trim()));
            beginIndex = m.end();
        }

        outputBuilder.append(input.substring(beginIndex, input.length()));
    }



    @Override
    public String solvePropertyName(final String propertyName) {
//        if (KEYWORDS.contains(propertyName)) {
//            return propertyName;
//        }
        final Property property = propertiesFactory.property(propertyName);
        final String alias = property.getAlias(defaultAlias);
        final String field = property.getField();
        if (registeredClass.containsKey(alias)) {
            final String dbColumn = getDbColumn(alias, field);
            if (alwaysResolveWithoutAlias) {
                return dbColumn;
            }
            return normalizedAliases.get(alias) + "." + dbColumn;
        }
        if (dynamicAliases.contains(alias)) {
            return propertyName;
        }
        throw new JpoWrongPropertyNameException(
                "Alias [" + alias + "] is not associated with an Orm Entity. Registered alias are: " + registeredClass.keySet()); //$NON-NLS-1$ //$NON-NLS-2$
    }

    @Override
    public void addDynamicAlias(String alias) {
        dynamicAliases.add(alias);
    }

}
