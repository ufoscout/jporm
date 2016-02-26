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
package com.jporm.sql.query.namesolver.impl;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.Test;

import com.jporm.annotation.exception.JpoWrongPropertyNameException;
import com.jporm.annotation.mapper.clazz.ClassDescriptor;
import com.jporm.sql.dsl.query.processor.TableName;
import com.jporm.sql.dsl.query.processor.TableNameImpl;
import com.jporm.sql.dsl.query.processor.TablePropertiesProcessor;
import com.jporm.sql.query.tool.DescriptorToolMap;

/**
 *
 * @author Francesco Cina
 *
 *         22/giu/2011
 */
public class NameSolverImpl implements TablePropertiesProcessor<Class<?>> {

    // public static String FIND_ALL_PROPERTY_PATTERN =
    // "[a-zA-Z_0-9]+[\\.][a-zA-Z_0-9]+[\\.][a-zA-Z_0-9]+|[a-zA-Z_0-9]+[\\.][a-zA-Z_0-9]+";
    // //$NON-NLS-1$

    private static String EXPRESSIONS = ",|=|<|>| like | in | not | or | and ";

    public static String FIND_ALL_PROPERTY_PATTERN = "(?<=[(]+|^[ ]*|" + EXPRESSIONS + ")[\\s]*[a-zA-Z]+[\\.a-zA-Z_0-9]*(?!'|\"|[(\\.a-zA-Z_0-9]+)";

    private static Pattern patternProperties = Pattern.compile(FIND_ALL_PROPERTY_PATTERN, Pattern.CASE_INSENSITIVE);

    private static final int MAX_ALIAS_LENGHT = 25;
    private static final String SEPARATOR = "_"; //$NON-NLS-1$

    private final Map<String, ClassDescriptor<?>> registeredClass = new ConcurrentHashMap<String, ClassDescriptor<?>>();
    private final Map<String, String> normalizedAliases = new ConcurrentHashMap<String, String>();
    private String defaultAlias = null;
    private int registeredClassCount = 0;
    private final boolean alwaysResolveWithoutAlias;
    private final PropertiesFactory propertiesFactory;
    private final DescriptorToolMap classDescriptorMap;
    /**
     *
     * @param serviceCatalog
     * @param alwaysResolveWithoutAlias
     *            If set to true always resolves the properties name without
     *            prepend the table name alias, even if the solvePropertyName is
     *            called
     */
    public NameSolverImpl(final DescriptorToolMap classDescriptorMap, final PropertiesFactory propertiesFactory, final boolean alwaysResolveWithoutAlias) {
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
        final Property property = propertiesFactory.property(propertyName);
        final String alias = property.getAlias(defaultAlias);
        final String field = property.getField();
        if (!registeredClass.containsKey(alias)) {
            throw new JpoWrongPropertyNameException(
                    "Alias [" + alias + "] is not associated with an Orm Entity. Registered alias are: " + registeredClass.keySet()); //$NON-NLS-1$ //$NON-NLS-2$
        }
        final String dbColumn = getDbColumn(alias, field);
        if (alwaysResolveWithoutAlias) {
            return dbColumn;
        }
        return normalizedAliases.get(alias) + "." + dbColumn; //$NON-NLS-1$
    }

