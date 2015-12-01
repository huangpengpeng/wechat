package com.wechat.entity.base;

import java.util.Date;

import com.common.jdbc.BaseEntity;

@javax.persistence.MappedSuperclass
public class BaseWechatUser extends BaseEntity{

	private static final long serialVersionUID = -901825183812957421L;
	
	public BaseWechatUser(){}
	
	public BaseWechatUser(Long partnerId,Long externalNo,String openId){
		this.setPartnerId(partnerId);
		this.setExternalNo(externalNo);
		this.setOpenId(openId);
	}

	/**
	 * 合作伙伴编号
	 */
	private Long partnerId;

	/**
	 * 外部编号（一般指外部用户编号）
	 */
	private Long externalNo;
	
	/**
	 * 微信用户唯一标示
	 */
	private String openId;
	
	/**
	 * 注册时间
	 */
	private Date registerTime;

	public Long getPartnerId() {
		return partnerId;
	}

	public void setPartnerId(Long partnerId) {
		this.partnerId = partnerId;
	}

	public String getOpenId() {
		return openId;
	}

	public void setOpenId(String openId) {
		this.openId = openId;
	}

	public Date getRegisterTime() {
		return registerTime;
	}

	public void setRegisterTime(Date registerTime) {
		this.registerTime = registerTime;
	}
	
	public Long getExternalNo() {
		return externalNo;
	}

	public void setExternalNo(Long externalNo) {
		this.externalNo = externalNo;
	}
}
