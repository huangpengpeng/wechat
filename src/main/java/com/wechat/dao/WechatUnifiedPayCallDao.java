package com.wechat.dao;

import java.util.List;

import com.wechat.entity.WechatUnifiedPayCall;

public interface WechatUnifiedPayCallDao {

	public long add(WechatUnifiedPayCall unifiedCall);

	WechatUnifiedPayCall getByRequestNo(String requestNo);

	List<WechatUnifiedPayCall> getByExternalNo(String externalNo);

	public void update(Long id, String responseText, String responseCode,
			String responseMsg);
}
