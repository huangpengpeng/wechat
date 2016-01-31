package com.wechat.service;

import java.io.IOException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.xml.parsers.ParserConfigurationException;

import me.chanjar.weixin.common.util.StringUtils;
import me.chanjar.weixin.common.util.crypto.WxCryptUtil;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.ClassUtils;
import org.xml.sax.SAXException;

import com.common.util.CollectionUtils;
import com.tencent.common.Configure;

public class Config {

	private static Logger log = LoggerFactory.getLogger(Config.class);

	public final static Long DEFAULT_PARTENR = 1L;

	public Config(String appId, String secretKey, String mchId,
			String deviceInfo, String signKey) {
		try {
			this.appId = appId;
			this.secretKey = secretKey;
			this.mchId = mchId;
			this.deviceInfo = deviceInfo;
			this.signKey = signKey;
			if (StringUtils.isBlank(Configure.getCertLocalPath())) {
				init();
			}
		} catch (IOException e) {
			throw new IllegalStateException(e);
		}
	}

	public void init() throws IOException {
		java.net.URL url = ClassUtils.getDefaultClassLoader().getResource(
				"apiclient_cert.p12");
		String file = url.getFile();
		Configure.setCertLocalPath(URLDecoder.decode(file, "utf-8"));
		Configure.setCertPassword(this.getMchId());
	}

	public ResponseConfig httpRequest(String url, String requestNo,
			Map<String, String> parameters) {
		try {
			HttpsRequest httpsRequest = new HttpsRequest();
			final SortedMap<String, String> params = new TreeMap<String, String>(
					parameters);
			StringBuilder postDataXML = new StringBuilder("<xml>");
			for (Entry<String, String> para : params.entrySet()) {
				postDataXML.append(String.format("<%s>%s</%s>", para.getKey(),
						para.getValue(), para.getKey()));
			}
			postDataXML.append("</xml>");
			String responseContent = httpsRequest.send(url,
					postDataXML.toString());
			Map<String, Object> res = getMapFromXML(responseContent);
			String return_code = (String) res.get("return_code");
			String return_msg = (String) res.get("return_msg");
			if (!"SUCCESS".equalsIgnoreCase(return_code)) {
				return new ResponseConfig(res, responseContent, "0",
						return_msg, requestNo);
			}
			String result_code = (String) res.get("result_code");
			String err_code = (String) res.get("err_code");
			String err_code_des = (String) res.get("err_code_des");
			if (!"SUCCESS".equalsIgnoreCase(result_code)) {
				return new ResponseConfig(res, responseContent, err_code,
						err_code_des, requestNo);
			}
			return new ResponseConfig(res, responseContent, null, null,
					requestNo);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return new ResponseConfig(null, null, "-1", e.getMessage(),
					requestNo);
		}
	}

	public Map<String, Object> getMapFromXML(String content) {
		// 这里用Dom的方式解析回包的最主要目的是防止API新增回包字段
		Document document = Jsoup.parseBodyFragment(content);
		// 获取到document里面的全部结点
		Element element = document.body().child(0);
		Elements allNodes = element.children();
		Map<String, Object> map = new HashMap<String, Object>();
		Element node;
		int i = 0;
		while (i < allNodes.size()) {
			node = allNodes.get(i);
			if (node instanceof Element) {
				map.put(node.tagName(), node.text());
			}
			i++;
		}
		return map;
	}

	/**
	 * 从API返回的XML数据里面重新计算一次签名
	 * 
	 * @param responseString
	 *            API返回的XML数据
	 * @return 新鲜出炉的签名
	 * @throws ParserConfigurationException
	 * @throws IOException
	 * @throws SAXException
	 */
	public String getResponseXMLForSign(String responseXML) throws IOException,
			SAXException, ParserConfigurationException {
		Map<String, Object> map = getMapFromXML(responseXML);
		// 清掉返回数据对象里面的Sign数据（不能把这个数据也加进去进行签名），然后用签名算法进行签名
		map.put("sign", "");
		Map<String, String> params = (Map<String, String>) CollectionUtils
				.toStringOfValueTreeMap(map);
		// 将API返回的数据根据用签名算法进行计算新的签名，用来跟API返回的签名进行比较
		return WxCryptUtil.createSign(params, this.getSignKey());
	}

	/**
	 * 商户编号
	 */
	private String appId;
	/**
	 * 商户秘钥
	 */
	private String secretKey;
	/**
	 * 商户密钥
	 */
	private String mchId;
	/**
	 * 实名认证生产环境商户编号
	 */
	private String deviceInfo;

	/**
	 * API密钥
	 */
	private String signKey;

	public static class ResponseConfig {

