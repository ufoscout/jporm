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
package com.jporm.domain;

import com.jporm.annotation.Id;
import com.jporm.annotation.Version;

/**
 * 
 * @author Francesco Cina'
 *
 * Apr 21, 2012
 */
public class AnnotationBean1Extended extends AnnotationBean1 {

	@Id
	private String index2;

	@Version
	private long myVersion;

	/**
	 * @return the index2
	 */
	public String getIndex2() {
		return index2;
	}

	/**
	 * @param index2 the index2 to set
	 */
	public void setIndex2(String index2) {
		this.index2 = index2;
	}

	/**
	 * @return the myVersion
	 */
	public long getMyVersion() {
		return myVersion;
	}

	/**
	 * @param myVersion the myVersion to set
	 */
	public void setMyVersion(long myVersion) {
		this.myVersion = myVersion;
	}


}
