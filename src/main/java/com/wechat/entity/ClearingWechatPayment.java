package com.wechat.entity;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import com.wechat.entity.base.BaseClearingWechatPayment;

/**
 * 微信转账清单
 * @author Administrator
 *
 */
@Entity
@Table(uniqueConstraints = { @UniqueConstraint(columnNames = { "externalNo",
		"type" }) })
public class ClearingWechatPayment extends BaseClearingWechatPayment{
	
	public ClearingWechatPayment(){}

	public ClearingWechatPayment(Long userId, Date createTime,
			BigDecimal clearingFee, Date clearingTime, String requestNo,
			String status, String externalNo, String type, BigDecimal allFee,
			BigDecimal remainderFee, BigDecimal freezeFee, String ip) {
		super(userId, createTime, clearingFee, clearingTime, requestNo, status,
				externalNo, type, allFee, remainderFee, freezeFee, ip);
	}
	
	public void init() {
		if (getCreateTime() == null) {
			setCreateTime(new Date());
		}
		if (getStatus() == null) {
			setStatus(ClearingwWechatPaymentStatus.未支付.toString());
		}
	}
	
	public enum ClearingwWechatPaymentStatus{
		未支付,已支付
	}
	
	public enum Type{
		佣金,分账,收货款
	}
	
	private static final long serialVersionUID = -4092008137144914268L;
}
