package test.spring;

import com.jporm.rm.JpoRm;
import com.jporm.rm.spring.session.jdbctemplate.JPOrmJdbcTemplateBuilder;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

/**
 * Created by ufo on 25/07/15.
 */
public class SpringActions {

    public JpoRm create_JPO_instance(DataSource dataSource, PlatformTransactionManager platformTransactionManager) {
        return new JPOrmJdbcTemplateBuilder().build(dataSource, platformTransactionManager);
    }

}
