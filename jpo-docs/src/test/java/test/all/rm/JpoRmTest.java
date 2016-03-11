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

import java.util.List;
import java.util.Optional;

import javax.annotation.Resource;
import javax.sql.DataSource;

import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.PlatformTransactionManager;

import com.jporm.rm.JpoRm;
import com.jporm.rm.session.Session;
import com.jporm.rm.spring.JpoRmJdbcTemplateBuilder;
import com.jporm.sql.query.where.expression.Exp;

import test.TestBase;
import test.all.bean.User;

public class JpoRmTest extends TestBase {

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

        //
        // BEGIN -- Create User
        //
        {
            User user = new User();
            user.firstName = "name";
            user.lastName = "surname";

            // It prints null as the User instance does not have an id yet.
            System.out.println(user.id);

            // A new User object is created. The User.id field contains the auto
            // generated value.
            // The original User instance is not modified.
            User savedUser = session.save(user);

            // It prints the id of the user instance. Eg. 1
            System.out.println(savedUser.id);
            id = savedUser.id;

            // It still prints null because the original User instance has not
            // been modified.
            System.out.println(user.id);
        }
        //
        // END -- Create User
        //

        //
        // BEGIN -- Find user
        //
        {
            // fetch a user by id. If not present, null is returned.
            User user1 = session.findById(User.class, id).fetch();

            // fetch a user by id. If not present, JpoNotUniqueResultException
            // is throw.
            User user2 = session.find(User.class).where(Exp.eq("id", id)).fetchUnique();

            // fetch a user by id and name. An optional is returned.
            String name = "Tom";
            Optional<User> user3 = session.find(User.class).where(Exp.eq("id", id).eq("firstName", name)).fetchOptional();

            // fetch all the users that have firstName = lastName and sort by id
            List<User> users1 = session.find(User.class).where().eqProperties("firstName", "lastName").orderBy().asc("id").fetchList();

            // fetch the count of users that have firstName = lastName
            int users1Count = session.find(User.class).where().eqProperties("firstName", "lastName").fetchRowCount();

            // fetch the firstName of the user with id 123
            String firstName = session.find("u.firstName").from(User.class, "u").where().eq("id", 123).fetchString();

        }
        //
        // END -- Find User
        //

        //
        // BEGIN -- Update user
        //
        {
            // fetch a user by id. If not present, null is returned.
            User user1 = session.findById(User.class, id).fetch();

            // Update the User instance
            user1.firstName = "new FirstName";
            User updatedUser = session.saveOrUpdate(user1);

            // Update all the Users with name "Tom" changing their name to
            // "Jack"
            int modifiedUsers = session.update(User.class).set("firstName", "Jack").where().eq("firstName", "Tom").execute();

        }
        //
        // END -- Update User
        //

        //
        // BEGIN -- Delete user
        //
        {
            // fetch a user by id. If not present, null is returned.
            User user1 = session.findById(User.class, id).fetch();

            // Delete the user and return the number of rows deleted.
            int deletedUser1 = session.delete(user1);

            // Delete all Users
            int deletedUser2 = session.delete(User.class).execute();

            // Delete all Users with less than 18 years
            int deletedUser3 = session.delete(User.class).where().le("age", 18).execute();
        }
        //
        // END -- Delete User
        //
    }

}
