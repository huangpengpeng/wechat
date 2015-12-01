package com.wechat.manager;

import com.wechat.entity.Partner;

public interface PartnerMng {

	Partner get(Long id);
	
	Partner getByToken(String token);
}
