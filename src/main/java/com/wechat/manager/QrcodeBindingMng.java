package com.wechat.manager;

import com.wechat.entity.QrcodeBinding;

public interface QrcodeBindingMng {

	void add(Long partnerId, Long qrcodeId, Long wechatUserId);
	
	QrcodeBinding getByQrcode(Long partnerId, Long wechatUserId);
	
	void delete(Long id);
}
