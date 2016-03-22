package com.wechat.dao;

import com.wechat.entity.WechatWallet;

public interface WechatWalletDao {

	long add(WechatWallet wechatWallet);
	
	WechatWallet get(Long id);
	
	WechatWallet getByexternalNo(String externalNo);
	
	void update(Long id ,String username, String wechatPayNo);
	
	/**
	 * 
			* 描述:设置认证
			* @author liyixing 2016年3月22日 下午1:39:27
	 */
	void setIdentificationFlag(Long id ,Boolean identificationFlag);
}
