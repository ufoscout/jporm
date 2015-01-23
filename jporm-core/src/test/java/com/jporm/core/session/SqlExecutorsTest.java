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
package com.jporm.core.session;

import static org.junit.Assert.assertEquals;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Test;

import com.jporm.JPO;
import com.jporm.core.BaseTestApi;
import com.jporm.core.session.datasource.JPOrmDataSource;
import com.jporm.session.GeneratedKeyReader;
import com.jporm.session.ResultSetReader;
import com.jporm.session.Session;
import com.jporm.session.SqlExecutor;

/**
 *
 * @author Francesco Cina
 *
 * 02/lug/2011
 */
public class SqlExecutorsTest extends BaseTestApi {

	@Test
	public void testExecuteAll() {
		final JPO jpOrm = new JPOrmDataSource(getH2DataSource());

		final Session session = jpOrm.session();
		SqlExecutor sqlExecutor = session.sqlExecutor();

		List<Long> ids = session.txNow((_session) -> {
			return sqlExecutorInsert( sqlExecutor );
		});

		checkExistAll(ids , sqlExecutor , true);

		session.txVoidNow((_session) -> {
			sqlExecutorDelete( ids, sqlExecutor );
		});

		checkExistAll(ids , sqlExecutor , false);

	}


	private void sqlExecutorDelete(final List<Long> ids, final SqlExecutor sqlExecutor) {
		final String sql = "delete from people where id = ?"; //$NON-NLS-1$

		final List<Object[]> args = new ArrayList<>();

		for (final Long id : ids) {
			args.add(new Object[]{id});
		}

		sqlExecutor.batchUpdate(sql, args);

	}

	private List<Long> sqlExecutorInsert(final SqlExecutor sqlExec) {
		final List<Long> results = new ArrayList<Long>();

		long idMain = new Date().getTime();

		final long id1 = idMain++;
		results.add(id1);
		final String sqlFixed = "insert into people (id, firstname, lastname) values ( " + id1 + " , 'fixed name' , 'fixed surname' )"; //$NON-NLS-1$ //$NON-NLS-2$
		assertEquals( 1 , sqlExec.update(sqlFixed));

		final String sql1 = "insert into people (id, firstname, lastname) values ( ? , ? , ? )"; //$NON-NLS-1$
		final long id2 = idMain++;
		results.add(id2);
		assertEquals( 1 , sqlExec.update(sql1, new Object[]{ id2, "name-" + id2 , "surname-" + id2 })); //$NON-NLS-1$ //$NON-NLS-2$

		final List<Object[]> args = new ArrayList<Object[]>();
		final long id3 = idMain++;
		final long id4 = idMain++;
		final long id5 = idMain++;
		results.add(id3);
		results.add(id4);
		results.add(id5);
		args.add( new Object[]{ id3, "name-" + id3 , "batchUpdate(sql1, args) " + id3 } ); //$NON-NLS-1$ //$NON-NLS-2$
		args.add( new Object[]{ id4, "name-" + id4 , "batchUpdate(sql1, args) " + id4 } ); //$NON-NLS-1$ //$NON-NLS-2$
		args.add( new Object[]{ id5, "name-" + id5 , "batchUpdate(sql1, args) " + id5 } ); //$NON-NLS-1$ //$NON-NLS-2$
		assertEquals( 3 , sqlExec.batchUpdate(sql1, args).length );

		final List<String> sqlsFixed = new ArrayList<String>();
		final long id6 = idMain++;
		final long id7 = idMain++;
		results.add(id6);
		results.add(id7);
		sqlsFixed.add("insert into people (id, firstname, lastname) values ( " + id6 + " , 'batchUpdate(sqlsFixed)' , '1' )"); //$NON-NLS-1$ //$NON-NLS-2$
		sqlsFixed.add("insert into people (id, firstname, lastname) values ( " + id7 + " , 'batchUpdate(sqlsFixed)' , '2' )"); //$NON-NLS-1$ //$NON-NLS-2$
		assertEquals( 2 , sqlExec.batchUpdate(sqlsFixed).length );

		final String sqlKeyExtractor = "insert into people (id, firstname, lastname) values ( SEQ_PEOPLE.nextval , ? , ? )"; //$NON-NLS-1$
		final GeneratedKeyReader generatedKeyExtractor = new GeneratedKeyReader() {
			/**
			 *
			 */


			@Override
			public void read(final ResultSet generatedKeyResultSet) throws SQLException {
				generatedKeyResultSet.next();
				final long gk = generatedKeyResultSet.getLong(1);
				System.out.println("Generated key: " + gk); //$NON-NLS-1$
				results.add(gk);
			}

			@Override
			public String[] generatedColumnNames() {
				return new String[]{"ID"}; //$NON-NLS-1$
			}
		};
		assertEquals( 1 , sqlExec.update(sqlKeyExtractor, generatedKeyExtractor, new Object[]{ "sqlExec.update(sqlKeyExtractor, generatedKeyExtractor, args", "1"}) ); //$NON-NLS-1$ //$NON-NLS-2$

		return results;
	}

	private void checkExistAll(final List<Long> peopleIds, final SqlExecutor sqlExecutor, final boolean exist) {
		String sql = "select * from people where id in ( "; //$NON-NLS-1$
		for (int i=0; i<(peopleIds.size()-1); i++) {
			sql += "?, "; //$NON-NLS-1$
		}
		sql += "? ) "; //$NON-NLS-1$

		final ResultSetReader<List<Long>> rse = new ResultSetReader<List<Long>>() {
			@Override
			public List<Long> read(final ResultSet resultSet) throws SQLException {
				final List<Long> result = new ArrayList<Long>();
				while (resultSet.next()) {
					result.add( resultSet.getLong("ID") ); //$NON-NLS-1$
				}
				return result;
			}
		};

		final List<Long> result = sqlExecutor.query(sql, rse, peopleIds.toArray());
		for (final Long id : peopleIds) {
			System.out.println("Check id: " + id + " exists? " + result.contains(id) ); //$NON-NLS-1$ //$NON-NLS-2$
			assertEquals( exist,  result.contains(id) );
		}
	}
}
