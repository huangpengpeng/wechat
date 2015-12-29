package com.wechat.dao;

import com.wechat.entity.WechatWallet;

public interface WechatWalletDao {

	long add(WechatWallet wechatWallet);
	
	WechatWallet get(Long id);
	
	WechatWallet getByexternalNo(String externalNo);
	
	void update(Long id ,String username, String wechatPayNo);
}
