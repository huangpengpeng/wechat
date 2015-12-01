package com.wechat.plugins;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import me.chanjar.weixin.mp.api.WxMpMessageHandler;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.WxMpXmlMessage;


public interface WechatConfigSvc {	

	public WxMpService createWxMpService(String appId, String secret, String token);
	
	public void route(HttpServletRequest request, HttpServletResponse response,
			WxMpXmlMessage inputXmlMessage);
	
	/**
	 * 增加消息处理器
	 * 
	 * @param msgType
	 *            消息类型
	 * @param event
	 *            消息时间
	 * @param eventKey
	 *            消息KEY
	 * @param content
	 *            消息内容
	 * @param regex
	 *            消息正则内容
	 * @param handler
	 *            消息处理器
	 */
	public void addMsgHandler(String msgType, String event,
			String eventKey, String content, String regex,WxMpMessageHandler handler);
	
	public void addMsgHandler(String msgType, String [] eventArray,
			String eventKey, String content, String regex,WxMpMessageHandler handler);
}
