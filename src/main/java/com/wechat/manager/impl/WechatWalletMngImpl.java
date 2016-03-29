package com.wechat.manager.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.wechat.dao.WechatWalletDao;
import com.wechat.entity.WechatWallet;
import com.wechat.manager.WechatWalletMng;

@Transactional(isolation = Isolation.REPEATABLE_READ)
@Service
public class WechatWalletMngImpl implements WechatWalletMng{

	@Override
	public WechatWallet add(String username, String wechatPayNo, String externalNo) {
		WechatWallet wechatWallet=new WechatWallet(username, wechatPayNo, externalNo);
		wechatWallet.init();
		wechatWallet.setId(dao.add(wechatWallet));
		return wechatWallet;
	}

	@Override
	public WechatWallet get(Long id) {
		return dao.get(id);
	}

	@Override
	public WechatWallet update(Long id, String username, String wechatPayNo,Boolean identificationFlag) {
		dao.update(id, username, wechatPayNo,identificationFlag);
		return dao.get(id);
	}

	@Override
	public WechatWallet getByexternalNo(String externalNo) {
		return dao.getByexternalNo(externalNo);
	}


	@Autowired
	private WechatWalletDao dao;
}
