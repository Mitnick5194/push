package com.ajie.push;

/**
 * 推送生产者
 * 
 * @author niezhenjie
 *
 */
public interface PushProducer {

	/**
	 * 发送一条消息
	 * 
	 * @param msg
	 *            消息内容
	 */
	void send(Message msg);

	/**
	 * 延迟发送消息
	 * 
	 * @param msg
	 *            消息内容
	 * @param delay
	 *            延迟值，单位ms
	 */
	void sendDelay(Message msg, long delay);

	/**
	 * 发送一条消息
	 * 
	 * @param msg
	 *            消息内容
	 * @param retry
	 *            失败重试次数
	 * @param interval
	 *            重试间隔 ，单位ms
	 */
	void send(Message msg, int retry, long interval);

	/**
	 * 发送一条带超时值消息
	 * 
	 * @param msg
	 *            消息内容
	 * @param timeout
	 *            超时值 ，单位ms
	 */
	void send(Message msg, long timeout);
}
