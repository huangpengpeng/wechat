package com.wechat.dao;

import com.wechat.entity.WechatUser;

public interface WechatUserDao {

	long add(WechatUser wechatUser);
	
	public WechatUser getByOpenId(Long partnerId,String openId);
	
	void update(Long id, Long externalNo);
}
