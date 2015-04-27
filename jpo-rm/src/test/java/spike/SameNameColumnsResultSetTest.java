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
package spike;

import static org.junit.Assert.assertEquals;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.junit.Test;

import com.jporm.rm.BaseTestApi;

/**
 * 
 * @author Francesco Cina
 *
 * 27/giu/2011
 */
public class SameNameColumnsResultSetTest extends BaseTestApi {

    @Test
    public void testSequences() throws Exception {
        final Connection connection = getH2DataSource().getConnection();

        final String sql = "select e.ID as \"e.ID\", p.ID as \"p.ID\" from EMPLOYEE e, PEOPLe p"; //$NON-NLS-1$
        final PreparedStatement ps = connection.prepareStatement(sql);
        final ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            System.out.println("id1?: " + rs.getLong(1)); //$NON-NLS-1$
            System.out.println("id1?: " + rs.getLong("e.ID")); //$NON-NLS-1$ //$NON-NLS-2$
            System.out.println("id2?: " + rs.getLong(2)); //$NON-NLS-1$
            System.out.println("id2?: " + rs.getLong("p.ID")); //$NON-NLS-1$ //$NON-NLS-2$
            assertEquals( rs.getLong(1) , rs.getLong("e.ID") ); //$NON-NLS-1$
            assertEquals( rs.getLong(2) , rs.getLong("p.ID") ); //$NON-NLS-1$
        }
        rs.close();
        ps.close();

        connection.close();
    }

}
