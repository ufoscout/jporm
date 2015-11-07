package spike;

import co.paralleluniverse.fibers.Suspendable;

/**
 * Created by ufo on 25/07/15.
 */
public class SyncApiImpl implements SyncApi {

    private AsyncApi asyncApi;

    public SyncApiImpl(final AsyncApi asyncApi) {
        this.asyncApi = asyncApi;
    }

    @Override
    @Suspendable
    public int sum(final int a, final int b) {
        try {
            System.out.println("received a is " + a);
            System.out.println("received b is " + b);
            return co.paralleluniverse.fibers.futures.AsyncCompletionStage.get(asyncApi.sum(a, b));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
