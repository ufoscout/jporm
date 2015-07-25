package spike;

import com.jporm.commons.core.async.AsyncTaskExecutor;
import com.jporm.commons.core.async.impl.ThreadPoolAsyncTaskExecutor;

import java.util.concurrent.CompletableFuture;

/**
 * Created by ufo on 25/07/15.
 */
public class AsyncApiImpl implements AsyncApi {

    private AsyncTaskExecutor exec = new ThreadPoolAsyncTaskExecutor(10, "async-pool");

    @Override
    public CompletableFuture<Integer> sum(int a, int b) {
        return exec.execute(() -> {return a + b;});
    }

}
