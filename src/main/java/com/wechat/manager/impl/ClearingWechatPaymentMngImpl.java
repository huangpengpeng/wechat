package com.wechat.manager.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import com.common.jdbc.page.Pagination;
import com.wechat.dao.ClearingWechatPaymentDao;
import com.wechat.entity.ClearingWechatPayment;
import com.wechat.manager.ClearingWechatPaymentMng;
@Transactional(isolation=Isolation.REPEATABLE_READ)
@Service
public class ClearingWechatPaymentMngImpl implements ClearingWechatPaymentMng{

	@Override
	public ClearingWechatPayment add(Long userId, BigDecimal clearingFee,
			String status, String externalNo, String type, String ip) {
		if (clearingFee == null || clearingFee.compareTo(BigDecimal.ZERO) == 0) {
			return null;
		}
		ClearingWechatPayment clearingWechatPayment = new ClearingWechatPayment(
				userId, null, clearingFee, null, null, status, externalNo,
				type, ip);
		clearingWechatPayment.init();
		clearingWechatPayment.setId(dao.add(clearingWechatPayment));
		return clearingWechatPayment;
	}

	@Override
	public ClearingWechatPayment get(Long id) {
		return dao.get(id);
	}

	@Override
	public ClearingWechatPayment getByExternalNo(String type, String externalNo) {
		return dao.getByExternalNo(type, externalNo);
	}

	@Override
	public void clearing(Long id, ClearingEvent clearingEvent) {
		ClearingWechatPayment clearingWechatPayment = dao.get(id);
		clearingWechatPayment.setRequestNo(StringUtils.remove(UUID.randomUUID()
				.toString(), "-"));
		dao.clearing(id, clearingWechatPayment.getRequestNo());
		if (clearingEvent.handleEvent(clearingWechatPayment)) {
			throw new IllegalStateException("转账失败");
		}
	}
	
	@Override
	public Pagination getPage(Date startCreateTime, Date endCreateTime,
			Integer pageNo) {
		return dao.getPage(startCreateTime, endCreateTime, pageNo);
	}
	
	@Autowired
	private ClearingWechatPaymentDao dao;
}
