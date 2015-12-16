package com.wechat.manager.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wechat.dao.WechatUnifiedPayCallDao;
import com.wechat.entity.WechatUnifiedPayCall;
import com.wechat.manager.WechatUnifiedPayCallMng;

@Transactional
@Service
public class WechatUnifiedPayCallMngImpl implements WechatUnifiedPayCallMng{

	@Override
	public WechatUnifiedPayCall add(Long userId, String appId, String mchId,
			String requestNo, String externalNo,
			String reqeustText, String api) {
		WechatUnifiedPayCall unifiedPayCall = new WechatUnifiedPayCall(userId,
				appId, mchId, requestNo, externalNo,
				reqeustText, null, null, null, api);
		unifiedPayCall.init();
		unifiedPayCall.setId(dao.add(unifiedPayCall));
		return unifiedPayCall;
	}

	@Override
	public WechatUnifiedPayCall addNotify(Long userId, String externalNo,
			String api, String responseText, String responseCode,
			String responseMsg) {
		WechatUnifiedPayCall unifiedPayCall = new WechatUnifiedPayCall(userId,
				null, null, externalNo, null, null, null, null, null, api);
		unifiedPayCall.init();
		unifiedPayCall.setId(dao.add(unifiedPayCall));
		return unifiedPayCall;
	}

	@Override
	public WechatUnifiedPayCall getByRequestNo(String requestNo) {
		return dao.getByRequestNo(requestNo);
	}

	@Override
	public List<WechatUnifiedPayCall> getByExternalNo(String externalNo) {
		return dao.getByExternalNo(externalNo);
	}

	@Override
	public void update(Long id, String responseText, String responseCode,
			String responseMsg) {
		dao.update(id, responseText, responseCode, responseMsg);
	}
	
	@Autowired
	private WechatUnifiedPayCallDao dao;
}
