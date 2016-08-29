package com.wechat.manager;

import java.math.BigDecimal;
import java.util.Date;

import com.common.jdbc.page.Pagination;
import com.wechat.entity.ClearingWechatPayment;

public interface ClearingWechatPaymentMng {
	
	ClearingWechatPayment add(Long userId, BigDecimal clearingFee, String status,
			String externalNo, String type, BigDecimal allFee,
			BigDecimal remainderFee, BigDecimal freezeFee,String ip);
	
	ClearingWechatPayment get(Long id);
	
	ClearingWechatPayment getByExternalNo(String type, String externalNo);
	
	void repair(Long id,BigDecimal clearingFee,ClearingEvent clearingEvent);
	
	void clearing(Long id,ClearingEvent clearingEvent);
	
	Pagination getPage(Long userId,String externalNo,String status,Date startCreateTime, Date endCreateTime, Integer pageNo,Integer pageSize);
	
	void transactionalClearing(TransactionalClearing transactionalClearing);
	
	void update(Long id,String number);
	
	public static interface ClearingEvent{
		public boolean handleEvent(ClearingWechatPayment clearingWechatPayment);
	}
	
	public static interface TransactionalClearing{
		public boolean handleEvent();
	}
}