		public ResponseConfig(Map<String, Object> res, String responseText,
				String code, String msg, String requestNo) {
			this.res = res;
			this.responseText = responseText;
			this.code = code;
			this.msg = msg;
			this.requestNo = requestNo;
		}

		public boolean hasErrors() {
			return code != null;
		}

		public String getErrorCodeMsg() {
			String errorMng = errorCodeMsg.get(code);
			return errorMng == null ? getMsg() : errorMng;
		}

		/**
		 * 响应内容
		 */
		private String responseText;
		/**
		 * 响应码
		 */
		private String code;
		/**
		 * 响应消息
		 */
		private String msg;
		/**
		 * 请求流水号
		 */
		private String requestNo;

		/**
		 * 返回字段
		 */
		private Map<String, Object> res;

		public String getAttr(String name) {
			Object value = res.get(name);
			if (value == null) {
				return null;
			}
			return String.valueOf(value);
		}

		public String getResponseText() {
			return responseText;
		}

		public String getCode() {
			return code;
		}

		public String getMsg() {
			return msg;
		}

		public String getRequestNo() {
			return requestNo;
		}
	}

	public static class Properties {
		/**
		 * 微信分配的公众账号ID（企业号corpid即为此appId）
		 */
		public static String PRE_APP_ID = "appid";
		/**
		 * 微信分配的公众账号ID（企业号corpid即为此appId）
		 */
		public static String PRE_WXAPP_ID = "wxappid";
		/**
		 * 微信支付分配的商户号
		 */
		public static String PRE_MCH_ID = "mch_id";
		/**
		 * 终端设备号(门店号或收银设备ID)，注意：PC网页或公众号内支付请传"WEB"
		 */
		public static String PRE_DEVICE_INFO = "device_info";
		/**
		 * 随机字符串，不长于32位。推荐随机数生成算法
		 */
		public static String PRE_NONCE_STR = "nonce_str";
		/**
		 * 签名，详见签名生成算法
		 */
		public static String PRE_SIGN = "sign";
		/**
		 * 商品或支付单简要描述
		 */
		public static String PRE_BODY = "body";
		/**
		 * 商品或支付单简要描述
		 */
		public static String PRE_DETAIL = "detail";
		/**
		 * 附加数据，在查询API和支付通知中原样返回，该字段主要用于商户携带订单的自定义数据
		 */
		public static String PRE_ATTACH = "attach";
		/**
		 * 商户系统内部的订单号,32个字符内、可包含字母, 其他说明见商户订单号
		 */
		public static String PRE_OUT_TRADE_NO = "out_trade_no";
		/**
		 * 商户订单号
		 */
		public static String PRE_MCH_BILLNO = "mch_billno";
		/**
		 * 商户名称
		 */
		public static String PRE_SEND_NAME = "send_name";
		/**
		 * 符合ISO 4217标准的三位字母代码，默认人民币：CNY，其他值列表详见货币类型
		 */
		public static String PRE_FEE_TYPE = "fee_type";
		/**
		 * 订单总金额，单位为分，详见支付金额
		 */
		public static String PRE_TOTAL_FEE = "total_fee";
		/**
		 * 红包总金额，单位为分，详见支付金额
		 */
		public static String PRE_TOTAL_AMOUNT = "total_amount";
		/**
		 * 红包总发放人数
		 */
		public static String PRE_TOTAL_NUM = "total_num";
		/**
		 * 红包祝福语
		 */
		public static String PRE_WISHING = "wishing";
		/**
		 * 红包活动名称
		 */
		public static String PRE_ACT_NAME = "act_name";
		/**
		 * APP和网页支付提交用户端ip，Native支付填调用微信支付API的机器IP。
		 */
		public static String PRE_SPBILL_CREATE_IP = "spbill_create_ip";
		
