package com.ajie.push;

import javax.jms.MessageListener;

/**
 * 消息监听者
 * 
 * @author niezhenjie
 *
 */
public interface PushListener extends MessageListener {

	/**
	 * 接收所有主题的消息
	 * 
	 * @param message
	 */
	void comsume(Message message);

}
