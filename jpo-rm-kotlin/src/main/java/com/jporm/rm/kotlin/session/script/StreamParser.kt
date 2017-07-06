/*******************************************************************************
 * Copyright 2013 Francesco Cina'

 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at

 * http://www.apache.org/licenses/LICENSE-2.0

 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.jporm.rm.kotlin.session.script

import com.jporm.commons.core.util.GenericWrapper

import java.io.BufferedReader
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader
import java.nio.charset.Charset
import java.util.function.Consumer

/**

 * @author Francesco Cina
 * *
 * *         01/lug/2011
 * *
 * *         Parse a Stream identifying valid sql statement. For every statement
 * *         found a call to the IParserCallback is performed.
 */
class StreamParser @JvmOverloads constructor(private val inputStream: InputStream?, private val closeInputStream: Boolean, private val charset: Charset = Charset.defaultCharset()) : Parser {

    private fun checkend(separatorSymbol: String, parserCallback: Consumer<String>, StringBuilder: StringBuilder, line: String,
                         apostrophesWrapper: GenericWrapper<Int>) {
        val trimmedline = line.trim { it <= ' ' }
        if (!trimmedline.isEmpty() && !trimmedline.startsWith("--")) { //$NON-NLS-1$
            if (line.contains(SEPARATOR_SYMBOL)) {
                var tempLine = line
                val splitted = tempLine.split(separatorSymbol.toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                var position = 0
                for (token in splitted) {
                    var apostrophes = apostrophesWrapper.value
                    position += token.length + 1
                    apostrophes += countApostrophes(token)
                    apostrophesWrapper.value = apostrophes
                    if (apostrophes % 2 == 1) {
                        StringBuilder.append(token + separatorSymbol)
                    } else {
                        StringBuilder.append(token)
                        parserCallback.accept(StringBuilder.toString())
                        StringBuilder.setLength(0)
                        apostrophesWrapper.value = 0
                        tempLine = tempLine.substring(position, tempLine.length)
                        checkend(separatorSymbol, parserCallback, StringBuilder, tempLine, apostrophesWrapper)
                        break
                    }
                }
            } else {
                StringBuilder.append(line + "\n") //$NON-NLS-1$
            }
        }
    }

    private fun countApostrophes(line: String): Int {
        var count = 0
        var index = 0
        while ((index = line.indexOf("'", index)) != -1) { //$NON-NLS-1$
            ++index
            ++count
        }
        return count
    }

    @Throws(IOException::class)
    private fun findStatement(separatorSymbol: String, parserCallback: Consumer<String>, bufferedReader: BufferedReader): Boolean {
        val StringBuilder = StringBuilder()
        var line: String? = null
        val apostrophes = GenericWrapper(0)
        while (true) {
            if ((line = bufferedReader.readLine()) == null) {
                return false
            }
            checkend(separatorSymbol, parserCallback, StringBuilder, line, apostrophes)
        }
    }

    @Throws(IOException::class)
    override fun parse(parserCallback: Consumer<String>) {
        parse(SEPARATOR_SYMBOL, parserCallback)
    }

    @Throws(IOException::class)
    override fun parse(separatorSymbol: String, parserCallback: Consumer<String>) {
        var inputStreamReader: InputStreamReader? = null
        var bufferedReader: BufferedReader? = null
        try {
            inputStreamReader = InputStreamReader(inputStream!!, charset)
            bufferedReader = BufferedReader(inputStreamReader)
            findStatement(separatorSymbol, parserCallback, bufferedReader)
        } finally {
            if (bufferedReader != null) {
                bufferedReader.close()
            }
            if (inputStreamReader != null) {
                inputStreamReader.close()
            }
            if (closeInputStream && inputStream != null) {
                inputStream.close()
            }
        }
    }

    companion object {

        private val SEPARATOR_SYMBOL = ";"
    }

}
