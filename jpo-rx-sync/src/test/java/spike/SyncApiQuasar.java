package spike;

/**
 * Created by ufo on 25/07/15.
 */
public class SyncApiQuasar implements SyncApi {

    private AsyncApi asyncApi;

    public SyncApiQuasar(AsyncApi asyncApi) {
        this.asyncApi = asyncApi;
    }

    @Override
    public int sum(int a, int b) {
        try {
            return co.paralleluniverse.fibers.futures.AsyncCompletionStage.get(asyncApi.sum(a,b));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
