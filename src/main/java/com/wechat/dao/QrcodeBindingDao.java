package com.wechat.dao;

import com.wechat.entity.QrcodeBinding;

public interface QrcodeBindingDao {

	void add(QrcodeBinding qrcodeBinding);
	
	QrcodeBinding getByQrcode(Long partnerId, Long wechatUserId);
	
	void delete(Long id);
}
