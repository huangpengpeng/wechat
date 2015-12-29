package com.wechat.entity;

import java.util.Date;

import javax.persistence.Entity;

import com.wechat.entity.base.BaseWechatWallet;

@Entity
public class WechatWallet extends BaseWechatWallet{
	
	public WechatWallet(){}

	public WechatWallet(String username, String wechatPayNo,String externalNo) {
		super(username, wechatPayNo,externalNo);
	}

	public void init(){
		if(getCreateTime()==null){
			setCreateTime(new Date());
		}
	}
	
	private static final long serialVersionUID = -1671739112157729024L;
}
