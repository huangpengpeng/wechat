package com.wechat.entity.base;

import com.common.jdbc.BaseEntity;

/**
 * 微信公众号
 * @author yz
 *
 */
@javax.persistence.MappedSuperclass
public class BaseQrcode extends BaseEntity {

	private static final long serialVersionUID = -6418795879573657415L;

	public BaseQrcode(long partnerId, String ticket, String sign){
		this.setPartnerId(partnerId);
		this.setTicket(ticket);
		this.setSign(sign);
	}
	
	public BaseQrcode(){}

	/**
	 * 公众号ID
	 */
	private Long partnerId;
	/**
	 * 二维码ticket
	 */
	private String ticket;
	/**
	 * 标识
	 */
	private String sign;

	public Long getPartnerId() {
		return partnerId;
	}

	public void setPartnerId(Long partnerId) {
		this.partnerId = partnerId;
	}

	public String getTicket() {
		return ticket;
	}

	public void setTicket(String ticket) {
		this.ticket = ticket;
	}

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}
}
