package com.wechat.dao;

import com.wechat.entity.Partner;

public interface PartnerDao {
	
	 Partner getByToken(String token);

	Partner get(Long id);
}