    @Test
    public void testRegex2() {
        final Pattern pattern = Pattern.compile(NameSolverImpl.FIND_ALL_PROPERTY_PATTERN);

        Matcher m = pattern.matcher("Employee.id"); //$NON-NLS-1$

        int count = 0;
        while (m.find()) {
            System.out.println("group: " + m.group() + " - start: " + m.start() + " - end: " + m.end()); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
            System.out.println("expec: Employee.id"); //$NON-NLS-1$
            assertEquals("Employee.id", m.group().trim()); //$NON-NLS-1$
            count++;
        }
        assertEquals(1, count);

        // -----------------------

        m = pattern.matcher(" count(Employee.age)"); //$NON-NLS-1$

        count = 0;
        while (m.find()) {
            System.out.println("group: " + m.group() + " - start: " + m.start() + " - end: " + m.end()); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
            System.out.println("expec: Employee.age"); //$NON-NLS-1$
            assertEquals("Employee.age", m.group().trim()); //$NON-NLS-1$
            count++;
        }
        assertEquals(1, count);

        // ------------------------

        m = pattern.matcher(" sum(old.age, young.age) as sum"); //$NON-NLS-1$
        List<String> expected = new ArrayList<String>();
        expected.add("old.age"); //$NON-NLS-1$
        expected.add("young.age"); //$NON-NLS-1$

        count = 0;
        while (m.find()) {
            System.out.println("group: " + m.group() + " - start: " + m.start() + " - end: " + m.end()); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
            System.out.println("expec: " + expected.get(count)); //$NON-NLS-1$
            assertEquals(expected.get(count), m.group().trim());
            count++;
        }
        assertEquals(expected.size(), count);

        // -----------------------

        m = pattern.matcher(" SchemaNAme.table.id"); //$NON-NLS-1$
        expected = new ArrayList<String>();
        expected.add("SchemaNAme.table.id"); //$NON-NLS-1$

        count = 0;
        while (m.find()) {
            System.out.println("group: " + m.group() + " - start: " + m.start() + " - end: " + m.end()); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
            System.out.println("expec: " + expected.get(count)); //$NON-NLS-1$
            assertEquals(expected.get(count), m.group().trim());
            count++;
        }
        assertEquals(expected.size(), count);

        // ------------------------

        m = pattern.matcher(" sum(schema.old.age, young.age, schema.table.name) as sum2 "); //$NON-NLS-1$
        expected = new ArrayList<String>();
        expected.add("schema.old.age"); //$NON-NLS-1$
        expected.add("young.age"); //$NON-NLS-1$
        expected.add("schema.table.name"); //$NON-NLS-1$

        count = 0;
        while (m.find()) {
            System.out.println("group: " + m.group() + " - start: " + m.start() + " - end: " + m.end()); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
            System.out.println("expec: " + expected.get(count)); //$NON-NLS-1$
            assertEquals(expected.get(count), m.group().trim());
            count++;
        }
        assertEquals(expected.size(), count);

    }

    @Test
    public void testRegex3() {
        final Pattern pattern = Pattern.compile(NameSolverImpl.FIND_ALL_PROPERTY_PATTERN);

        Matcher m = pattern.matcher("Employee.id"); //$NON-NLS-1$

        int count = 0;
        while (m.find()) {
            System.out.println("group: " + m.group() + " - start: " + m.start() + " - end: " + m.end()); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
            System.out.println("expec: Employee.id"); //$NON-NLS-1$
            assertEquals("Employee.id", m.group().trim()); //$NON-NLS-1$
            count++;
        }
        assertEquals(1, count);

        // -----------------------

        m = pattern.matcher(" count(Employee.age)"); //$NON-NLS-1$

        count = 0;
        while (m.find()) {
            System.out.println("group: " + m.group() + " - start: " + m.start() + " - end: " + m.end()); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
            System.out.println("expec: Employee.age"); //$NON-NLS-1$
            assertEquals("Employee.age", m.group().trim()); //$NON-NLS-1$
            count++;
        }
        assertEquals(1, count);

        // ------------------------

        m = pattern.matcher(" sum(old.age, young.age) as sum"); //$NON-NLS-1$
        List<String> expected = new ArrayList<String>();
        expected.add("old.age"); //$NON-NLS-1$
        expected.add("young.age"); //$NON-NLS-1$

        count = 0;
        while (m.find()) {
            System.out.println("group: " + m.group() + " - start: " + m.start() + " - end: " + m.end()); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
            System.out.println("expec: " + expected.get(count)); //$NON-NLS-1$
            assertEquals(expected.get(count), m.group().trim());
            count++;
        }
        assertEquals(expected.size(), count);

        // -----------------------

        m = pattern.matcher(" SchemaNAme.table.id"); //$NON-NLS-1$
        expected = new ArrayList<String>();
        expected.add("SchemaNAme.table.id"); //$NON-NLS-1$

        count = 0;
        while (m.find()) {
            System.out.println("group: " + m.group() + " - start: " + m.start() + " - end: " + m.end()); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
            System.out.println("expec: " + expected.get(count)); //$NON-NLS-1$
            assertEquals(expected.get(count), m.group().trim());
            count++;
        }
        assertEquals(expected.size(), count);

        // ------------------------

        m = pattern.matcher(" sum(schema.old.age, young.age, schema.table.name) as sum2 "); //$NON-NLS-1$
        expected = new ArrayList<String>();
        expected.add("schema.old.age"); //$NON-NLS-1$
        expected.add("young.age"); //$NON-NLS-1$
        expected.add("schema.table.name"); //$NON-NLS-1$

        count = 0;
        while (m.find()) {
            System.out.println("group: " + m.group() + " - start: " + m.start() + " - end: " + m.end()); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
            System.out.println("expec: " + expected.get(count)); //$NON-NLS-1$
            assertEquals(expected.get(count), m.group().trim());
            count++;
        }
        assertEquals(expected.size(), count);

    }
}
