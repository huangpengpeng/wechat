package com.wechat.manager.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import com.common.counter.manager.NumberMng;
import com.common.jdbc.page.Pagination;
import com.wechat.dao.ClearingWechatPaymentDao;
import com.wechat.entity.ClearingWechatPayment;
import com.wechat.entity.WechatWallet;
import com.wechat.manager.ClearingWechatPaymentMng;
import com.wechat.manager.WechatWalletMng;

@Transactional(isolation = Isolation.REPEATABLE_READ)
@Service
public class ClearingWechatPaymentMngImpl implements ClearingWechatPaymentMng {

	@Override
	public ClearingWechatPayment add(Long userId, BigDecimal clearingFee, String status, String externalNo, String type,
			BigDecimal allFee, BigDecimal remainderFee, BigDecimal freezeFee, String ip) {
		if (clearingFee == null || clearingFee.compareTo(BigDecimal.ZERO) == 0) {
			return null;
		}
		ClearingWechatPayment clearingWechatPayment = new ClearingWechatPayment(userId,
				numberMng.quick(NumberMng.WECHAT_CLEARING_PAYMENT_PREFIX), null, clearingFee,
				null, null, status, externalNo, type, allFee, remainderFee, freezeFee, ip);
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
		clearingWechatPayment.setRequestNo(StringUtils.remove(UUID.randomUUID().toString(), "-"));
		dao.clearing(id, clearingWechatPayment.getRequestNo());
		if (!clearingEvent.handleEvent(clearingWechatPayment)) {
			throw new IllegalStateException("转账失败");
		}
	}

	@Override
	public Pagination getPage(Long userId, String externalNo,String status, Date startCreateTime, Date endCreateTime, Integer pageNo,Integer pageSize) {
		return dao.getPage(userId, externalNo,status,startCreateTime, endCreateTime, pageNo,pageSize);
	}

	@Override
	public void repair(Long id, BigDecimal clearingFee, ClearingEvent clearingEvent) {
		ClearingWechatPayment clearingWechatPayment = dao.get(id);
		dao.repair(id, clearingFee);
		if (!clearingEvent.handleEvent(clearingWechatPayment)) {
			throw new IllegalStateException("修复失败");
		}
	}

	@Override
	public void transactionalClearing(TransactionalClearing transactionalClearing) {
		if (!transactionalClearing.handleEvent()) {
			throw new IllegalStateException("操作失败");
		}
	}

	@Override
	public void update(Long id, String number) {
		dao.update(id, number);
	}
	
	@Autowired
	private NumberMng numberMng;
	@Autowired
	private WechatWalletMng wechatWalletMng;
	@Autowired
	private ClearingWechatPaymentDao dao;
}
