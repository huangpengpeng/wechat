package com.wechat.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import me.chanjar.weixin.common.util.crypto.WxCryptUtil;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.common.util.DateTimeUtils;
import com.common.util.JsonUtils;
import com.common.web.util.WebUtils;
import com.wechat.entity.WechatUnifiedPayCall;
import com.wechat.manager.WechatUnifiedPayCallMng;
import com.wechat.service.Config;
import com.wechat.service.Config.Properties;
import com.wechat.service.Config.ResponseConfig;
import com.wechat.service.Config.URL;
import com.wechat.service.WechatUnifiedPayCallSvc;

@Transactional(propagation = Propagation.REQUIRES_NEW)
@Service
public class WechatUnifiedPayCallSvcImpl implements WechatUnifiedPayCallSvc {

	@Override
	public ResponseConfig paymentSubmit(HttpServletRequest request,
			Config config, Long userId, String out_trade_no, String body,
			BigDecimal total_fee, String spbill_create_ip, String trade_type,
			String openid) {
		List<WechatUnifiedPayCall> list = manager.getByExternalNo(out_trade_no);
		for (WechatUnifiedPayCall unifiedCall : list) {
			if (unifiedCall.getResponseText() == null) {
				continue;
			}
			ResponseConfig responseConfg = new ResponseConfig(
					config.getMapFromXML(unifiedCall.getResponseText()),
					unifiedCall.getResponseText(),
					unifiedCall.getResponseCode(),
					unifiedCall.getResponseMsg(), unifiedCall.getRequestNo());
			if (!responseConfg.hasErrors()) {
				return responseConfg;
			}
		}
		SortedMap<String, String> params = new TreeMap<String, String>();
		params.put(Properties.PRE_APP_ID, config.getAppId());
		params.put(Properties.PRE_MCH_ID, config.getMchId());
		params.put(Properties.PRE_DEVICE_INFO, config.getDeviceInfo());
		params.put(Properties.PRE_NONCE_STR,
				StringUtils.remove(UUID.randomUUID().toString(), "-"));
		params.put(Properties.PRE_BODY, body);
		params.put(Properties.PRE_OUT_TRADE_NO, out_trade_no);
		params.put(Properties.PRE_TOTAL_FEE,
				total_fee.multiply(new BigDecimal("100")).toBigInteger()
						.toString());
		params.put(Properties.PRE_SPBILL_CREATE_IP, spbill_create_ip);
		params.put(Properties.PRE_TIME_START, DateTimeUtils.format(new Date(),
				DateTimeUtils.DEFAULT_DATE_TIME_FORMAT_PATTERN2));
		params.put(Properties.PRE_TIME_EXPIRE, DateTimeUtils.format(
				DateTimeUtils.addYears(new Date(), 1),
				DateTimeUtils.DEFAULT_DATE_TIME_FORMAT_PATTERN2));
		params.put(Properties.PRE_NOTIFY_URL,
				WebUtils.getRealmURL(request, URL.NOTIFY_URL));
		params.put(Properties.PRE_TRADE_TYPE, trade_type);
		params.put(Properties.PRE_OPEN_ID, openid);
		String sign = WxCryptUtil.createSign(params, config.getSignKey());
		params.put(Properties.PRE_SIGN, sign);
		WechatUnifiedPayCall unifiedPayCall = manager.add(userId,
				config.getAppId(), config.getMchId(),
				params.get(Properties.PRE_NONCE_STR), out_trade_no,
				JsonUtils.renderJson(params), URL.UNIFIED_ORDER_URL);
		ResponseConfig responseConfig = config.httpRequest(
				URL.UNIFIED_ORDER_URL, params.get(Properties.PRE_NONCE_STR),
				params);
		manager.update(unifiedPayCall.getId(),
				responseConfig.getResponseText(), responseConfig.getCode(),
				responseConfig.getMsg());
		return responseConfig;
	}
	
