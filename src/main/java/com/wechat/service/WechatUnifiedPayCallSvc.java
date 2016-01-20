package com.wechat.service;

import java.math.BigDecimal;

import javax.servlet.http.HttpServletRequest;

import com.wechat.entity.WechatUnifiedPayCall;
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
	public ResponseConfig queryOrder(Config config,Long userId,String transaction_id,String out_trade_no);
	
	
	/**
	 * 
	 * @param config
	 * @param userId
	 * @param mch_appid 微信分配的公众账号ID（企业号corpid即为此appId）
	 * @param mchid  微信支付分配的商户号
	 * @param device_info 微信支付分配的终端设备号
	 * @param partner_trade_no 随机字符串，不长于32位
	 * @param openid 商户appid下，某用户的openid
	 * @param amount  企业付款金额，单位为分
	 * @param desc 企业付款操作说明信息。必填。
	 * @param spbill_create_ip  调用接口的机器Ip地址
	 * @return
	 */
	public ResponseConfig transferSubmit(Config config, Long userId,
			String partner_trade_no, String openid, BigDecimal amount,
			String desc,String check_name,String re_user_name, String spbill_create_ip);
	
	/**
	 * 
	 * @param config
	 * @param userId
	 * @param partner_trade_no  商户订单号
	 * @return
	 */
	public ResponseConfig getTransferinfo(Config config,Long userId,String partner_trade_no);
	
	/**
	 * 统一通知
	 * 
	 * @param responseConfg
	 */
	public WechatUnifiedPayCall doNotify(ResponseConfig responseConfg);
	
	/**
	 * 
	 * @author zoro
	 *
	 */
	public static interface WechatUnifiedCallEventListener {
		void doPaymentNotify(WechatUnifiedPayCall unifiedCall);
	}

	public void addEvent(WechatUnifiedCallEventListener listener);
}
