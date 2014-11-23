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
package com.jporm.introspector.mapper.clazz;

import com.jporm.introspector.annotation.column.ColumnInfo;
import com.jporm.introspector.annotation.generator.GeneratorInfo;
import com.jporm.introspector.annotation.version.VersionInfo;


/**
 *
 * @author cinafr
 *
 * @param <P>
 */
public class FieldDescriptorImpl<BEAN, P> implements FieldDescriptor<BEAN, P> {

	private VersionInfo versionInfo;
	private GeneratorInfo generatorInfo;
	private ColumnInfo columnInfo;
	private final String fieldName;
	private final Class<P> type;
	private boolean identifier = false;

	public FieldDescriptorImpl(final Class<P> type,  final String fieldName) {
		this.type = type;
		this.fieldName = fieldName;
	}

	@Override
	public VersionInfo getVersionInfo() {
		return this.versionInfo;
	}

	public void setVersionInfo(final VersionInfo versionInfo) {
		this.versionInfo = versionInfo;
	}

	@Override
	public GeneratorInfo getGeneratorInfo() {
		return this.generatorInfo;
	}

	public void setGeneratorInfo(final GeneratorInfo generatorInfo) {
		this.generatorInfo = generatorInfo;
	}

	@Override
	public final boolean isIdentifier()
	{
		return this.identifier ;
	}

	public final void setIdentifier(final boolean identifier) {
		this.identifier = identifier;
	}

	@Override
	public final Class<P> getType() {
		return this.type;
	}

	@Override
	public final ColumnInfo getColumnInfo() {
		return this.columnInfo;
	}

	public final void setColumnInfo(final ColumnInfo columnInfo) {
		this.columnInfo = columnInfo;
	}

	@Override
	public final String getFieldName() {
		return this.fieldName;
	}

}
