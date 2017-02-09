package com.wechat.dao;

import com.common.jdbc.Updater;
import com.wechat.entity.WeiXin;

public interface WeiXinDao {

	public Long add(WeiXin weiXin);

	public WeiXin getByUnionid(String unionid);

	public WeiXin getByUser(Long userId);
	
	public WeiXin get(Long id);
	
	public void delete(Long id);
	
	public void updateByUpdater(Updater<?> updater);
}
