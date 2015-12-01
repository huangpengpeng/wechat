package com.wechat.manager.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import com.wechat.dao.QrcodeDao;
import com.wechat.entity.Qrcode;
import com.wechat.manager.QrcodeMng;

@Transactional(isolation=Isolation.REPEATABLE_READ)
@Service
public class QrcodeMngImpl implements QrcodeMng {
	
	public Qrcode add(Long partnerId, String ticket, String sign) {
		Qrcode qrcode=new Qrcode(partnerId, ticket, sign);
		qrcode.init();
		qrcode.setId(dao.add(qrcode));
		return qrcode;
	}

	public Qrcode getBySign(Long partnerId, String sign) {
		return dao.getBySign(partnerId, sign);
	}

	public void update(Long id, String ticket) {
		dao.update(id, ticket);
	}

	public Qrcode get(Long id) {
		return dao.get(id);
	}

	@Autowired
	private QrcodeDao dao;
}
