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
package com.jporm.sql.query.update.set;

import java.util.ArrayList;
import java.util.List;

public class CaseWhenImpl implements CaseWhen {

    private final String caseField;
    private final List<CasePair> casePairs = new ArrayList<>();

    public CaseWhenImpl(String caseField) {
        this.caseField = caseField;
    }

    @Override
    public CaseWhen when(Object when, Object then) {
        casePairs.add(new CasePair(when, then));
        return this;
    }

    public String getCaseField() {
        return caseField;
    }

    public List<CasePair> getCasePairs() {
        return casePairs;
    }

}
