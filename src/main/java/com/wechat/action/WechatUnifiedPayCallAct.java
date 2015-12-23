package com.wechat.action;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.common.concurrent.lock.ObjectUtils;
import com.common.util.JsonUtils;
import com.common.web.ResponseUtils;
import com.common.web.WebErrors;
import com.wechat.entity.Partner;
import com.wechat.entity.WechatUnifiedPayCall;
import com.wechat.manager.PartnerMng;
import com.wechat.manager.WechatUnifiedPayCallMng;
import com.wechat.service.Config;
import com.wechat.service.Config.Properties;
import com.wechat.service.Config.ResponseConfig;
import com.wechat.service.WechatUnifiedPayCallSvc;

@Controller
public class WechatUnifiedPayCallAct {

	private Logger log = LoggerFactory.getLogger(WechatUnifiedPayCallAct.class);

	@RequestMapping(value = { "/wechat_config/c.jhtml" })
	public void wechatCallGo(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		ResponseConfig responseConfg = validateResponseConfig(request,
				IOUtils.toString(request.getInputStream()));
		log.info(" responseconfig: {}", JsonUtils.renderJson(responseConfg));
		if (responseConfg.hasErrors()) {
			log.error(responseConfg.getErrorCodeMsg());
			return;
		}
		try {
			WechatUnifiedPayCall unifiedCall = manager
					.getByRequestNo(responseConfg.getRequestNo());
			synchronized (ObjectUtils.getLock(WechatUnifiedPayCall.class,
					unifiedCall.getExternalNo())) {
				wechatUnifiedPayCallSvc.doNotify(responseConfg);
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			WechatUnifiedPayCall unifiedCall = manager
					.getByRequestNo(responseConfg.getRequestNo());
			manager.addNotify(unifiedCall.getUserId(),
					responseConfg.getRequestNo(), unifiedCall.getApi(),
					responseConfg.getResponseText(), "500", e.getMessage());
			return;
		} finally {
			ObjectUtils.removeLock(WechatUnifiedPayCall.class,
					responseConfg.getRequestNo());
		}
		ResponseUtils
				.renderText(
						response,
						"<xml><return_code><![CDATA[SUCCESS]]></return_code><return_msg><![CDATA[OK]]></return_msg></xml>");

	}

	@RequestMapping(value = { "/wechat_config/w_c{no}.jhtml" })
	public void webCallGo(HttpServletRequest request, ModelMap model,
			@PathVariable("no") String no) throws Exception {
		WebErrors errors = validateResponseConfig2(request, no);
		log.info(" w_c requestNo: {}", no);
		if (!errors.hasErrors()) {
			WechatUnifiedPayCall unifiedCall = null;
			try {
				unifiedCall = manager.getByRequestNo(no);
				synchronized (ObjectUtils.getLock(WechatUnifiedPayCall.class,
						unifiedCall.getExternalNo())) {
					unifiedCall = wechatUnifiedPayCallSvc
							.doNotify(new ResponseConfig(null, null, "1", null,
									no));
				}
			} catch (Exception e) {
				log.error(e.getMessage(), e);
				manager.addNotify(unifiedCall.getUserId(), no,
						unifiedCall.getApi(), "", "500", e.getMessage());
			} finally {
				ObjectUtils.removeLock(WechatUnifiedPayCall.class,
						unifiedCall.getExternalNo());
			}
		}
		if (errors.hasErrors()) {
			errors.toModel(model);
		}
	}

	protected WebErrors validateResponseConfig2(HttpServletRequest request,
			String requestNo) {
		WebErrors errors = WebErrors.create(request);
		errors.ifBlank(requestNo, "请求流水号", 255);
		if (errors.hasErrors()) {
			return errors;
		}
		if (manager.getByRequestNo(requestNo) == null) {
			errors.addErrorString("请求流水号错误");
		}
		return errors;
	}

	protected ResponseConfig validateResponseConfig(HttpServletRequest request,
			String responseXML) {
		try {
			Partner partner = partnerMng.get(Config.DEFAULT_PARTENR);
			Config config = new Config(partner.getAppId(),
					partner.getSecretKey(), partner.getMchId(),
					partner.getDeviceInfo(), partner.getSignKey());
			Map<String, Object> res = config.getMapFromXML(responseXML);
			String sign = (String) res.get(Properties.PRE_SIGN);
			if (!StringUtils.equals(sign,
					config.getResponseXMLForSign(responseXML))) {
				return new ResponseConfig(res, responseXML, "-1", "签名错误", null);
			}
			String return_code = (String) res.get("return_code");
			String return_msg = (String) res.get("return_msg");
			String requestNo = (String) res.get(Properties.PRE_NONCE_STR);
			if (!"SUCCESS".equalsIgnoreCase(return_code)) {
				return new ResponseConfig(res, responseXML, "0", return_msg,
						requestNo);
			}
			String result_code = (String) res.get("result_code");
			String err_code = (String) res.get("err_code");
			String err_code_des = (String) res.get("err_code_des");
			if (!"SUCCESS".equalsIgnoreCase(result_code)) {
				return new ResponseConfig(res, responseXML, err_code,
						err_code_des, requestNo);
			}
			return new ResponseConfig(res, responseXML, null, null, requestNo);
		} catch (Exception e) {
			return new ResponseConfig(null, responseXML, "-1", e.getMessage(),
					null);
		}
	}

	@Autowired
	private WechatUnifiedPayCallMng manager;
	@Autowired
	private WechatUnifiedPayCallSvc wechatUnifiedPayCallSvc;
	@Autowired
	private PartnerMng partnerMng;
}
