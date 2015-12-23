package com.wechat.manager.impl;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import com.wechat.dao.ClearingwWechatPaymentDao;
import com.wechat.entity.ClearingwWechatPayment;
import com.wechat.manager.ClearingwWechatPaymentMng;

@Transactional(isolation=Isolation.REPEATABLE_READ)
@Service
public class ClearingwWechatPaymentMngImpl implements ClearingwWechatPaymentMng{

	@Override
	public ClearingwWechatPayment add(Long userId, BigDecimal clearingFee,
			String status, String externalNo, String type, String ip) {
		ClearingwWechatPayment clearingwWechatPayment = new ClearingwWechatPayment(
				userId, null, clearingFee, null, null, status, externalNo,
				type, ip);
		clearingwWechatPayment.init();
		clearingwWechatPayment.setId(dao.add(clearingwWechatPayment));
		return clearingwWechatPayment;
	}

	@Override
	public ClearingwWechatPayment get(Long id) {
		return dao.get(id);
	}

	@Override
	public ClearingwWechatPayment getByExternalNo(String type, String externalNo) {
		return dao.getByExternalNo(type, externalNo);
	}

	@Autowired
	private ClearingwWechatPaymentDao dao;
}
