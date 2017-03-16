package com.wechat.manager;

import com.wechat.entity.Qrcode;

public interface QrcodeMng {

	Qrcode add(Long partnerId, String ticket, String sign);
	
	void update(Long id,String ticket);

	Qrcode get(Long id);
	
	Qrcode getBySign(Long partnerId, String sign);
	
	
}
