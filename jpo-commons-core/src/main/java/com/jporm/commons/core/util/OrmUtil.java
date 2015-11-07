/**
 * *****************************************************************************
 * Copyright 2013 Francesco Cina'
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 *****************************************************************************
 */
package com.jporm.commons.core.util;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;

/**
 *
 * @author Francesco Cina
 *
 *         06/giu/2011
 */
public abstract class OrmUtil {

    public static String UTF8 = "UTF-8"; //$NON-NLS-1$

    public static String readerToString(final Reader reader, final boolean closeReader) {
        try {
            if (reader != null) {
                Writer writer = new StringWriter();
                char[] buffer = new char[1024];
                int n;
                while ((n = reader.read(buffer)) != -1) {
                    writer.write(buffer, 0, n);
                }
                writer.close();
                if (closeReader) {
                    reader.close();
                }
                return writer.toString();
            }
            return ""; //$NON-NLS-1$
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    public static String streamToString(final InputStream is, final String encoding, final boolean closeStream) {
        try (InputStreamReader isr = new InputStreamReader(is, encoding)) {
            String result;
            Reader reader = new BufferedReader(isr);
            result = readerToString(reader, true);
            if (closeStream) {
                is.close();
            }
            return result;
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    public static Reader stringToReader(final String text) {
        return new StringReader(text);
    }

    public static InputStream stringToStream(final String text, final String encoding) {
        try {
            return new ByteArrayInputStream(text.getBytes(encoding));
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

}
