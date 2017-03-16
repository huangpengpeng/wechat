package com.wechat.dao;

import java.util.Date;
import java.util.List;

import com.wechat.entity.Partner;

public interface PartnerDao {

	public long add(Partner partner);
	
	public void delete(Long id);
	
	public void update(Long id, String name, String appId, String secretKey, String mchId, String signKey,
			String deviceInfo, String realm, String token, Date registerTime, String callUrl,Boolean ifPushFlg); 
	
	Partner getByToken(String token);

	Partner get(Long id);
	
	public List<Partner> getList();
}
