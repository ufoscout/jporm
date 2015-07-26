package spike;

import co.paralleluniverse.fibers.Suspendable;

/**
 * Created by ufo on 25/07/15.
 */
@Suspendable
public interface SyncApi {

    int sum(int a, int b);

}
