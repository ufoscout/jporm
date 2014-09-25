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
/* ----------------------------------------------------------------------------
 *     PROJECT : JPOrm
 *
 *  CREATED BY : Francesco Cina'
 *          ON : Feb 13, 2013
 * ----------------------------------------------------------------------------
 */
package com.jporm.test.domain.section08;

import java.util.ArrayList;
import java.util.List;

import com.jporm.annotation.Cascade;
import com.jporm.annotation.cascade.CascadeType;



/**
 * <class_description>
 * <p><b>notes</b>:
 * <p>ON : Feb 13, 2013
 *
 * @author Francesco Cina'
 * @version $Revision
 */
public class CascadeUser extends CommonUser {

	@Cascade(on=CascadeType.DELETE_SAVE)
	private List<CascadeUserJob> jobs = new ArrayList<CascadeUserJob>();

	private CascadeUserAddress address;

	public List<CascadeUserJob> getJobs() {
		return jobs;
	}

	public void setJobs(final List<CascadeUserJob> jobs) {
		this.jobs = jobs;
	}

	public CascadeUserAddress getAddress() {
		return address;
	}

	public void setAddress(final CascadeUserAddress address) {
		this.address = address;
	}

}
