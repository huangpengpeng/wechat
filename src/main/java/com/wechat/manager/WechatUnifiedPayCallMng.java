package com.wechat.manager;

import java.util.List;

import com.wechat.entity.WechatUnifiedPayCall;

public interface WechatUnifiedPayCallMng {

	WechatUnifiedPayCall add(Long userId, String appId, String mchId,
			String requestNo, String externalNo, 
			String reqeustText, String api);
	
	WechatUnifiedPayCall addNotify(Long userId, 
			String externalNo,String api,  String responseText,String responseCode, String responseMsg);

	WechatUnifiedPayCall getByRequestNo(String requestNo);
	
	List<WechatUnifiedPayCall>  getByExternalNo(String externalNo);

	public void update(Long id,String responseText, 
			String responseCode, String responseMsg);
}
