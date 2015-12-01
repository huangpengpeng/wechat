package com.wechat.entity;

import javax.persistence.Entity;

import com.wechat.entity.base.BasePartner;

@Entity
public class Partner extends BasePartner{
	
	public Partner(){}

	public Partner(String name, String appId, String secretKey, String token) {
		super(name, appId, secretKey, token);
	}
	
	public void init(){}

	private static final long serialVersionUID = -8732787732173346795L;
}
