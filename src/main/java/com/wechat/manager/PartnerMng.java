package com.wechat.manager;

import java.util.Date;
import java.util.List;

import com.wechat.entity.Partner;

public interface PartnerMng {

	public Partner add(String name, String appId, String secretKey, String mchId, String signKey,
			String deviceInfo, String realm, String token, Date registerTime, String callUrl,Boolean ifPushFlg);

	public void delete(Long id);

	public void update(Long id, String name, String appId, String secretKey, String mchId, String signKey,
			String deviceInfo, String realm, String token, Date registerTime, String callUrl,Boolean ifPushFlg);

	Partner get(Long id);

	Partner getByToken(String token);
	
	public List<Partner> getList();
}
