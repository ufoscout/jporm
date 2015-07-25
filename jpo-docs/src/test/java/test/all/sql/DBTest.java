package test.all.sql;

import org.junit.Assert;
import org.junit.Test;
import test.TestBase;

/**
 * Created by ufo on 25/07/15.
 */
public class DBTest extends TestBase {

    @Test
    public void dataSourceShouldBeCreated() {
        Assert.assertNotNull(getDataSource());
    }
}
