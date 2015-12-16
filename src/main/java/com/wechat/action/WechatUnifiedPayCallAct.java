package com.wechat.action;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;

import com.wechat.service.Config;
import com.wechat.service.Config.ResponseConfig;

@Controller
public class WechatUnifiedPayCallAct {

//	protected ResponseConfig validateCall(HttpServletRequest request, String data) {
//		try {
//			Config config = new Config();
//			String resValue = AESUtil.decrypt(data, config.getCustomerSecret()
//					.substring(0, 16));
//			Map<String, Object> res = config.toMap(resValue);
//			String hmac = (String) res.get("hmac");
//			String code = String.valueOf(res.get("code"));
//			String msg = (String) res.get("msg");
//			String requestNo = (String) res.get(Properties.REQUEST_ID);
//			if (StringUtils.isBlank(requestNo)) {
//				requestNo = (String) res.get(Properties.CASH_REQUESTID);
//			}
//			return new ResponseConfg(res, data, resValue, hmac, code, msg,
//					requestNo);
//		} catch (Exception e) {
//			return new ResponseConfg(null, null, null, null, "-1",
//					e.getMessage(), null);
//		}
//	}
}
