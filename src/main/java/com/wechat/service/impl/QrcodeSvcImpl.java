package com.wechat.service.impl;

import me.chanjar.weixin.common.exception.WxErrorException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import com.wechat.entity.Partner;
import com.wechat.entity.Qrcode;
import com.wechat.manager.PartnerMng;
import com.wechat.manager.QrcodeMng;
import com.wechat.manager.impl.QrcodeMngImpl;
import com.wechat.plugins.WechatConfigSvc;
import com.wechat.service.QrcodeSvc;

@Transactional(isolation = Isolation.REPEATABLE_READ)
@Service
public class QrcodeSvcImpl implements QrcodeSvc {
	
	private Logger log=LoggerFactory.getLogger(QrcodeMngImpl.class);

	public Qrcode add(Long partnerId, String sign) throws WxErrorException {
		log.info("wecaht add qrcode partnerId:{} sign:{}", partnerId, sign);
		Partner partner = partnerMng.get(partnerId);
		Qrcode qrcode = manager.add(partnerId, null, sign);
		String ticket = wechatConfigSvc
				.createWxMpService(partner.getAppId(), partner.getSecretKey(),
						partner.getToken())
				.qrCodeCreateLastTicket(qrcode.getId().intValue()).getTicket();
		StringBuffer buffer = new StringBuffer(
				"https://mp.weixin.qq.com/cgi-bin/showqrcode?ticket=");
		buffer.append(ticket);
		qrcode.setTicket(buffer.toString());
		manager.update(qrcode.getId(), qrcode.getTicket());
		return qrcode;
	}

	@Autowired
	private WechatConfigSvc wechatConfigSvc;
	@Autowired
	private PartnerMng partnerMng;
	@Autowired
	private QrcodeMng manager;
}
