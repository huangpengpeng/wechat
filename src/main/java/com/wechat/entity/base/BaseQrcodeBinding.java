package com.wechat.entity.base;

import java.util.Date;

import com.common.jdbc.BaseEntity;

/**
 * 微信绑定
 * @author yz
 *
 */
@javax.persistence.MappedSuperclass

public class BaseQrcodeBinding extends BaseEntity {

	private static final long serialVersionUID = 3322808443651410247L;

	public BaseQrcodeBinding(){}
	
	public BaseQrcodeBinding(Long partnerId,Long qrcodeId, Long wechatUserId){
		this.setPartnerId(partnerId);
		this.setQrcodeId(qrcodeId);
		this.setWechatUserId(wechatUserId);
	}

	/**
	 * 公众号编号
	 */
	private Long partnerId;
	/**
	 * 公众号二维码Id
	 */
	private long qrcodeId;
	/**
	 * 用户编号
	 */
	private Long wechatUserId;
	/**
	 * 绑定时间
	 */
	private Date createtime;

	public long getQrcodeId() {
		return qrcodeId;
	}

	public void setQrcodeId(long qrcodeId) {
		this.qrcodeId = qrcodeId;
	}

	public Date getCreatetime() {
		return createtime;
	}

	public void setCreatetime(Date createtime) {
		this.createtime = createtime;
	}

	public Long getPartnerId() {
		return partnerId;
	}

	public void setPartnerId(Long partnerId) {
		this.partnerId = partnerId;
	}

	public Long getWechatUserId() {
		return wechatUserId;
	}

	public void setWechatUserId(Long wechatUserId) {
		this.wechatUserId = wechatUserId;
	}
}
