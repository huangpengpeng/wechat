package com.wechat.manager.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import com.common.jdbc.Updater;
import com.wechat.dao.WeiXinDao;
import com.wechat.entity.WeiXin;
import com.wechat.manager.WeiXinMng;

@Transactional(isolation = Isolation.REPEATABLE_READ)
@Service
public class WeiXinMngImpl implements WeiXinMng{

	@Override
	public WeiXin add(Long userId, String openid, String unionid) {
		WeiXin entity  = new WeiXin(userId, openid, unionid);
		entity.init();
		entity.setId(dao.add(entity));
		return entity;
	}

	@Override
	public WeiXin getByUnionid(String unionid) {
		return dao.getByUnionid(unionid);
	}

	@Override
	public WeiXin getByUser(Long userId) {
		return dao.getByUser(userId);
	}

	@Override
	public WeiXin updateByUpdater(WeiXin weiXin) {
		Updater<WeiXin> updater = new Updater<WeiXin>(weiXin);
		dao.updateByUpdater(updater);
		return updater.getBean();
	}
	@Override
	public void delete(Long id) {
		dao.delete(id);
	}
	
	@Autowired
	private WeiXinDao dao;
}
