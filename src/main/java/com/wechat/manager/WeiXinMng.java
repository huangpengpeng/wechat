package com.wechat.manager;

import com.wechat.entity.WeiXin;

public interface WeiXinMng {

	public WeiXin add(Long userId, String openid, String unionid);

	public WeiXin getByUnionid(String unionid);

	public WeiXin getByUser(Long userId);

	public WeiXin updateByUpdater(WeiXin weiXin);
	
	public void delete(Long id);
}
