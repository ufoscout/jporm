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
package com.jporm.annotation.introspector.version;

import com.jporm.query.LockMode;

/**
 * 
 * @author cinafr
 *
 * @param <BEAN>
 * @param <P>
 */
public class VersionInfoImpl implements VersionInfo {

	private final LockMode lockMode;
	private final boolean versionable;

	public VersionInfoImpl(final LockMode lockMode, final boolean versionable) {
		this.lockMode = lockMode;
		this.versionable = versionable;

	}

	@Override
	public LockMode getLockMode() {
		return this.lockMode;
	}

	@Override
	public boolean isVersionable() {
		return this.versionable;
	}

}
