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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.function.Consumer;

import com.jporm.commons.core.util.GenericWrapper;

/**
 *
 * @author Francesco Cina
 *
 *         01/lug/2011
 *
 *         Parse a Stream identifying valid sql statement. For every statement
 *         found a call to the IParserCallback is performed.
 */
public class StreamParser implements Parser {

    private static final String SEPARATOR_SYMBOL = ";";
    private final InputStream inputStream;
    private final Charset charset;
    private final boolean closeInputStream;

    public StreamParser(final InputStream inputStream, final boolean closeInputStream) {
        this(inputStream, closeInputStream, Charset.defaultCharset());
    }

    public StreamParser(final InputStream inputStream, final boolean closeInputStream, final Charset charset) {
        this.inputStream = inputStream;
        this.closeInputStream = closeInputStream;
        this.charset = charset;
    }

    private void checkend(String separatorSymbol, final Consumer<String> parserCallback, final StringBuilder StringBuilder, final String line,
            final GenericWrapper<Integer> apostrophesWrapper) {
        String trimmedline = line.trim();
        if (!trimmedline.isEmpty() && !trimmedline.startsWith("--")) { //$NON-NLS-1$
            if (line.contains(SEPARATOR_SYMBOL)) {
                String tempLine = line;
                String[] splitted = tempLine.split(separatorSymbol);
                int position = 0;
                for (String token : splitted) {
                    int apostrophes = apostrophesWrapper.getValue();
                    position += token.length() + 1;
                    apostrophes += countApostrophes(token);
                    apostrophesWrapper.setValue(apostrophes);
                    if ((apostrophes % 2) == 1) {
                        StringBuilder.append(token + separatorSymbol);
                    } else {
                        StringBuilder.append(token);
                        parserCallback.accept(StringBuilder.toString());
                        StringBuilder.setLength(0);
                        apostrophesWrapper.setValue(0);
                        tempLine = tempLine.substring(position, tempLine.length());
                        checkend(separatorSymbol, parserCallback, StringBuilder, tempLine, apostrophesWrapper);
                        break;
                    }
                }
            } else {
                StringBuilder.append(line + "\n"); //$NON-NLS-1$
            }
        }
    }

    private int countApostrophes(final String line) {
        int count = 0;
        int index = 0;
        while ((index = line.indexOf("'", index)) != -1) { //$NON-NLS-1$
            ++index;
            ++count;
        }
        return count;
    }

    private boolean findStatement(String separatorSymbol, final Consumer<String> parserCallback, final BufferedReader bufferedReader) throws IOException {
        StringBuilder StringBuilder = new StringBuilder();
        String line = null;
        GenericWrapper<Integer> apostrophes = new GenericWrapper<Integer>(0);
        while (true) {
            if (!((line = bufferedReader.readLine()) != null)) {
                return false;
            }
            checkend(separatorSymbol, parserCallback, StringBuilder, line, apostrophes);
        }
    }

    @Override
    public void parse(final Consumer<String> parserCallback) throws IOException {
        parse(SEPARATOR_SYMBOL, parserCallback);
    }

    @Override
    public void parse(final String separatorSymbol, final Consumer<String> parserCallback) throws IOException {
        InputStreamReader inputStreamReader = null;
        BufferedReader bufferedReader = null;
        try {
            inputStreamReader = new InputStreamReader(inputStream, charset);
            bufferedReader = new BufferedReader(inputStreamReader);
            findStatement(separatorSymbol, parserCallback, bufferedReader);
        } finally {
            if (bufferedReader != null) {
                bufferedReader.close();
            }
            if (inputStreamReader != null) {
                inputStreamReader.close();
            }
            if (closeInputStream && (inputStream != null)) {
                inputStream.close();
            }
        }
    }

}
