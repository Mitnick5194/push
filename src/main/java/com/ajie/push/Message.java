package com.ajie.push;

import java.util.Date;
import java.util.List;

import org.springframework.jms.core.MessageCreator;

import com.ajie.chilli.common.KVpair;
import com.ajie.chilli.common.KVpairs;
import com.ajie.push.impl.SimpleMessage;
import com.ajie.push.vo.MessageVo;

/**
 * 推送消息，其实就是对activemq推送的text字段进行一下封装
 * 
 * @author niezhenjie
 *
 */
public interface Message extends MessageCreator {
	/** 消息状态 -- 正常 */
	public static final KVpair STATE_NORMAL = KVpair.valueOf(1, "正常");
	/** 消息状态 -- 过期 */
	public static final KVpair STATE_EXPIRED = KVpair.valueOf(2, "已过期");

	/** 发送方式 -- 点对点 */
	public static final KVpair TYPE_PEER_TO_PEER = KVpair.valueOf(0x01, "点对点");
	/** 发送方式 -- 订阅发布 */
	public static final KVpair TYPE_SUBSCRIBE_PUBLISH = KVpair.valueOf(0x02,
			"订阅发布");

	public static final KVpairs TYPE_ALL = KVpairs.valueOf(TYPE_PEER_TO_PEER,
			TYPE_SUBSCRIBE_PUBLISH);

	/**
	 * 获取发送类型
	 */
	KVpair getType();

	/**
	 * 获取所属主题或队列名称
	 * 
	 * @return
	 */
	String getDestination();

	/**
	 * 业务类型，可通过此字段来区分不同的业务
	 * 
	 * @return
	 */
	String getBiz();

	/**
	 * 获取引用
	 * 
	 * @return
	 */
	String getReference();

	/**
	 * 获取指定类型的内容
	 * 
	 * @return
	 */
	<T> T getContent(Class<T> clazz);

	/**
	 * 如果内容是列表形式，则通过此接口来获取内容
	 * 
	 * @return
	 */
	<T> List<T> getContents(Class<T> clazz);

	/**
	 * 获取消息创建时间
	 * 
	 * @return
	 */
	Date getCreateTime();

	/**
	 * 获取发送时间
	 * 
	 * @return
	 */
	Date getSendTime();

	/**
	 * 获取状态
	 * 
	 * @return
	 */
	KVpair getState();

	/**
	 * 是否为指定状态
	 * 
	 * @param state
	 * @return
	 */
	boolean isState(int state);

	public static class MessageBuilder {
		private MessageVo vo;

		private MessageBuilder() {
			vo = new MessageVo();
		}

		public static MessageBuilder getMessageBuilder() {
			return new MessageBuilder();
		}

		public MessageBuilder setType(int type) {
			vo.setType(type);
			return this;
		}

		public MessageBuilder setDestination(String destination) {
			vo.setDestination(destination);
			return this;
		}

		public MessageBuilder setReference(String reference) {
			vo.setReference(reference);
			return this;
		}

		public MessageBuilder setContent(Object content) {
			vo.setContent(content);
			return this;
		}

		public MessageBuilder setCreateTime(Date createTime) {
			vo.setCreateTime(createTime);
			return this;
		}

		public MessageBuilder setSendTime(Date sendTime) {
			vo.setSendTime(sendTime);
			return this;
		}

		public MessageBuilder setTimeout(long timeout) {
			vo.setTimeout(timeout);
			return this;
		}

		public MessageBuilder setBiz(String biz) {
			vo.setBiz(biz);
			return this;
		}

		public Message build() {
			return new SimpleMessage(vo);
		}
	}
}
