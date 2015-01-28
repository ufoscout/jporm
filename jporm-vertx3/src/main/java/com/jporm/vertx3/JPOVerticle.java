/*******************************************************************************
 * Copyright 2014 Francesco Cina'
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
package com.jporm.vertx3;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.eventbus.MessageCodec;

import java.util.UUID;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jporm.core.session.datasource.JPOrmDataSource;

public class JPOVerticle extends AbstractVerticle {

	public static void deploy(final Vertx vertx, final DataSource datasource, final int maxConnections, final Handler<JPO> handler) {

		String instanceId = UUID.randomUUID().toString();
		MessageCodec<?, ?> messageCodec = new NullMessageCodec<>(instanceId);
		vertx.eventBus().registerCodec(messageCodec);
		JPOVertxNameBuilder nameBuider = new JPOVertxNameBuilder(instanceId, messageCodec);
		com.jporm.core.JPO jpo = new JPOrmDataSource(datasource);
		vertx.deployVerticle(new JPOVerticle(nameBuider), result -> {
			deployWorkers(vertx, maxConnections, jpo, nameBuider, handler);
		});
	}

	private static void deployWorkers(final Vertx vertx, final int remainingInstances, final com.jporm.core.JPO jpo, final JPOVertxNameBuilder nameBuider, final Handler<JPO> handler) {
		final DeploymentOptions options = new DeploymentOptions();
		options.setWorker(true);
		vertx.deployVerticle(new JPOWorkerVerticle(jpo, nameBuider, remainingInstances), options, workerResult -> {
			if (workerResult.failed()) {
				throw new RuntimeException(workerResult.cause());
			}
			int updatedRemainingInstances = remainingInstances - 1;
			if (updatedRemainingInstances > 0) {
				deployWorkers(vertx, updatedRemainingInstances, jpo, nameBuider, handler);
			} else {
				handler.handle(new JPOVertxExecutor(nameBuider, vertx));
			}
		});
	}

	private final Logger logger = LoggerFactory.getLogger(getClass());
	private final JPOVertxNameBuilder nameBuider;

	private JPOVerticle(final JPOVertxNameBuilder nameBuider) {
		this.nameBuider = nameBuider;
	}

	@Override
	public void start(final Future<Void> startFuture) {
		logger.info("Starting JPO verticle with JPO instanceId [{}]", nameBuider.getInstanceId());
		startFuture.complete();
	}

}
