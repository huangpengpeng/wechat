package com.wechat.manager;

import java.math.BigDecimal;

import com.wechat.entity.ClearingwWechatPayment;

public interface ClearingwWechatPaymentMng {

	ClearingwWechatPayment add(Long userId, BigDecimal clearingFee, String status,
			String externalNo, String type, String ip);
	
	ClearingwWechatPayment get(Long id);
	
	ClearingwWechatPayment getByExternalNo(String type, String externalNo);
}
