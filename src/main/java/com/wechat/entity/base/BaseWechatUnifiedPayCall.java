package com.wechat.entity.base;

import javax.persistence.Lob;
import javax.persistence.MappedSuperclass;

import com.common.jdbc.BaseEntity;

@MappedSuperclass
public class BaseWechatUnifiedPayCall extends BaseEntity{

	private static final long serialVersionUID = 3227325543786352743L;
	
	public BaseWechatUnifiedPayCall(){}
	
	public BaseWechatUnifiedPayCall(Long userId, String appId, String mchId,
			String requestNo, String externalNo,
			String reqeustText, String responseText, String responseCode,
			String responseMsg, String api) {
		this.setUserId(userId);
		this.setApi(api);
		this.setMchId(mchId);
		this.setRequestNo(requestNo);
		this.setExternalNo(externalNo);
		this.setReqeustText(reqeustText);
		this.setResponseText(responseText);
		this.setResponseCode(responseCode);
		this.setResponseMsg(responseMsg);
		this.setApi(api);
	}

	/**
	 * 用户编号
	 */
	private Long userId;
	
	/**
	 * 微信APPID
	 */
	private String appId;
	
	/**
	 * 商家编号
	 */
	private String mchId;

	/**
	 * 请求流水号 | 随机号
	 */
	private String requestNo;
	
	/**
	 * 外部订单编号
	 */
	private String externalNo;
	/**
	 * 请求完整内容
	 */
	@Lob
	private String reqeustText;
	
	/**
	 * 请求相应内容
	 */
	@Lob
	private String responseText;
	
	/**
	 * 请求相应码
	 */
	private String responseCode;
	
	/**
	 * 请求响应消息
	 */
	@Lob
	private String responseMsg;
	
	/**
	 * 调用API
	 */
	private String api;

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	public String getMchId() {
		return mchId;
	}

	public void setMchId(String mchId) {
		this.mchId = mchId;
	}

	public String getRequestNo() {
		return requestNo;
	}

	public void setRequestNo(String requestNo) {
		this.requestNo = requestNo;
	}

	public String getExternalNo() {
		return externalNo;
	}

	public void setExternalNo(String externalNo) {
		this.externalNo = externalNo;
	}

	public String getReqeustText() {
		return reqeustText;
	}

	public void setReqeustText(String reqeustText) {
		this.reqeustText = reqeustText;
	}

	public String getResponseText() {
		return responseText;
	}

	public void setResponseText(String responseText) {
		this.responseText = responseText;
	}

	public String getResponseCode() {
		return responseCode;
	}

	public void setResponseCode(String responseCode) {
		this.responseCode = responseCode;
	}

	public String getResponseMsg() {
		return responseMsg;
	}

	public void setResponseMsg(String responseMsg) {
		this.responseMsg = responseMsg;
	}

	public String getApi() {
		return api;
	}

	public void setApi(String api) {
		this.api = api;
	}
}
