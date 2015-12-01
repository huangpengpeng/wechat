package com.wechat.manager.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import com.wechat.dao.WechatUserDao;
import com.wechat.entity.WechatUser;
import com.wechat.manager.WechatUserMng;


@Transactional(isolation = Isolation.REPEATABLE_READ)
@Service
public class WechatUserMngImpl implements WechatUserMng{

	public WechatUser add(Long partnerId, Long externalNo, String openId) {
		WechatUser wechatUser = new WechatUser(partnerId, externalNo, openId);
		wechatUser.init();
		wechatUser.setId(dao.add(wechatUser));
		return wechatUser;
	}

	public WechatUser getByOpenId(Long partnerId, String openId) {
		return dao.getByOpenId(partnerId, openId);
	}


	public void update(Long id, Long externalNo) {
		dao.update(id, externalNo);
	}
	
	@Autowired
	private WechatUserDao dao;
}
