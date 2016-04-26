/*******************************************************************************
 * Copyright 2015 Francesco Cina'
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
package test.all.rm;

import javax.annotation.Resource;
import javax.sql.DataSource;

import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.PlatformTransactionManager;

import com.jporm.rm.JpoRm;
import com.jporm.rm.session.Session;
import com.jporm.rm.spring.JpoRmJdbcTemplateBuilder;

import test.TestBase;
import test.all.bean.User;

public class JpoRmCRUDTest extends TestBase {

    @Resource
    private DataSource dataSource;
    @Resource
    private PlatformTransactionManager platformTransactionManager;

    @SuppressWarnings("unused")
    @Test
    public void testCRUD() {
        JpoRm jpo = JpoRmJdbcTemplateBuilder.get().build(new JdbcTemplate(dataSource), platformTransactionManager);
        Session session = jpo.session();

        Long id = null;

        User user = new User();
        user.firstName = "name";
        user.lastName = "surname";

        // Create User
        // A new User object is created. The User.id field contains the auto
        // generated value.
        // The original User instance is not modified.
        User savedUser = session.save(user);

        // Find user
        User userFound = session.findById(User.class, savedUser.id).fetchOne();

        userFound.firstName = "new FirstName";
        // Update the User instance
        User userUpdated = session.update(userFound);

        // Delete the user and return the number of rows deleted.
        int deletedUserCount = session.delete(userFound);
    }

}
