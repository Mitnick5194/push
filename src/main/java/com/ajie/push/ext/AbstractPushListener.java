package com.ajie.push.ext;

import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.TextMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ajie.chilli.utils.common.JsonUtils;
import com.ajie.push.Message;
import com.ajie.push.PushListener;
import com.ajie.push.exception.PushException;
import com.ajie.push.impl.SimpleMessage;
import com.ajie.push.vo.MessageVo;

/**
 * 消息监听者的抽象实现
 * <p>
 * 所有的在comsume方法里处理自己的业务消息监听者需要继承该类
 * </p>
 * 
 * @author niezhenjie
 *
 */
public abstract class AbstractPushListener implements PushListener {
	private static final Logger logger = LoggerFactory
			.getLogger(AbstractPushListener.class);

	@Override
	public void onMessage(javax.jms.Message message) {
		if (message instanceof TextMessage == false) {
			throw new PushException("无法处理当前消息类型");
		}
		TextMessage textMessage = (TextMessage) message;
		try {
			String text = textMessage.getText();
			if (null == text) {
				return;
			}
			MessageVo vo = null;
			try {
				vo = JsonUtils.toBean(text, MessageVo.class);
			} catch (Exception e) {
				logger.error("无法将推送结果装换成MessageVo，尝试直接使用String类型", e);
				vo = new MessageVo();
				vo.setContent(text);
				Destination jmsDestination = textMessage.getJMSDestination();
				String topic = jmsDestination.toString();
				if (topic.indexOf("topic") > -1) {
					vo.setType(Message.TYPE_SUBSCRIBE_PUBLISH.getId());
				} else {
					vo.setType(Message.TYPE_PEER_TO_PEER.getId());
				}
				vo.setDestination(topic.substring(topic.lastIndexOf("//")));
			}
			comsume(new SimpleMessage(vo));
		} catch (JMSException e) {
			logger.error("获取消息失败", e);
		} catch (Throwable e) {
			logger.error("", e);
		}
	}

	/**
	 * 子类实现逻辑处理
	 */
	public abstract void comsume(Message message);
}
