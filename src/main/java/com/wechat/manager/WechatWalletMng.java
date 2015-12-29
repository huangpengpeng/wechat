package com.wechat.manager;

import com.wechat.entity.WechatWallet;

public interface WechatWalletMng {

	WechatWallet add(String username, String wechatPayNo,String externalNo);
	
	WechatWallet get(Long id);
	
	void update(Long id ,String username, String wechatPayNo);
	
	WechatWallet getByexternalNo(String externalNo);
}