	@Override
	public ResponseConfig transferSubmit(Config config, Long userId,
			String partner_trade_no, String openid, BigDecimal amount,
			String desc, String spbill_create_ip) {
		SortedMap<String, String> params = new TreeMap<String, String>();
		params.put(Properties.PRE_MCH_APPID, config.getAppId());
		params.put(Properties.PRE_MCHID, config.getMchId());
		params.put(Properties.PRE_DEVICE_INFO, config.getDeviceInfo());
		params.put(Properties.PRE_NONCE_STR,
				StringUtils.remove(UUID.randomUUID().toString(), "-"));
		params.put(Properties.PRE_PARTNER_TRADE_NO, partner_trade_no);
		params.put(Properties.PRE_OPEN_ID, openid);
		params.put(Properties.PRE_CHECK_NAME, "NO_CHECK");
		params.put(Properties.PRE_AMOUNT, amount.toString());
		params.put(Properties.PRE_DESC, desc);
		params.put(Properties.PRE_SPBILL_CREATE_IP, spbill_create_ip);
		String sign = WxCryptUtil.createSign(params, config.getSignKey());
		params.put(Properties.PRE_SIGN, sign);
		WechatUnifiedPayCall unifiedPayCall = manager.add(userId,
				config.getAppId(), config.getMchId(),
				params.get(Properties.PRE_NONCE_STR), partner_trade_no,
				JsonUtils.renderJson(params), URL.UNIFIED_ORDER_URL);
		ResponseConfig responseConfig = config.httpRequest(URL.TRANSFERS_URL,
				params.get(Properties.PRE_NONCE_STR), params);
		manager.update(unifiedPayCall.getId(),
				responseConfig.getResponseText(), responseConfig.getCode(),
				responseConfig.getMsg());
		return responseConfig;
	}
	
	@Override
	public ResponseConfig queryOrder(Config config, Long userId,
			String transaction_id, String out_trade_no) {
		SortedMap<String, String> params = new TreeMap<String, String>();
		params.put(Properties.PRE_APP_ID, config.getAppId());
		params.put(Properties.PRE_MCH_ID, config.getMchId());
		if (transaction_id != null) {
			params.put(Properties.PRE_TRANSACTION_ID, transaction_id);
		}
		if (out_trade_no != null) {
			params.put(Properties.PRE_OUT_TRADE_NO, out_trade_no);
		}
		params.put(Properties.PRE_NONCE_STR,
				StringUtils.remove(UUID.randomUUID().toString(), "-"));
		String sign = WxCryptUtil.createSign(params, config.getSignKey());
		params.put(Properties.PRE_SIGN, sign);
		WechatUnifiedPayCall unifiedPayCall = manager.add(userId,
				config.getAppId(), config.getSignKey(),
				params.get(Properties.PRE_NONCE_STR), out_trade_no,
				JsonUtils.renderJson(params), URL.UNIFIED_ORDER_QUERY);
		ResponseConfig responseConfig = config.httpRequest(
				URL.UNIFIED_ORDER_QUERY, params.get(Properties.PRE_NONCE_STR),
				params);
		manager.update(unifiedPayCall.getId(),
				responseConfig.getResponseText(), responseConfig.getCode(),
				responseConfig.getMsg());
		return responseConfig;
	}


	@Override
	public ResponseConfig getTransferinfo(Config config, Long userId,
			String partner_trade_no) {
		SortedMap<String, String> params = new TreeMap<String, String>();
		params.put(Properties.PRE_APP_ID, config.getAppId());
		params.put(Properties.PRE_MCH_ID, config.getMchId());
		params.put(Properties.PRE_PARTNER_TRADE_NO, partner_trade_no);
		params.put(Properties.PRE_NONCE_STR,
				StringUtils.remove(UUID.randomUUID().toString(), "-"));
		String sign = WxCryptUtil.createSign(params, config.getSignKey());
		params.put(Properties.PRE_SIGN, sign);
		WechatUnifiedPayCall unifiedPayCall = manager.add(userId,
				config.getAppId(), config.getSignKey(),
				params.get(Properties.PRE_NONCE_STR), partner_trade_no,
				JsonUtils.renderJson(params), URL.GET_TRANSFER_INFO_URL);
		ResponseConfig responseConfig = config.httpRequest(
				URL.GET_TRANSFER_INFO_URL, params.get(Properties.PRE_NONCE_STR),
				params);
		manager.update(unifiedPayCall.getId(),
				responseConfig.getResponseText(), responseConfig.getCode(),
				responseConfig.getMsg());
		return responseConfig;
	}

	@Override
	public WechatUnifiedPayCall doNotify(ResponseConfig responseConfg) {
		WechatUnifiedPayCall unifiedCall = manager.getByRequestNo(responseConfg
				.getRequestNo());
		for (WechatUnifiedCallEventListener listener : caches) {
			if (URL.UNIFIED_ORDER_URL.equals(unifiedCall.getApi())) {
				listener.doPaymentNotify(unifiedCall);
			}
		}
		manager.addNotify(unifiedCall.getUserId(),
				responseConfg.getRequestNo(), unifiedCall.getApi(),
				responseConfg.getResponseText(), responseConfg.getCode(),
				responseConfg.getMsg());
		return unifiedCall;
	}
	

	@Override
	public void addEvent(WechatUnifiedCallEventListener listener) {
		caches.add(listener);
	}
	
	private List<WechatUnifiedCallEventListener> caches = new ArrayList<WechatUnifiedCallEventListener>();
	
	@Autowired
	private WechatUnifiedPayCallMng manager;
}
