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
 *          ON : Feb 19, 2013
 * ----------------------------------------------------------------------------
 */
package com.jporm.domain.section08;

import java.util.ArrayList;
import java.util.List;

/**
 * <class_description>
 * <p><b>notes</b>:
 * <p>ON : Feb 19, 2013
 *
 * @author Francesco Cina'
 * @version $Revision
 */
public class AggregatedUserJob extends UserJob {

    private List<UserJobTask> jobTasks = new ArrayList<UserJobTask>();

    public List<UserJobTask> getJobTasks() {
        return jobTasks;
    }

    public void setJobTasks(final List<UserJobTask> jobTasks) {
        this.jobTasks = jobTasks;
    }

}
