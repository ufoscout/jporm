package spike;

import java.util.concurrent.CompletableFuture;

/**
 * Created by ufo on 25/07/15.
 */
public interface AsyncApi {

    CompletableFuture<Integer> sum(int a, int b);

}
