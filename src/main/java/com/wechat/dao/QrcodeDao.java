package com.wechat.dao;

import com.wechat.entity.Qrcode;

public interface QrcodeDao {
	
	Qrcode get(Long id);

	long add(Qrcode qrcode);
	
	Qrcode getBySign(Long partnerId, String sign);
	
	public void update(Long id, String ticket);
}
