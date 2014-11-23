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
package com.jporm.core.query.namesolver;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.jporm.core.inject.ClassTool;
import com.jporm.core.inject.ServiceCatalog;
import com.jporm.exception.OrmException;
import com.jporm.exception.OrmQueryFormatException;
import com.jporm.introspector.mapper.clazz.ClassDescriptor;
import com.jporm.query.Property;
import com.jporm.query.namesolver.NameSolver;

/**
 *
 * @author Francesco Cina
 *
 * 22/giu/2011
 */
public class NameSolverImpl implements NameSolver {

	//    public static String FIND_ALL_PROPERTY_PATTERN = "[a-zA-Z_0-9]+[\\.][a-zA-Z_0-9]+[\\.][a-zA-Z_0-9]+|[a-zA-Z_0-9]+[\\.][a-zA-Z_0-9]+"; //$NON-NLS-1$

	private static String EXPRESSIONS = ",|=|<|>| like | in | not | or | and ";

	public static String FIND_ALL_PROPERTY_PATTERN = "(?<=[(]+|^[ ]*|" + EXPRESSIONS + ")[\\s]*[a-zA-Z]+[\\.a-zA-Z_0-9]*(?!'|\"|[(\\.a-zA-Z_0-9]+)";

	private static Pattern patternProperties = Pattern.compile(FIND_ALL_PROPERTY_PATTERN, Pattern.CASE_INSENSITIVE);

	private static final int MAX_ALIAS_LENGHT = 25;
	private static final String SEPARATOR = "_"; //$NON-NLS-1$

	private final Map<String, ClassDescriptor<?>> registeredClass = new HashMap<String, ClassDescriptor<?>>();
	private final Map<Integer, String> classAlias = new HashMap<Integer, String>();
	private final Map<String, String> normalizedAliases = new HashMap<String, String>();
	private final ServiceCatalog serviceCatalog;
	private String defaultAlias = null;
	private int registeredClassCount = 0;
	private final boolean alwaysResolveWithoutAlias;

	/**
	 *
	 * @param serviceCatalog
	 * @param alwaysResolveWithoutAlias If set to true always resolves the properties
	 * name without prepend the table name alias, even if the solvePropertyName is called
	 */
	public NameSolverImpl(final ServiceCatalog serviceCatalog, final boolean alwaysResolveWithoutAlias) {
		this.serviceCatalog = serviceCatalog;
		this.alwaysResolveWithoutAlias = alwaysResolveWithoutAlias;
	}

	@Override
	public String solvePropertyName(final String propertyName) throws OrmException {
		final Property property = serviceCatalog.getPropertiesFactory().property(propertyName);
		final String alias = property.getAlias(defaultAlias);
		final String field = property.getField();
		if (!registeredClass.containsKey(alias)) {
			throw new OrmException("Alias [" + alias + "] is not associated with an Orm Entity. Registered alias are: " + registeredClass.keySet()); //$NON-NLS-1$ //$NON-NLS-2$
		}
		final String dbColumn = getDbColumn(alias, field);
		if (alwaysResolveWithoutAlias) {
			return dbColumn;
		}
		return normalizedAliases.get(alias) + "." + dbColumn; //$NON-NLS-1$
	}

	@Override
	public <P> Integer register(final Class<P> clazz, final String alias) throws OrmException {
		if ((alias==null) || alias.isEmpty()) {
			throw new OrmQueryFormatException("Cannot use an empty or null alias"); //$NON-NLS-1$
		}
		return register(clazz, alias, serviceCatalog.getClassToolMap().get(clazz));
	}

	private <P> Integer register(final Class<P> clazz, final String alias, final ClassTool<P> ormClassTool) throws OrmException {
		Integer classId = registeredClassCount++;
		registeredClass.put(alias, ormClassTool.getDescriptor());
		classAlias.put(classId, alias);
		normalizedAliases.put(alias, normalizeAlias(alias, classId));
		if (defaultAlias==null) {
			defaultAlias = alias;
		}
		return classId;
	}

	private String normalizeAlias(final String alias, final Integer classId) {
		String normalized = alias;
		if (normalized.length()>MAX_ALIAS_LENGHT) {
			normalized=normalized.substring(0, MAX_ALIAS_LENGHT);
		}
		return normalized + SEPARATOR + classId;
	}

	@Override
	public String normalizedAlias(final Integer classId) throws OrmException {
		String alias = classAlias.get(classId);
		if (alias==null) {
			throw new OrmException("No class are registered in this query with the id " + classId  ); //$NON-NLS-1$
		}
		return normalizedAliases.get(alias);
	}

	private String getDbColumn(final String alias, final String field) {
		String dbColumn = registeredClass.get(alias).getFieldDescriptorByJavaName(field).getColumnInfo().getDBColumnName();
		if (dbColumn.isEmpty()) {
			throw new OrmQueryFormatException("Field with name [" + field + "] is not present or ignored for alias [" + alias + "]"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		}
		return dbColumn;
	}

	@Override
	public void solveAllPropertyNames(final String input, final StringBuilder outputBuilder) {
		final Matcher m = patternProperties.matcher(input);
		int beginIndex = 0;

		while (m.find()) {
			outputBuilder.append( input.substring(beginIndex, m.start()) );
			outputBuilder.append( solvePropertyName( m.group().trim() ) );
			beginIndex = m.end();
		}

		outputBuilder.append( input.substring(beginIndex, input.length()) );
	}

}
