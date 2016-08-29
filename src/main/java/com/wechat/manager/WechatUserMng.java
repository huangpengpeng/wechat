package com.wechat.manager;

import java.util.List;
import java.util.Map;

import com.wechat.entity.WechatUser;

public interface WechatUserMng {
	
	public static final String MSG_WECHAT_USER_REGISTER_SUBMIT="WechatUserMng.register";

	WechatUser add(Long partnerId, Long externalNo, String openId);
	
	Map<String,Object> register(Long partnerId, Long externalNo, String openId,
			Long pId);
	
	public WechatUser getByOpenId(Long partnerId,String openId);
	
	public WechatUser delete(Long id);
	
	WechatUser update(Long id,Long externalNo);
	
	List<WechatUser> getByExternalNo(Long externalNo);
	
	WechatUser removeBinding(Long id);
}
