package com.wechat.manager;

import com.wechat.entity.WechatWallet;

public interface WechatWalletMng {

	WechatWallet add(String username, String wechatPayNo,String externalNo);
	
	WechatWallet get(Long id);
	
	WechatWallet update(Long id ,String username, String wechatPayNo);
	
	/**
	 * 
			* 描述:设置认证
			* @author liyixing 2016年3月22日 下午1:39:27
	 */
	void setIdentificationFlag(Long id ,Boolean identificationFlag);
	
	WechatWallet getByexternalNo(String externalNo);
}
