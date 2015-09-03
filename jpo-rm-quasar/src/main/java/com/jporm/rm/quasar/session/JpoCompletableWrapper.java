package com.jporm.rm.quasar.session;

import co.paralleluniverse.fibers.SuspendExecution;
import co.paralleluniverse.fibers.futures.AsyncCompletionStage;

import java.util.concurrent.ExecutionException;

/**
 * Created by ufo on 26/07/15.
 */
public class JpoCompletableWrapper {

    public static <V> V get(java.util.concurrent.CompletionStage<V> future) {
        try {
            return AsyncCompletionStage.get(future);
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (SuspendExecution suspendExecution) {
            throw new RuntimeException(suspendExecution);
        }
    }
}
