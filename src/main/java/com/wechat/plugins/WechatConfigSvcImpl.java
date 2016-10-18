package com.wechat.plugins;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.common.web.ResponseUtils;

import me.chanjar.weixin.mp.api.WxMpInMemoryConfigStorage;
import me.chanjar.weixin.mp.api.WxMpMessageHandler;
import me.chanjar.weixin.mp.api.WxMpMessageRouter;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.WxMpXmlOutMessage;

@Service
public class WechatConfigSvcImpl implements WechatConfigSvc{
	
	public WxMpService createWxMpService(String appId, String secret, String token) {
		configStorage.setAppId(appId);
		configStorage.setSecret(secret);
		configStorage.setToken(token);
		wxMpService.setWxMpConfigStorage(configStorage);
		return wxMpService;
	}

	public void route(HttpServletRequest request, HttpServletResponse response,
			WxMpXmlMessage inputXmlMessage) {
		WxMpXmlOutMessage outMessage = wxMpMessageRouter.route(inputXmlMessage);
		if (outMessage != null) {
			ResponseUtils.renderXml(response, outMessage.toXml());
		}
	}

	public void addMsgHandler(String msgType, String event, String eventKey,
			String content, String regex, WxMpMessageHandler handler) {
		wxMpMessageRouter.rule().async(false).msgType(msgType).event(event)
				.eventKey(eventKey).content(content).rContent(regex)
				.handler(handler).end();
	}
	
	public void addMsgHandler(String msgType, String[] eventArray,
			String eventKey, String content, String regex,
			WxMpMessageHandler handler) {
		for (String event : eventArray) {
			addMsgHandler(msgType, event, eventKey, content, regex, handler);
		}
	}
	
	@Autowired
	private WxMpInMemoryConfigStorage configStorage;
	@Autowired
	private WxMpMessageRouter wxMpMessageRouter;
	@Autowired
	public WxMpService wxMpService;
}
