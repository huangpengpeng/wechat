package com.wechat.entity;

import java.util.Date;

import javax.persistence.Entity;

import com.wechat.entity.base.BasePartner;

@Entity
public class Partner extends BasePartner {

	public Partner() {
	}

	public Partner(String name, String appId, String secretKey, String mchId, String signKey, String deviceInfo,
			String realm, String token, Date registerTime, String callUrl,Boolean ifPushFlg) {
		super(name, appId, secretKey, mchId, signKey, deviceInfo, realm, token, registerTime, callUrl,ifPushFlg);
	}

	public void init() {
	}

	private static final long serialVersionUID = -8732787732173346795L;
}
