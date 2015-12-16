package com.wechat.service;

import java.math.BigDecimal;

import javax.servlet.http.HttpServletRequest;

import com.wechat.service.Config.ResponseConfig;


public interface WechatUnifiedPayCallSvc {

	/**
	 * 微信支付接口
	 * 
	 * @param request
	 * @param config
	 * @param userId
	 *            当前用户编号
	 * @param out_trade_no
	 *            支付订单编号
	 * @param body
	 *            支付描述
	 * @param total_fee
	 *            支付金额 元
	 * @param spbill_create_ip
	 *            支付这IP
	 * @param trade_type
	 *            支付交易类型
	 * @param openid
	 *            支付者OPENID
	 * @return
	 */
	public ResponseConfig paymentSubmit(HttpServletRequest request, Config config,
			Long userId, String out_trade_no, String body,BigDecimal total_fee, String spbill_create_ip, 
			String trade_type,  String openid);
	
	/**
	 * 查询一笔交易
	 * @param request
	 * @param config
	 * @param userId  用户编号
	 * @param transaction_id  微信交易号
	 * @param out_trade_no  系统订单号
	 * @return
	 */
	public ResponseConfig queryOrder(HttpServletRequest request,Config config,Long userId,String transaction_id,String out_trade_no);
}
