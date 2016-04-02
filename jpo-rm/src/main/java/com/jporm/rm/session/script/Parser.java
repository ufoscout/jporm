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
package com.jporm.rm.session.script;

import java.io.IOException;
import java.util.function.Consumer;

/**
 *
 * @author Francesco Cina
 *
 *         01/lug/2011
 */
public interface Parser {

    /**
     * Parse the script using the default symbol ";" to identify the sql
     * statements
     *
     * @param parserCallback
     * @throws IOException
     */
    void parse(Consumer<String> parserCallback) throws IOException;

    /**
     * Parse the script using a custom separator symbol to split the sql statements
     *
     * @param parserCallback
     * @param separatorSymbol
     *            a custom separator symbol
     * @throws IOException
     */
    void parse(String separatorSymbol, Consumer<String> parserCallback) throws IOException;

}
