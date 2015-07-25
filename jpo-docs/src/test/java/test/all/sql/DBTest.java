package test.all.sql;

import org.junit.Assert;
import org.junit.Test;
import test.TestBase;

import javax.annotation.Resource;
import javax.sql.DataSource;
import javax.xml.crypto.Data;

/**
 * Created by ufo on 25/07/15.
 */
public class DBTest extends TestBase {

    @Resource
    private DataSource dataSource;

    @Test
    public void dataSourceShouldBeCreated() {
        Assert.assertNotNull(dataSource);
    }
}
