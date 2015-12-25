package com.wechat.manager;

import com.wechat.entity.WechatUser;

public interface WechatUserMng {

	WechatUser add(Long partnerId, Long externalNo, String openId);
	
	public WechatUser getByOpenId(Long partnerId,String openId);
	
	void update(Long id,Long externalNo);
	
	void removeBinding(Long id);
}
