package com.wechat.manager.impl;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import com.wechat.dao.PartnerDao;
import com.wechat.entity.Partner;
import com.wechat.manager.PartnerMng;

@Transactional(isolation = Isolation.REPEATABLE_READ)
@Service
public class PartnerMngImpl implements PartnerMng {

	@Override
	public Partner add(String name, String appId, String secretKey, String mchId, String signKey, String deviceInfo,
			String realm, String token, Date registerTime, String callUrl) {
		Partner partner = new Partner(name, appId, secretKey, mchId, signKey, deviceInfo, realm, token, registerTime,
				callUrl);
		partner.init();
		partner.setId(dao.add(partner));
		return partner;
	}

	@Override
	public void delete(Long id) {
		dao.delete(id);
	}

	@Override
	public void update(Long id, String name, String appId, String secretKey, String mchId, String signKey,
			String deviceInfo, String realm, String token, Date registerTime, String callUrl) {
		dao.update(id, name, appId, secretKey, mchId, signKey, deviceInfo, realm, token, registerTime, callUrl);
	}

	public Partner get(Long id) {
		return dao.get(id);
	}

	public Partner getByToken(String token) {
		return dao.getByToken(token);
	}

	@Autowired
	private PartnerDao dao;
}
