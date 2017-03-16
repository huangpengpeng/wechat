package com.wechat.manager.impl;

import java.util.Date;
import java.util.List;

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
			String realm, String token, Date registerTime, String callUrl, Boolean ifPushFlg) {
		Partner partner = new Partner(name, appId, secretKey, mchId, signKey, deviceInfo, realm, token, registerTime,
				callUrl, ifPushFlg);
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
			String deviceInfo, String realm, String token, Date registerTime, String callUrl, Boolean ifPushFlg) {
		dao.update(id, name, appId, secretKey, mchId, signKey, deviceInfo, realm, token, registerTime, callUrl,
				ifPushFlg);
	}

	@Override
	public Partner get(Long id) {
		return dao.get(id);
	}

	@Override
	public Partner getByToken(String token) {
		return dao.getByToken(token);
	}

	@Override
	public List<Partner> getList() {
		return dao.getList();
	}

	@Autowired
	private PartnerDao dao;

}
