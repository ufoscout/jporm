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
package spike;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.Collection;
import java.util.List;

import org.junit.Test;

import com.jporm.BaseTestApi;
import com.jporm.domain.section08.UserJob;

/**
 * <class_description>
 * <p><b>notes</b>:
 * <p>ON : Feb 13, 2013
 *
 * @author Francesco Cina'
 * @version $Revision
 */
public class ReflectionTest extends BaseTestApi {

    @Test
    public void test() throws SecurityException {
        Field[] fields = ReflectionBeanWithList.class.getDeclaredFields();
        for (Field field : fields) {
            getLogger().info("Field found [{}]", field.getName()); //$NON-NLS-1$
        }

        assertEquals(1, fields.length);
        Field listField = fields[0];
        assertEquals("list", listField.getName()); //$NON-NLS-1$
        assertEquals( List.class ,  listField.getType() );
        assertTrue( listField.getType().isAssignableFrom(List.class) );
        assertTrue( Collection.class.isAssignableFrom(listField.getType()) );
        assertEquals( UserJob.class , ((ParameterizedType) listField.getGenericType()).getActualTypeArguments()[0] );

        //        ParameterizedType stringListType = (ParameterizedType) listField.getGenericType();
        //        Class<?> stringListClass = (Class<?>) stringListType.getActualTypeArguments()[0];
        //        System.out.println(stringListClass);
    }

}
