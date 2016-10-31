/*******************************************************************************
 * Copyright 2016 Francesco Cina'
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package spike;

import org.junit.Test;

import com.jporm.rx.BaseTestApi;

import io.reactivex.Completable;
import io.reactivex.Maybe;
import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.observers.TestObserver;
import io.reactivex.subscribers.TestSubscriber;

public class RxTest extends BaseTestApi {

    @Test
    public void doOnDisposeShouldBeCalled() {
        Observable.fromCallable(() -> {
            System.out.println("start");
            return "start";
        })
        .doOnError(e -> {
            System.out.println("doOnError");
        })
        .doOnComplete(() -> {
            System.out.println("doOnComplete");
        })
        .doOnDispose(() -> {
            System.out.println("doOnDispose");
        })
        .subscribe(new TestObserver<>());
    }

    @Test
    public void concatWithTest() {
        Observable.fromCallable(() -> {
            System.out.println("start");
            return "start";
        })
//        .doOnError(e -> {
//            System.out.println("doOnError");
//        })
//        .doOnComplete(() -> {
//            System.out.println("doOnComplete");
//        })
        .concatWith(Observable.fromCallable(() -> {
            System.out.println("concatWith");
            return "concatWith";
        }))
        .onErrorResumeNext(e -> {
            return Observable.fromCallable(() -> {
                System.out.println("onErrorResumeNext");
                return "onErrorResumeNext";
            });
        })
        .flatMap(firstname -> {
            System.out.println("flatMap");
            //throw new RuntimeException();
            return Observable.just(firstname);
        })
        .subscribe(new TestObserver<>());
    }

    @Test
    public void flatMapErrorHandling() {
        doSomething()
        .flatMap(firstname -> {
            System.out.println("flatMap");
            throw new RuntimeException();
        })
        .subscribe(new TestObserver<>());
    }

    Observable<String> doSomething() {
        return Observable.fromCallable(() -> {
            System.out.println("start");
            return "start";
        })
        .flatMap(
                v -> Observable.just(v),
                e -> Observable.fromCallable(() -> {
                    System.out.println("Error");
                    return "Error";
                }),
                () -> Observable.fromCallable(() -> {
                    System.out.println("Complete");
                    return "Complete";
                })
             );
    }


    @Test
    public void observableConcatWithTest() {
        Observable.fromCallable(() -> {
            System.out.println("start");
            return "start";
        })
        .doOnError(e -> {
            System.out.println("doOnError");
        })
        .doOnComplete(() -> {
            System.out.println("doOnComplete");
        })
        .doOnDispose(() -> {
            System.out.println("doOnDispose");
        })
        .concatWith(Completable.fromAction(() -> {
            System.out.println("concatWith");
        }).toObservable())
        .doOnComplete(() -> {
            System.out.println("doOnComplete after concatWith");
        })
        .doOnDispose(() -> {
            System.out.println("doOnDispose after concatWith");
        })
        .onErrorResumeNext(e -> {
            return Observable.fromCallable(() -> {
                System.out.println("onErrorResumeNext");
                return "onErrorResumeNext";
            });
        })
        .flatMap(firstname -> {
            System.out.println("flatMap " + firstname);
            throw new RuntimeException();
            //return Observable.just(firstname);
        })
        .subscribe(new TestObserver<>());
    }

    @Test
    public void singleConcatWithTest() {

        boolean fail = false;

        Single.fromCallable(() -> {
            if (fail) {
                throw new RuntimeException();
            }
            System.out.println("start");
            return "start";
        })
        .doOnError(e -> {
            System.out.println("doOnError");
        })
        .doOnSuccess(result -> {
            System.out.println("doOnSuccess");
        })
        .doOnDispose(() -> {
            System.out.println("doOnDispose");
        })
        .onErrorResumeNext(e -> {
            return Single.fromCallable(() -> {
                System.out.println("onErrorResumeNext");
                return "onErrorResumeNext";
            });
        })
        .concatWith(Single.fromCallable(() -> {
            System.out.println("concatWith");
            return "concatWith";
        }))
        .flatMap(firstname -> {
            System.out.println("flatMap");
            throw new RuntimeException();
            //return Observable.just(firstname);
        })
        .subscribe(new TestSubscriber<>());
    }

    @Test
    public void testMaybeCompletion() throws InterruptedException {

        Maybe.just("hello")
        .doOnComplete(() -> {
            System.out.println("complete");
        })
        .doOnSuccess((a) -> {
            System.out.println("success");
        })
        .doOnError((e) -> {
            System.out.println("error");
        })
        .doAfterTerminate(() -> {
            System.out.println("afterTerminate");
        })
        .subscribe(new TestObserver<>());
    }

    @Test
    public void testMaybeError() throws InterruptedException {

        Maybe.fromCallable(() -> {
            throw new RuntimeException();
        })
        .doOnComplete(() -> {
            System.out.println("complete");
        })
        .doOnSuccess((a) -> {
            System.out.println("success");
        })
        .doOnError((e) -> {
            System.out.println("error");
        })
        .doAfterTerminate(() -> {
            System.out.println("afterTerminate");
        })
        .subscribe(new TestObserver<>());
    }

}
