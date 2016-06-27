/*******************************************************************************
 * Copyright 2016 Francesco Cina'
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
package com.jporm.sql.query.select.from;

public enum JoinType {

    FULL_OUTER_JOIN("FULL OUTER JOIN "),
    INNER_JOIN("INNER JOIN "),
    LEFT_OUTER_JOIN("LEFT OUTER JOIN "),
    NATURAL_JOIN("NATURAL JOIN "),
    RIGHT_OUTER_JOIN("RIGHT OUTER JOIN "),
    SIMPLE_JOIN(", ");

    private final String joinType;

    JoinType(final String joinType) {
        this.joinType = joinType;
    }

    public String getJoinClause() {
        return joinType;
    }
}
