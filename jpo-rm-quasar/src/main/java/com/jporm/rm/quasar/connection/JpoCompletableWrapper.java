package com.jporm.rm.quasar.connection;

import java.util.concurrent.ExecutionException;

import co.paralleluniverse.fibers.SuspendExecution;
import co.paralleluniverse.fibers.futures.AsyncCompletionStage;

/**
 * Created by ufo on 26/07/15.
 */
public class JpoCompletableWrapper {

    public static <V> V get(final java.util.concurrent.CompletionStage<V> future) {
        try {
            return AsyncCompletionStage.get(future);
        } catch (ExecutionException e) {
            Throwable cause = e.getCause();
            if ((cause != null) && (cause instanceof RuntimeException)) {
                throw (RuntimeException) cause;
            } else {
                throw new RuntimeException(e);
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (SuspendExecution suspendExecution) {
            throw new RuntimeException(suspendExecution);
        }
    }
}
