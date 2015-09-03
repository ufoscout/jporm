package spike;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Created by ufo on 25/07/15.
 */
public class AsyncApiImpl implements AsyncApi {

    Executor exec = Executors.newFixedThreadPool(10);

    @Override
    public CompletableFuture<Integer> sum(int a, int b) {
        CompletableFuture<Integer> result = new CompletableFuture<>();
        exec.execute(() -> {
            int sum = a+b;
            System.out.println("received " + a + " " + b + " -> returning: " + sum);
            result.complete(sum);
        });
        return result;
    }

}
