package com.ajie.push.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.jms.JMSException;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ajie.chilli.common.KVpair;
import com.ajie.chilli.utils.common.JsonUtils;
import com.ajie.push.Message;
import com.ajie.push.vo.MessageVo;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import edu.emory.mathcs.backport.java.util.Collections;

/**
 * 推送消息的简单实现
 * 
 * @author niezhenjie
 *
 */
public class SimpleMessage implements Message {
	private static final Logger logger = LoggerFactory
			.getLogger(SimpleMessage.class);
	private MessageVo vo;

	public SimpleMessage() {

	}

	public SimpleMessage(MessageVo messageVo) {
		vo = new MessageVo();
		vo.setDestination(messageVo.getDestination());
		vo.setContent(messageVo.getContent());
		vo.setCreateTime(new Date(messageVo.getCreateTime()));
		vo.setReference(messageVo.getReference());
		vo.setSendTime(new Date(messageVo.getSendTime()));
		vo.setTimeout(messageVo.getTimeout());
		vo.setBiz(messageVo.getBiz());
	}

	@Override
	public String getDestination() {
		return vo.getDestination();
	}

	@Override
	public String getReference() {
		return vo.getReference();
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> T getContent(Class<T> clazz) {
		if (isState(STATE_EXPIRED.getId())) { // 超过有效期了
			return (T) null;
		}
		Object content = vo.getContent();
		if (null == content) {
			return (T) null;
		}

		// 发送数据转json时，fastjson会将对象转换成jsonObject
		if (content instanceof JSONObject) {
			JSONObject obj = (JSONObject) content;
			return JsonUtils.toBean(obj, clazz);
		}
		return (T) content;
	}

	/**
	 * 取结果集，如果结果返回的是列表（jsonarray），则调用此方法获取结果
	 * 
	 * @return
	 */
	@Override
	@SuppressWarnings("unchecked")
	public <T> List<T> getContents(Class<T> clazz) {
		if (isState(STATE_EXPIRED.getId())) { // 超过有效期了
			return Collections.emptyList();
		}
		Object content = vo.getContent();
		if (null == content) {
			return Collections.emptyList();
		}

		if (content instanceof JSONArray) {
			JSONArray arr = (JSONArray) content;
			List<T> list = new ArrayList<T>(arr.size());
			for (int i = 0; i < arr.size(); i++) {
				JSONObject json = arr.getJSONObject(i);
				T t = (T) JsonUtils.toBean(json, clazz);
				list.add(t);
			}
			return list;
		}
		if (content instanceof String) {
			String str = (String) content;
			// 先转成jsonarray
			try {
				JSONArray arr = JsonUtils.toBean(str, JSONArray.class);
				List<T> list = new ArrayList<T>(arr.size());
				for (int i = 0; i < arr.size(); i++) {
					JSONObject json = arr.getJSONObject(i);
					T t = (T) JsonUtils.toBean(json, clazz);
					list.add(t);
				}
				return list;
			} catch (Exception e) {
				logger.error("解析失败", e);
				return Collections.emptyList();
			}
		}
		return Collections.emptyList();
	}

	@Override
	public KVpair getState() {
		long timeout = vo.getTimeout();
		if (timeout == -1) {
			return STATE_NORMAL;
		}
		long sendTime = vo.getTimeout();
		if (sendTime + timeout > System.currentTimeMillis()) {
			return STATE_EXPIRED;
		}
		return STATE_EXPIRED;
	}

	@Override
	public Date getCreateTime() {
		return new Date(vo.getCreateTime());
	}

	@Override
	public Date getSendTime() {
		return new Date(vo.getSendTime());
	}

	@Override
	public boolean isState(int state) {
		KVpair s = getState();
		return s.getId() == state;
	}

	@Override
	public KVpair getType() {
		return TYPE_ALL.getItemById(vo.getType());
	}

	@Override
	public javax.jms.Message createMessage(Session session) throws JMSException {
		TextMessage textMessage = session.createTextMessage(vo.toString());
		return textMessage;
	}

	@Override
	public String getBiz() {
		return vo.getBiz();
	}

}
