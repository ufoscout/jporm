package test.spring;

import com.jporm.rm.JpoRm;
import org.junit.Test;
import org.springframework.transaction.PlatformTransactionManager;
import test.TestBase;
import test.all.bean.User;

import javax.annotation.Resource;
import javax.sql.DataSource;

import static org.junit.Assert.*;

/**
 * Created by ufo on 25/07/15.
 */
public class SpringActionsTest extends TestBase {

    @Resource
    private DataSource dataSource;
    @Resource
    private PlatformTransactionManager platformTransactionManager;
    private SpringActions actions = new SpringActions();

    @Test
    public void should_create_a_jpo_instance() {

        JpoRm jpo = actions.create_JPO_instance(dataSource, platformTransactionManager);
        assertNotNull(jpo);

        jpo.transaction().executeVoid(session -> {

            User user = new User();
            user.firstName = "name";
            user.lastName = "surname";

            // It prints null as the User instance does not have an id yet.
            System.out.println(user.id);

            // A new User object is created. The User.id field contains the auto generated value.
            // The original User instance is not modified.
            User savedUser = session.save(user);

            // It prints the id of the user instance. Eg. 1
            System.out.println(savedUser.id);

            // It still prints null because the original User instance has not been modified.
            System.out.println(user.id);

        });

    }

}