/*******************************************************************************
 * Copyright 2017 Francesco Cina'
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
package spike.json;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

import javax.sql.DataSource;

import org.junit.Test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jporm.core.domain.Employee;
import com.jporm.sql.dialect.DBType;
import com.jporm.test.BaseTestAllDB;
import com.jporm.test.TestData;

public class MySqlJsonTest  extends BaseTestAllDB {

	private final ObjectMapper mapper = new ObjectMapper();

	public MySqlJsonTest(final String testName, final TestData testData) {
		super(testName, testData);
	}

	@Test
	public void testJsonCRUD() throws Exception {
		if (! isDBType(DBType.MYSQL)) {
			return;
		}

		final DataSource datasource = getTestData().getDataSource();

		final AtomicLong id = new AtomicLong(-1);

		final Employee employee = new Employee();
		employee.setId(100);
		employee.setEmployeeNumber(UUID.randomUUID().toString());

		try (Connection connection = datasource.getConnection()) {

			{
				final String insertSql = "INSERT INTO MYSQL_JSON (JSON_COL) VALUES (?)";
				final PreparedStatement insertPs = connection.prepareStatement(insertSql,  Statement.RETURN_GENERATED_KEYS);
				insertPs.setString(1, mapper.writeValueAsString(employee));

				insertPs.execute();
				final ResultSet gkrs = insertPs.getGeneratedKeys();

				assertTrue(gkrs.next());
				final long generatedId = gkrs.getLong(1);
				getLogger().info("Saved JSONB object with Id [{}]", generatedId);
				id.set(generatedId);
			}

			{
				final String selectSql = "SELECT ID, JSON_COL FROM MYSQL_JSON WHERE ID = ?";
				final PreparedStatement selectPs = connection.prepareStatement(selectSql);
				selectPs.setLong(1, id.get());

				final ResultSet selectRs = selectPs.executeQuery();
				assertTrue(selectRs.next());
				assertEquals(id.get(), selectRs.getInt(1));
				assertEquals(employee.getEmployeeNumber(), mapper.readValue(selectRs.getString(2), Employee.class).getEmployeeNumber());
			}

			{
				final String selectJsonSql = "SELECT ID, JSON_COL FROM MYSQL_JSON WHERE JSON_COL -> '$.employeeNumber' = ?";
				final PreparedStatement selectJsonPs = connection.prepareStatement(selectJsonSql);
				selectJsonPs.setString( 1, employee.getEmployeeNumber() );

				final ResultSet selectJsonRs = selectJsonPs.executeQuery();
				assertTrue(selectJsonRs.next());
				assertEquals(id.get(), selectJsonRs.getInt(1));
				assertEquals(employee.getEmployeeNumber(), mapper.readValue(selectJsonRs.getString(2), Employee.class).getEmployeeNumber());
			}

			{
				final String selectJsonSql = "SELECT ID, JSON_COL FROM MYSQL_JSON WHERE JSON_COL -> '$.id' = ?";
				final PreparedStatement selectJsonPs = connection.prepareStatement(selectJsonSql);
				selectJsonPs.setLong( 1, employee.getId() );

				final ResultSet selectJsonRs = selectJsonPs.executeQuery();
				assertTrue(selectJsonRs.next());
				assertEquals(id.get(), selectJsonRs.getInt(1));
				assertEquals(employee.getEmployeeNumber(), mapper.readValue(selectJsonRs.getString(2), Employee.class).getEmployeeNumber());
			}
		}



	}

}
