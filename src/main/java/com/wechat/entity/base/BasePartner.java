package com.wechat.entity.base;

import java.util.Date;

import javax.persistence.MappedSuperclass;

import com.common.jdbc.BaseEntity;
import com.common.jdbc.Comment;
import com.common.jdbc.VersionEntity;

/**
 * 微信公众号合作方
 */
@MappedSuperclass
public class BasePartner extends BaseEntity {

	private static final long serialVersionUID = -5086179935270931118L;

	public BasePartner(){}
	
	public BasePartner(String name, String appId, String secretKey, String token) {
		this.setName(name);
		this.setAppId(appId);
		this.setSecretKey(secretKey);
		this.setToken(token);
	}

	@Comment(value={"公众号名称"})
	private String name;

	@Comment(value={"公众号AppId"})
	private String appId;

	@Comment(value={"公众号密钥"})
	private String secretKey;
	
	@Comment(value={" 商户编号"})
	private String mchId;
	
	@Comment(value={"签名KEY"})
	private String signKey;
	
	@Comment(value={"支付设备号"})
	private String deviceInfo;
	
	@Comment(value={"公众号授权域名"})
	private String realm;

	@Comment(value={"公众号填写的token"})
	private String token;

	@Comment(value={"注册时间"})
	private Date registerTime;
	
	@Comment(value="回调地址")
	private String callUrl;

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

	public String getCallUrl() {
		return callUrl;
	}

	public void setCallUrl(String callUrl) {
		this.callUrl = callUrl;
	}
}
