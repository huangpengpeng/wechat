package com.wechat.manager;

import com.wechat.entity.WechatWallet;

public interface WechatWalletMng {

	WechatWallet add(String username, String wechatPayNo,String externalNo);
	
	WechatWallet get(Long id);
	
	WechatWallet update(Long id ,String username, String wechatPayNo,Boolean identificationFlag);
	
	WechatWallet getByexternalNo(String externalNo);
}
