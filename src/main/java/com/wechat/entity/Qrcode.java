package com.wechat.entity;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import com.wechat.entity.base.BaseQrcode;

@Entity
@Table(uniqueConstraints = { @UniqueConstraint(columnNames = { "partnerId",
		"sign" }) })
public class Qrcode extends BaseQrcode {

	public Qrcode(Long partnerId, String ticket, String sign) {
		super(partnerId, ticket, sign);
	}

	public Qrcode() {
	}
	
	public void init(){}

	private static final long serialVersionUID = 4225600320896663028L;
}
