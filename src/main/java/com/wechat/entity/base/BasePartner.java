package com.wechat.entity.base;

import java.util.Date;

import javax.persistence.Column;

import com.common.jdbc.BaseEntity;

/**
 * 微信公众号合作方
 * 
 * @author yz
 *
 */
@javax.persistence.MappedSuperclass
public class BasePartner extends BaseEntity {

	private static final long serialVersionUID = -5086179935270931118L;

	public BasePartner(){}
	
	public BasePartner(String name, String appId, String secretKey, String token) {
		this.setName(name);
		this.setAppId(appId);
		this.setSecretKey(secretKey);
		this.setToken(token);
	}

	/**
	 * 公众号名称
	 */
	@Column(unique = true)
	private String name;

	/**
	 * 公众号AppId
	 */
	@Column(unique=true)
	private String appId;

	/**
	 * 公众号密钥
	 */
	private String secretKey;
	
	/**
	 * 商户编号
	 */
	private String mchId;
	
	/**
	 * 签名KEY
	 */
	private String signKey;
	
	/**
	 * 支付设备号
	 */
	private String deviceInfo;
	
	/**
	 * 公众号授权域名
	 */
	private String realm;

	/**
	 * 公众号填写的token
	 */
	@Column(unique=true)
	private String token;

	/**
	 * 注册时间
	 */
	private Date registerTime;

	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSecretKey() {
		return secretKey;
	}

	public void setSecretKey(String secretKey) {
		this.secretKey = secretKey;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public Date getRegisterTime() {
		return registerTime;
	}

	public void setRegisterTime(Date registerTime) {
		this.registerTime = registerTime;
	}

	public String getMchId() {
		return mchId;
	}

	public void setMchId(String mchId) {
		this.mchId = mchId;
	}

	public String getSignKey() {
		return signKey;
	}

	public void setSignKey(String signKey) {
		this.signKey = signKey;
	}

	public String getDeviceInfo() {
		return deviceInfo;
	}

	public void setDeviceInfo(String deviceInfo) {
		this.deviceInfo = deviceInfo;
	}

	public String getRealm() {
		return realm;
	}

	public void setRealm(String realm) {
		this.realm = realm;
	}
}
