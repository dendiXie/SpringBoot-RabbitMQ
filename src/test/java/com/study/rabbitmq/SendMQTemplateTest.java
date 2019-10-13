package com.study.rabbitmq;

import com.study.rabbitmq.mq.RabbitConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.concurrent.CountDownLatch;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SendMQTemplateTest {

	@Autowired
	private AmqpTemplate rabbitTemplate;

	@Test
	public void sendMessage() {
		CountDownLatch countDownLatch = new CountDownLatch(1);
		// 模拟300个并发请求
		for (int i = 0; i < 300; i++) {
			new Thread(new Send(countDownLatch)).start();
		}

		// 计数器減一 所有线程释放 并发访问。
		countDownLatch.countDown();
	}

	private class Send implements Runnable {
		private final CountDownLatch countDownLatch;

		public Send(CountDownLatch countDownLatch) {
			this.countDownLatch = countDownLatch;
		}

		@Override
		public void run() {
			try {
				countDownLatch.await(); // 一直阻塞当前线程，直到计时器的值为0
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			String message = "1"; // 模拟获取用户id为1
			rabbitTemplate.convertAndSend(RabbitConfig.DIRECT_ROUTING_KEY_recvqueue, message);
		}

	}

	/**
	 docker run -d --name rabbitmq -p 5671:5671 -p 5672:5672 -p 4369:4369 \
	 -p 25672:25672 -p 15671:15671 -p 15672:15672 -p 15674:15674 -p 15670:15670 \
	 -p 15673:15673 --restart=always xiaochunping/rabbitmq:management
	 */

}
