package com.wechat.dao;

import java.math.BigDecimal;
import java.util.Date;

import com.common.jdbc.page.Pagination;
import com.wechat.entity.ClearingWechatPayment;

public interface ClearingWechatPaymentDao {

	long add(ClearingWechatPayment clearingWechatPayment);
	
	ClearingWechatPayment get(Long id);
	
	ClearingWechatPayment getByExternalNo(String type ,String externalNo);
	
	Pagination getPage(Long userId,String externalNo,String status,Date startCreateTime, Date endCreateTime,
			Integer pageNo,Integer pageSize);
	
	void clearing(Long id,String requestNo);
	
	void repair(Long id ,BigDecimal clearingFee);
	
	void update(Long id, String number) ;
}
