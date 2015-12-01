package com.wechat.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import com.wechat.entity.base.BaseWechatUser;

@Entity
@Table(uniqueConstraints = { @UniqueConstraint(columnNames = { "partnerId", "openId" }) })
public class WechatUser extends BaseWechatUser {
	
	public WechatUser(){
		
	}

	public WechatUser(Long partnerId, Long externalNo, String openId) {
		super(partnerId, externalNo, openId);
	}

	public void init() {
		if (getRegisterTime() == null) {
			setRegisterTime(new Date());
		}
	}

	private static final long serialVersionUID = 8465895926859182587L;
}
