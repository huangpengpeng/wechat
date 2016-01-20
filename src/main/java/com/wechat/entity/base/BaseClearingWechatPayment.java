package com.wechat.entity.base;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;

import com.common.jdbc.VersionEntity;

@javax.persistence.MappedSuperclass
public class BaseClearingWechatPayment extends VersionEntity{

	private static final long serialVersionUID = -23648241372697966L;
	
	public static final String PRE_USER_ID="userId";
	
	public BaseClearingWechatPayment(){}
	
	public BaseClearingWechatPayment(Long userId, Date createTime,
			BigDecimal clearingFee, Date clearingTime, String requestNo,
			String status, String externalNo, String type, BigDecimal allFee,
			BigDecimal remainderFee, BigDecimal freezeFee, String ip) {
		this.setUserId(userId);
		this.setCreateTime(createTime);
		this.setClearingFee(clearingFee);
		this.setClearingTime(clearingTime);
		this.setRequestNo(requestNo);
		this.setStatus(status);
		this.setExternalNo(externalNo);
		this.setType(type);
		this.setIp(ip);
		this.setAllFee(allFee);
		this.setRemainderFee(remainderFee);
		this.setFreezeFee(freezeFee);
	}

	/**
	 * 入账方
	 */
	private Long userId;
	
	/**
	 * 创建时间
	 */
	private Date createTime;
	
	/**
	 * 结算金额
	 */
	@Column(precision = 18, scale = 2)
	private BigDecimal clearingFee;
	
	/**
	 * 结算时间
	 */
	private Date clearingTime;
	
	/**
	 * 转账流水号
	 */
	@Column(unique=true)
	private String requestNo;
	
	/**
	 * 支付状态
	 */
	private String status;
	
	/**
	 * 外部订单编号（分账编号 | 推广编号）
	 */
	private String externalNo;
	
	/**
	 * 转账类型（分账|订单推广）
	 */
	private String type;
	
	/**
	 * 账户总余额
	 */
	@Column(precision = 18, scale = 2)
	private BigDecimal allFee;
	
	/**
	 * 账户余额
	 */
	@Column(precision = 18, scale = 2)
	private BigDecimal remainderFee;
	
	/**
	 * 账户冻结金额
	 */
	@Column(precision = 18, scale = 2)
	private BigDecimal freezeFee;
	
	/**
	 * 转账IP
	 */
	private String ip;

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public BigDecimal getClearingFee() {
		return clearingFee;
	}

	public void setClearingFee(BigDecimal clearingFee) {
		this.clearingFee = clearingFee;
	}

	public Date getClearingTime() {
		return clearingTime;
	}

	public void setClearingTime(Date clearingTime) {
		this.clearingTime = clearingTime;
	}

	public String getRequestNo() {
		return requestNo;
	}

	public void setRequestNo(String requestNo) {
		this.requestNo = requestNo;
	}


	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getExternalNo() {
		return externalNo;
	}

	public void setExternalNo(String externalNo) {
		this.externalNo = externalNo;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public BigDecimal getAllFee() {
		return allFee;
	}

	public void setAllFee(BigDecimal allFee) {
		this.allFee = allFee;
	}

	public BigDecimal getRemainderFee() {
		return remainderFee;
	}

	public void setRemainderFee(BigDecimal remainderFee) {
		this.remainderFee = remainderFee;
	}

	public BigDecimal getFreezeFee() {
		return freezeFee;
	}

	public void setFreezeFee(BigDecimal freezeFee) {
		this.freezeFee = freezeFee;
	}
}
