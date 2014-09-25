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
import static org.junit.Assert.fail;

import org.junit.Test;

import com.jporm.core.BaseTestApi;

/**
 * <class_description>
 * <p><b>notes</b>:
 * <p>ON : Feb 13, 2013
 *
 * @author Francesco Cina'
 * @version $Revision
 */
public class EnumTest extends BaseTestApi {

    @Test
    public void testEnumCasting() {
        
        Status status = Status.ERROR;
        assertEquals("ERROR", status.name());
        assertEquals( Status.ERROR, Status.valueOf("ERROR") );
        
        Class<Status> statusClass = Status.class;
        
        assertTrue( statusClass.isEnum() );
        
        assertEquals( Status.ERROR, Enum.valueOf(statusClass, "ERROR") );
        
        assertEquals( Animal.DOG, Enum.valueOf(Animal.class, "DOG") );

    }

    @Test(expected=RuntimeException.class)
    public void testWrongEnumCasting() {
        Enum.valueOf(Animal.class, "MOUSE");
        fail("should throw an exception before");
    }
    
    public enum Status {
        ERROR,
        WARNING,
        NORMAL
    }
    
    public enum Animal {
        DOG,
        CAT,
        LION
    }
    
}
