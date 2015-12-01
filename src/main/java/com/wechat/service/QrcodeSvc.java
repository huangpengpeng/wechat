package com.wechat.service;

import me.chanjar.weixin.common.exception.WxErrorException;

import com.wechat.entity.Qrcode;

public interface QrcodeSvc {

	/**
	 * 增加二维码
	 * 
	 * @param partnerId
	 * @param sign
	 * @return
	 * @throws WxErrorException 
	 */
	Qrcode add(Long partnerId, String sign) throws WxErrorException;
}
