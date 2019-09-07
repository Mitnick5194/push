package com.ajie.push.impl;

import java.util.Date;

import javax.jms.Destination;

import org.apache.activemq.command.ActiveMQQueue;
import org.apache.activemq.command.ActiveMQTopic;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.core.JmsTemplate;

import com.ajie.chilli.common.KVpair;
import com.ajie.chilli.support.TimingTask;
import com.ajie.chilli.support.Worker;
import com.ajie.chilli.thread.ThreadPool;
import com.ajie.push.Message;
import com.ajie.push.PushProducer;
import com.ajie.push.exception.PushException;

/**
 * 简单的推送生产者
 * 
 * @author niezhenjie
 *
 */
public class SimplePushProducer implements PushProducer {
	private static final Logger logger = LoggerFactory
			.getLogger(SimplePushProducer.class);
	/** spring对发送消息进行了封装 */
	private JmsTemplate template;
	/** 线程池 */
	private ThreadPool threadPool;

	public SimplePushProducer(JmsTemplate temp) {
		template = temp;
	}

	@Override
	public void send(Message message) {
		Destination destination = getDestination(message);
		assertNotNull(destination);
		template.send(destination, message);
	}

	public void setThreadPool(ThreadPool pool) {
		threadPool = pool;
	}

	@Override
	public void sendDelay(final Message msg, long delay) {
		final Destination destination = getDestination(msg);
		assertNotNull(destination);
		TimingTask.createTimingTask(threadPool, new Worker() {
			@Override
			public void work() throws Exception {
				template.send(destination, msg);
			}
		}, new Date(System.currentTimeMillis() + delay));
	}

	@Override
	public void send(final Message msg, final int retry, long interval) {
		final Destination destination = getDestination(msg);
		assertNotNull(destination);
		try {
			template.send(destination, msg);
		} catch (Exception e) {
			logger.warn("发送失败，1秒后重试", e);
			TimingTask.createTimingTask(threadPool, new Worker() {
				int count = 0;

				@Override
				public void work() throws Exception {
					if (count++ < retry) {
						template.send(destination, msg);
					} else {
						Thread.interrupted();
					}
				}
			}, interval, interval);
		}
	}

	@Override
	public void send(Message msg, long timeout) {
		throw new UnsupportedOperationException();
	}

	private Destination getDestination(Message message) {
		KVpair type = message.getType();
		String destination = message.getDestination();
		if (null == destination) {
			return null;
		}
		if (type.getId() == Message.TYPE_PEER_TO_PEER.getId()) {
			return new ActiveMQQueue(destination);
		}
		return new ActiveMQTopic(destination);
	}

	private void assertNotNull(Destination destination) {
		if (null == destination) {
			throw new PushException("destination为空");
		}
	}
}
