package com.wechat.entity.base;

import java.util.Date;

import javax.persistence.Column;

import com.common.jdbc.VersionEntity;

@javax.persistence.MappedSuperclass
public class BaseWechatWallet extends VersionEntity{

	private static final long serialVersionUID = 5975117540571016471L;

	public BaseWechatWallet(){}
	
	public BaseWechatWallet(String username,String wechatPayNo,String externalNo){
		this.setUsername(username);
		this.setWechatPayNo(wechatPayNo);
		this.setExternalNo(externalNo);
	}
	
	/**
	 * 微信账户昵称
	 */
	private String username;
	
	/**
	 * OPENID
	 */
	private String wechatPayNo;
	
	/**
	 * 创建时间
	 */
	private Date createTime;
	
	/**
	 * 外部系统编号
	 */
	@Column(unique=true)
	private String externalNo;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getWechatPayNo() {
		return wechatPayNo;
	}

	public void setWechatPayNo(String wechatPayNo) {
		this.wechatPayNo = wechatPayNo;
	}

	public String getExternalNo() {
		return externalNo;
	}

	public void setExternalNo(String externalNo) {
		this.externalNo = externalNo;
	}
}
