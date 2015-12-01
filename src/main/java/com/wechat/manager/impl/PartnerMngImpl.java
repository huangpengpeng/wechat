package com.wechat.manager.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import com.wechat.dao.PartnerDao;
import com.wechat.entity.Partner;
import com.wechat.manager.PartnerMng;

@Transactional(isolation=Isolation.REPEATABLE_READ)
@Service
public class PartnerMngImpl implements PartnerMng{

	public Partner get(Long id) {
		return dao.get(id);
	}

	public Partner getByToken(String token) {
		return dao.getByToken(token);
	}
	
	@Autowired
	private PartnerDao dao;
}
