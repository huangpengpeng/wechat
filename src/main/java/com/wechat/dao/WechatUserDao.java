package com.wechat.dao;

import java.util.List;

import com.wechat.entity.WechatUser;

public interface WechatUserDao {

	long add(WechatUser wechatUser);
	
	public WechatUser getByOpenId(Long partnerId,String openId);
	
	void update(Long id, Long externalNo);
	
	public void removeBinding(Long id) ;
	
	List<WechatUser> getByExternalNo(Long externalNo);
	
	public void delete(Long id) ;
}
