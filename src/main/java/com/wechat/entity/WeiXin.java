package com.wechat.entity;

import javax.persistence.Entity;

import com.common.util.DateTimeUtils;
import com.wechat.entity.base.BaseWeiXin;

@Entity
public class WeiXin extends BaseWeiXin{
	
	public static String APPID = "wx49294a805cee197c";
	
	public static String SECRET ="64457e211e0437591bce35d9e7636a03";

	private static final long serialVersionUID = -3225261788149750768L;

	public WeiXin() {
	}

	public WeiXin(Long userId, String openid, String unionid) {
		super(userId, openid, unionid);
	}
	
	public void init(){
		if(getCreateTime() == null){
			setCreateTime(DateTimeUtils.getNow());
		}
	}
}
