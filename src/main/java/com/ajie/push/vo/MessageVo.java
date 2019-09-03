package com.ajie.push.vo;

import java.util.Date;

import com.ajie.chilli.utils.common.JsonUtils;
import com.ajie.push.Message;

/**
 * 消息vo
 * 
 * @author ajie
 *
 */
public class MessageVo {
	/** 业务类型 */
	private String biz;
	/** 发送类型 ，默认订阅发布 */
	private int type;
	/** 所属主题 */
	private String destination;
	/** 引用 */
	private String reference;
	/** 内容 */
	private Object content;
	/** 创建时间 */
	private long createTime;
	/** 发送时间 */
	private long sendTime;
	/** 超时时间 -1表示不超时 ，发送时间 + timeout < 当前时间表示超时 */
	private long timeout;

	public MessageVo() {
		createTime = System.currentTimeMillis();
		timeout = -1;
		type = Message.TYPE_SUBSCRIBE_PUBLISH.getId();
	}

	public MessageVo(String destination) {
		this(Message.TYPE_SUBSCRIBE_PUBLISH.getId(), destination);
	}

	public MessageVo(int type, String destination) {
		this(type, destination, (Object) null);
	}

	public MessageVo(String destination, Object content) {
		this(Message.TYPE_SUBSCRIBE_PUBLISH.getId(), destination, content);
	}

	public MessageVo(int type, String destination, Object content) {
		this();
		this.type = type;
		this.destination = destination;
		this.content = content;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getDestination() {
		return destination;
	}

	public void setDestination(String destination) {
		this.destination = destination;
	}

	public String getReference() {
		return reference;
	}

	public void setReference(String reference) {
		this.reference = reference;
	}

	public Object getContent() {
		return content;
	}

	public void setContent(Object content) {
		this.content = content;
	}

	public long getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime.getTime();
	}

	public long getSendTime() {
		return sendTime;
	}

	public void setSendTime(Date sendTime) {
		this.sendTime = sendTime.getTime();
	}

	public void setTimeout(long timeout) {
		this.timeout = timeout;
	}

	public long getTimeout() {
		return timeout;
	}

	public String getBiz() {
		return biz;
	}

	public void setBiz(String biz) {
		this.biz = biz;
	}

	public String toString() {
		return JsonUtils.toJSONString(this);
	}

}
