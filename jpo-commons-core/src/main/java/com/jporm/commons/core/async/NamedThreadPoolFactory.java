/*******************************************************************************
 * Copyright 2015 Francesco Cina'
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
package com.jporm.commons.core.async;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

public class NamedThreadPoolFactory implements ThreadFactory {

    private static AtomicInteger GLOBAL_POOL_COUNT = new AtomicInteger(0);

    private static synchronized int getGlobalCount() {
        return GLOBAL_POOL_COUNT.getAndIncrement();
    }

    private AtomicInteger count = new AtomicInteger(0);
    private final String baseThreadName;

    private boolean daemon;

    public NamedThreadPoolFactory(final String baseThreadPoolName) {
        this(baseThreadPoolName, false);
    }

    public NamedThreadPoolFactory(final String baseThreadPoolName, final boolean daemon) {
        this.daemon = daemon;
        baseThreadName = baseThreadPoolName + "-" + getGlobalCount() + "-thread-";
    }

    private synchronized int getCount() {
        return count.getAndIncrement();
    }

    @Override
    public Thread newThread(final Runnable r) {
        Thread thread = new Thread(r, baseThreadName + getCount());
        thread.setDaemon(daemon);
        return thread;
    }

}