package com.wechat.entity;

import javax.persistence.Entity;

import com.wechat.entity.base.BaseWechatUnifiedPayCall;

@Entity
public class WechatUnifiedPayCall extends BaseWechatUnifiedPayCall{
	
	public WechatUnifiedPayCall(){}
	
	public WechatUnifiedPayCall(Long userId, String appId, String mchId,
			String requestNo, String externalNo,
			String reqeustText, String responseText, String responseCode,
			String responseMsg, String api) {
		super(userId, appId, mchId, requestNo, externalNo, reqeustText,
				responseText, responseCode, responseMsg, api);
	}

	public void init(){}

	private static final long serialVersionUID = 8790541427538685820L;
}
