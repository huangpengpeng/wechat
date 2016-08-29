package com.wechat.dao;

import java.util.List;

import com.wechat.entity.WechatUser;

public interface WechatUserDao {

	public long add(WechatUser wechatUser);
	
	public WechatUser getByOpenId(Long partnerId,String openId);
	
	public WechatUser get(Long id);
	
	public WechatUser update(Long id, Long externalNo);
	
	public WechatUser removeBinding(Long id) ;
	
	public List<WechatUser> getByExternalNo(Long externalNo);
	
	public void delete(Long id) ;
}
