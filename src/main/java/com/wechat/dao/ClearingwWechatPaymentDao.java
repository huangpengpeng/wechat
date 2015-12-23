package com.wechat.dao;

import com.wechat.entity.ClearingwWechatPayment;

public interface ClearingwWechatPaymentDao {

	long add(ClearingwWechatPayment clearingwWechatPayment);
	
	ClearingwWechatPayment get(Long id);
	
	ClearingwWechatPayment getByExternalNo(String type ,String externalNo);
}
