package spike;

import java.util.Random;
import java.util.concurrent.ExecutionException;

import org.junit.Test;

import com.jporm.rm.quasar.RmQuasarTestBase;

import co.paralleluniverse.fibers.Fiber;

/**
 * Created by ufo on 25/07/15.
 */
public class QuasarTest extends RmQuasarTestBase {

	@Test
	public void testQuasar() throws ExecutionException, InterruptedException {

		AsyncApi async = new AsyncApiImpl();
		SyncApi sync = new SyncApiImpl(async);

		new Fiber<Void>(() ->{
			int a = new Random().nextInt(1000);
			System.out.println("a is " + a);
			int b = new Random().nextInt(1000);
			System.out.println("b is " + b);
			System.out.println(a + " + " + b + " = " + sync.sum(a,b));

		}).start().join();

	}




}
