package com.wechat.entity;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import com.wechat.entity.base.BaseClearingwWechatPayment;

/**
 * 微信转账清单
 * @author Administrator
 *
 */
@Entity
@Table(uniqueConstraints = { @UniqueConstraint(columnNames = { "externalNo",
		"type" }) })
public class ClearingwWechatPayment extends BaseClearingwWechatPayment{
	
	public ClearingwWechatPayment(){}

	public ClearingwWechatPayment(Long userId, Date createTime,
			BigDecimal clearingFee, Date clearingTime, String requestNo,
			String status, String externalNo, String type, String ip) {
		super(userId, createTime, clearingFee, clearingTime, requestNo, status,
				externalNo, type, ip);
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
	
	private static final long serialVersionUID = -4092008137144914268L;
}
