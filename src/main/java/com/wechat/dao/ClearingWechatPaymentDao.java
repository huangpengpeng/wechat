package com.wechat.dao;

import java.util.Date;

import com.common.jdbc.page.Pagination;
import com.wechat.entity.ClearingWechatPayment;

public interface ClearingWechatPaymentDao {

	long add(ClearingWechatPayment clearingWechatPayment);
	
	ClearingWechatPayment get(Long id);
	
	ClearingWechatPayment getByExternalNo(String type ,String externalNo);
	
	Pagination getPage(Long userId,Date startCreateTime, Date endCreateTime,
			Integer pageNo);
	
	void clearing(Long id,String requestNo );
}
