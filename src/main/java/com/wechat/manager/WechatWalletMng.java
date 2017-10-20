package com.wechat.manager;

import com.wechat.entity.WechatWallet;

public interface WechatWalletMng {

	public WechatWallet add(String username, String wechatPayNo,String externalNo);
	
	public WechatWallet get(Long id);
	
	public WechatWallet update(Long id ,String username, String wechatPayNo,Boolean identificationFlag);
	
	public WechatWallet getByexternalNo(String externalNo);
}
