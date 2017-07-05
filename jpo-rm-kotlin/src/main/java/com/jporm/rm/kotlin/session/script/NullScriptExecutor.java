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
package com.jporm.rm.kotlin.session.script;

import com.jporm.commons.core.exception.JpoException;
import com.jporm.rm.kotlin.session.ScriptExecutor;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

/**
 * 
 * @author Francesco Cina
 *
 *         02/lug/2011
 */
public class NullScriptExecutor implements ScriptExecutor {

    @Override
    public void execute(final InputStream scriptStream) throws IOException, JpoException {
        // do nothing
    }

    @Override
    public void execute(final InputStream scriptStream, final Charset charset) throws IOException, JpoException {
        // do nothing
    }

    @Override
    public void execute(final String script) throws JpoException {
        // do nothing
    }

}
