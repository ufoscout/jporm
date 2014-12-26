package spike;

import io.vertx.core.buffer.Buffer;
import io.vertx.core.eventbus.MessageCodec;
import io.vertx.core.eventbus.MessageConsumer;
import io.vertx.test.core.VertxTestBase;

import java.util.concurrent.TimeUnit;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LocalConsumerTest extends VertxTestBase {

	private final Logger logger = LoggerFactory.getLogger(getClass());

	@Test
	public void testLocalConsumer() throws InterruptedException {

		vertx.eventBus().registerDefaultCodec(MyBean.class, new MyBeanMessageCodec());

		MessageConsumer<MyBean> consumer = vertx.eventBus().localConsumer("localConsumerTest");
		consumer.handler(message -> {
			logger.info("Receveid MyBean with value " + message.body().value);
			testComplete();
		})
		.completionHandler(event -> {
			MyBean bean = new MyBean();
			bean.value = "testValue";
			vertx.eventBus().send("localConsumerTest", bean);

		});

		await(5, TimeUnit.SECONDS);
	}

	class MyBean {
		String value;
	}

	class MyBeanMessageCodec implements MessageCodec<MyBean, MyBean> {

		@Override
		public void encodeToWire(final Buffer buffer, final MyBean s) {
		}

		@Override
		public MyBean decodeFromWire(final int pos, final Buffer buffer) {
			return null;
		}

		@Override
		public MyBean transform(final MyBean s) {
			return s;
		}

		@Override
		public String name() {
			return getClass().getName();
		}

		@Override
		public byte systemCodecID() {
			return -1;
		}

	}
}
