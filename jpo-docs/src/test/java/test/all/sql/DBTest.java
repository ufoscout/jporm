package test.all.sql;

import javax.annotation.Resource;
import javax.sql.DataSource;

import org.junit.Assert;
import org.junit.Test;

import test.TestBase;

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
