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

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;

import javax.sql.DataSource;

import org.junit.Test;

import com.jporm.rx.vertx.BaseTestApi;
import com.jporm.sql.query.insert.Insert;
import com.jporm.test.domain.section08.CommonUser;

import io.vertx.core.Vertx;
import io.vertx.core.json.JsonArray;
import io.vertx.ext.jdbc.JDBCClient;
import io.vertx.ext.sql.SQLConnection;
import io.vertx.ext.sql.UpdateResult;

/**
 * <class_description>
 * <p>
 * <b>notes</b>:
 * <p>
 * ON : Feb 13, 2013
 *
 * @author Francesco Cina'
 * @version $Revision
 */
public class VertxJDBCServiceTest extends BaseTestApi {

    @Test
    public void testQuery() throws Exception {
        Vertx vertx = Vertx.vertx();

        DataSource dataSource = getH2DataSource();

        JDBCClient jdbcService = JDBCClient.create(vertx, dataSource);

        CountDownLatch latch = new CountDownLatch(1);
        jdbcService.getConnection(handler -> {

            handler.result().query("select count(*) as count from users", result -> {
                try {
                    getLogger().info("Found {} columns", result.result().getNumColumns());
                    getLogger().info("Columns: {}", result.result().getColumnNames());
                    getLogger().info("Found {} rows", result.result().getNumRows());
                    getLogger().info("Results size {}", result.result().getResults().size());
                    getLogger().info("Rows size {}", result.result().getRows().size());

                    for (JsonArray res : result.result().getResults()) {
                        getLogger().info("Results at position: {}", res.getInteger(0));
                    }

                    int count = result.result().getRows().size();
                    getLogger().info("Found {} users", count);
                } finally {
                    latch.countDown();
                }
            });

        });

        latch.await();

    }

    @Test
    public void testUpdate() throws Exception {
        Vertx vertx = Vertx.vertx();

        DataSource dataSource = getH2DataSource();

        JDBCClient jdbcService = JDBCClient.create(vertx, dataSource);

        final String firstname = UUID.randomUUID().toString();
        final String lastname = UUID.randomUUID().toString();
        final Insert insertUser = getSqlFactory().insertInto(CommonUser.class, new String[] { "firstname", "lastname" });
        insertUser.values(new Object[] { firstname, lastname });

        CountDownLatch latch = new CountDownLatch(1);
        jdbcService.getConnection(handler -> {
            final SQLConnection connection = handler.result();

            List<Object> values = new ArrayList<>();
            insertUser.sqlValues(values);
            JsonArray params = new JsonArray(values);

            getLogger().info("Execute query: {}", insertUser.sqlQuery());

            connection.updateWithParams(insertUser.sqlQuery(), params, handler2 -> {
                getLogger().info("Insert succeeded: {}", handler2.succeeded());
                UpdateResult updateResult = handler2.result();
                getLogger().info("Updated {} rows", updateResult.getUpdated());
                getLogger().info("Keys {}", updateResult.getKeys());

                Long userId = updateResult.getKeys().getLong(0);
                getLogger().info("User id {}", userId);

                JsonArray params2 = new JsonArray();
                // params2.add(userId);
                connection.queryWithParams("Select * from Users", params2, handler3 -> {
                    getLogger().info("Select succeeded: {}", handler3.succeeded());
                    // getLogger().info("error message: {}",
                    // handler3.cause().getMessage());
                    getLogger().info("Found {} columns", handler3.result().getNumColumns());
                    getLogger().info("Columns: {}", handler3.result().getColumnNames());
                    getLogger().info("Found {} rows", handler3.result().getNumRows());
                    getLogger().info("Results size {}", handler3.result().getResults().size());
                    getLogger().info("Rows size {}", handler3.result().getRows().size());
                    latch.countDown();
                });

                // for (JsonArray res : result.result().getResults()) {
                // getLogger().info("Results at position: {}",
                // res.getInteger(0));
                // }

            });

        });

        latch.await();

    }

}
