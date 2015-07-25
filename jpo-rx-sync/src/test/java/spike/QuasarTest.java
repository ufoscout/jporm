package spike;

import co.paralleluniverse.fibers.Fiber;
import com.jporm.rx.sync.SyncTestBase;
import org.junit.Test;

import java.util.Random;
import java.util.concurrent.ExecutionException;

/**
 * Created by ufo on 25/07/15.
 */
public class QuasarTest extends SyncTestBase {

    @Test
    public void testQuasar() throws ExecutionException, InterruptedException {

        AsyncApi async = new AsyncApiImpl();
        SyncApi sync = new SyncApiQuasar(async);

        new Fiber<Void>(() ->{
            int a = new Random().nextInt(1000);
            int b = new Random().nextInt(1000);
            System.out.println(a + " + " + b + " = " + sync.sum(a,b));

        }).start().join();


    }

}
