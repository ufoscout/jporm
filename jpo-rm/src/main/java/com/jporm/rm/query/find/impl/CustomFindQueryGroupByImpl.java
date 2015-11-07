/*******************************************************************************
 * Copyright 2013 Francesco Cina' Licensed under the Apache License, Version 2.0 (the "License"); you may not use this
 * file except in compliance with the License. You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0 Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific language governing permissions and limitations under the
 * License.
 ******************************************************************************/
package com.jporm.rm.query.find.impl;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import com.jporm.commons.core.exception.JpoException;
import com.jporm.commons.core.exception.JpoNotUniqueResultException;
import com.jporm.commons.core.query.find.impl.CommonFindQueryGroupByImpl;
import com.jporm.rm.query.find.CustomFindQuery;
import com.jporm.rm.query.find.CustomFindQueryGroupBy;
import com.jporm.rm.query.find.CustomFindQueryOrderBy;
import com.jporm.rm.query.find.CustomFindQueryWhere;
import com.jporm.sql.query.clause.SelectCommon;
import com.jporm.types.io.ResultSetReader;
import com.jporm.types.io.ResultSetRowReader;

/**
 * <class_description>
 * <p>
 * <b>notes</b>:
 * <p>
 * ON : Mar 23, 2013
 *
 * @author Francesco Cina'
 * @version $Revision
 */
public class CustomFindQueryGroupByImpl extends
        CommonFindQueryGroupByImpl<CustomFindQuery, CustomFindQueryWhere, CustomFindQueryOrderBy, CustomFindQueryGroupBy> implements CustomFindQueryGroupBy {

    public CustomFindQueryGroupByImpl(final com.jporm.sql.query.clause.GroupBy sqlGroupBy, final CustomFindQuery customFindQuery) {
        super(sqlGroupBy, customFindQuery);
    }

    @Override
    public <T> T fetch(final ResultSetReader<T> rsr) throws JpoException {
        return root().fetch(rsr);
    }

    @Override
    public <T> List<T> fetch(final ResultSetRowReader<T> rsrr) throws JpoException {
        return root().fetch(rsrr);
    }

    @Override
    public BigDecimal fetchBigDecimal() {
        return root().fetchBigDecimal();
    }

    @Override
    public Optional<BigDecimal> fetchBigDecimalOptional() throws JpoException {
        return root().fetchBigDecimalOptional();
    }

    @Override
    public BigDecimal fetchBigDecimalUnique() throws JpoException {
        return root().fetchBigDecimalUnique();
    }

    @Override
    public Boolean fetchBoolean() {
        return root().fetchBoolean();
    }

    @Override
    public Optional<Boolean> fetchBooleanOptional() throws JpoException {
        return root().fetchBooleanOptional();
    }

    @Override
    public Boolean fetchBooleanUnique() throws JpoException {
        return root().fetchBooleanUnique();
    }

    @Override
    public Double fetchDouble() {
        return root().fetchDouble();
    }

    @Override
    public Optional<Double> fetchDoubleOptional() {
        return root().fetchDoubleOptional();
    }

    @Override
    public Double fetchDoubleUnique() throws JpoException {
        return root().fetchDoubleUnique();
    }

    @Override
    public Float fetchFloat() {
        return root().fetchFloat();
    }

    @Override
    public Optional<Float> fetchFloatOptional() {
        return root().fetchFloatOptional();
    }

    @Override
    public Float fetchFloatUnique() throws JpoException {
        return root().fetchFloatUnique();
    }

    @Override
    public Integer fetchInt() {
        return root().fetchInt();
    }

    @Override
    public Optional<Integer> fetchIntOptional() {
        return root().fetchIntOptional();
    }

    @Override
    public Integer fetchIntUnique() throws JpoException {
        return root().fetchIntUnique();
    }

    @Override
    public Long fetchLong() {
        return root().fetchLong();
    }

    @Override
    public Optional<Long> fetchLongOptional() {
        return root().fetchLongOptional();
    }

    @Override
    public Long fetchLongUnique() throws JpoException {
        return root().fetchLongUnique();
    }

    @Override
    public String fetchString() {
        return root().fetchString();
    }

    @Override
    public Optional<String> fetchStringOptional() {
        return root().fetchStringOptional();
    }

    @Override
    public String fetchStringUnique() throws JpoException {
        return root().fetchStringUnique();
    }

    @Override
    public <T> T fetchUnique(final ResultSetRowReader<T> rsrr) throws JpoException, JpoNotUniqueResultException {
        return root().fetchUnique(rsrr);
    }

    @Override
    public SelectCommon sql() {
        return root().sql();
    }

    @Override
    protected CustomFindQueryGroupBy sqlQuery() {
        return this;
    }

}