		/**
		 * APP和网页支付提交用户端ip，Native支付填调用微信支付API的机器IP。
		 */
		public static String PRE_CREATE_IP = "client_ip";
		/**
		 * 订单生成时间，格式为yyyyMMddHHmmss，如2009年12月25日9点10分10秒表示为20091225091010。
		 * 其他详见时间规则
		 */
		public static String PRE_TIME_START = "time_start";
		/**
		 * 订单失效时间，格式为yyyyMMddHHmmss，如2009年12月27日9点10分10秒表示为20091227091010。
		 * 其他详见时间规则 注意：最短失效时间间隔必须大于5分钟
		 */
		public static String PRE_TIME_EXPIRE = "time_expire";
		/**
		 * 接收微信支付异步通知回调地址
		 */
		public static String PRE_GOODS_TAG = "goods_tag";
		/**
		 * 取值如下：JSAPI，NATIVE，APP，详细说明见参数规定
		 */
		public static String PRE_NOTIFY_URL = "notify_url";
		/**
		 * 取值如下：JSAPI，NATIVE，APP，详细说明见参数规定
		 */
		public static String PRE_TRADE_TYPE = "trade_type";
		/**
		 * trade_type=NATIVE，此参数必传。此id为二维码中包含的商品ID，商户自行定义。
		 */
		public static String PRE_PRODUCT_ID = "product_id";
		/**
		 * no_credit--指定不能使用信用卡支付
		 */
		public static String PRE_LIMIT_PAY = "limit_pay";
		/**
		 * trade_type=JSAPI，此参数必传，用户在商户appid下的唯一标识。openid如何获取，可参考【获取openid】。
		 * 企业号请使用【企业号OAuth2.0接口】获取企业号内成员userid，再调用【企业号userid转openid接口】进行转换
		 */
		public static String PRE_OPEN_ID = "openid";
		/**
		 * trade_type=JSAPI，此参数必传，用户在商户appid下的唯一标识。openid如何获取，可参考【获取openid】。
		 * 企业号请使用【企业号OAuth2.0接口】获取企业号内成员userid，再调用【企业号userid转openid接口】进行转换
		 */
		public static String PRE_REOPEN_ID = "re_openid";
		/**
		 * 微信订单号
		 */
		public static String PRE_TRANSACTION_ID = "transaction_id";
		/**
		 * 交易状态
		 */
		public static String PRE_TRADE_STATE = "trade_state";
		/**
		 * 付款银行
		 */
		public static String PRE_BANK_TYPE = "bank_type";
		/**
		 * 微信分配的公众账号ID（企业号corpid即为此appId）
		 */
		public static String PRE_MCH_APPID = "mch_appid";
		/**
		 * 商户订单号，需保持唯一性
		 */
		public static String PRE_PARTNER_TRADE_NO = "partner_trade_no";
		/**
		 * NO_CHECK：不校验真实姓名 FORCE_CHECK：强校验真实姓名（未实名认证的用户会校验失败，无法转账）
		 * OPTION_CHECK：针对已实名认证的用户才校验真实姓名（未实名认证用户不校验，可以转账成功）
		 */
		public static String PRE_CHECK_NAME = "check_name";
		/**
		 * 收款用户真实姓名。 如果check_name设置为FORCE_CHECK或OPTION_CHECK，则必填用户真实姓名
		 */
		public static String PRE_RE_USER_NAME = "re_user_name";
		/**
		 * 企业付款金额，单位为分
		 */
		public static String PRE_AMOUNT = "amount";
		/**
		 * 企业付款操作说明信息。必填。
		 */
		public static String PRE_DESC = "desc";
		/**
		 * 微信支付分配的商户号
		 */
		public static String PRE_MCHID = "mchid";
		/**
		 * 备注
		 */
		public static String PRE_REMARK = "remark";
	}

	/**
	 * 易宝调用所用到的地址
	 * 
	 * @author zoro
	 *
	 */
	public static class URL {
		public static String UNIFIED_ORDER_URL = "https://api.mch.weixin.qq.com/pay/unifiedorder";// 统一下单
		public static String UNIFIED_ORDER_QUERY = "https://api.mch.weixin.qq.com/pay/orderquery";// 查询订单
		public static String TRANSFERS_URL = "https://api.mch.weixin.qq.com/mmpaymkttransfers/promotion/transfers";// 查询订单
		public static String GET_TRANSFER_INFO_URL = "https://api.mch.weixin.qq.com/mmpaymkttransfers/gettransferinfo";// 查询订单
		public static String NOTIFY_URL = "/common/wechat_config/c.jhtml"; // 系统回调
		public static String SENDREDPACK_URL = "https://api.mch.weixin.qq.com/mmpaymkttransfers/sendredpack";//红包
	}

	public static Map<String, String> errorCodeMsg = new HashMap<String, String>() {
		private static final long serialVersionUID = 5106474019335603328L;

		{
		}
	};

	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	public String getSecretKey() {
		return secretKey;
	}

	public void setSecretKey(String secretKey) {
		this.secretKey = secretKey;
	}

	public String getMchId() {
		return mchId;
	}

	public void setMchId(String mchId) {
		this.mchId = mchId;
	}

	public String getDeviceInfo() {
		return deviceInfo;
	}

	public void setDeviceInfo(String deviceInfo) {
		this.deviceInfo = deviceInfo;
	}

	public static Map<String, String> getErrorCodeMsg() {
		return errorCodeMsg;
	}

	public static void setErrorCodeMsg(Map<String, String> errorCodeMsg) {
		Config.errorCodeMsg = errorCodeMsg;
	}

	public String getSignKey() {
		return signKey;
	}

	public void setSignKey(String signKey) {
		this.signKey = signKey;
	}
}
