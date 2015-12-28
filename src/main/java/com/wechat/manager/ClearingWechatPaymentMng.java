package com.wechat.manager;

import java.math.BigDecimal;
import java.util.Date;

import com.common.jdbc.page.Pagination;
import com.wechat.entity.ClearingWechatPayment;

public interface ClearingWechatPaymentMng {

	ClearingWechatPayment add(Long userId, BigDecimal clearingFee, String status,
			String externalNo, String type, String ip);
	
	ClearingWechatPayment get(Long id);
	
	ClearingWechatPayment getByExternalNo(String type, String externalNo);
	
	void clearing(Long id,ClearingEvent clearingEvent);
	
	Pagination getPage(Date startCreateTime, Date endCreateTime, Integer pageNo);
	
	public static interface ClearingEvent{
		public boolean handleEvent(ClearingWechatPayment clearingWechatPayment);
	}
}
