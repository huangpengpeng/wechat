package com.wechat.action;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.common.util.CryptoDesUtils;
import com.common.util.JsonUtils;
import com.common.util.ParamMap;
import com.common.util.ParamentersUtils;
import com.common.web.CookieUtils;
import com.common.web.ResponseUtils;
import com.common.web.WebErrors;
import com.common.web.session.SessionProvider;
import com.common.web.util.WebUtils;
import com.wechat.entity.Partner;
import com.wechat.manager.PartnerMng;
import com.wechat.plugins.WechatConfigSvc;

import me.chanjar.weixin.common.api.WxConsts;
import me.chanjar.weixin.common.bean.WxJsapiSignature;
import me.chanjar.weixin.common.exception.WxErrorException;
import me.chanjar.weixin.common.util.StringUtils;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.result.WxMpOAuth2AccessToken;

@Controller
public class WechatConfigAct {

	private Logger log = LoggerFactory.getLogger(WechatConfigAct.class);
	public final static String WECHAT_OPEN_ID = "wechat_open_id";
	public final static String WECHAT_ERROR_COUNT = "wechat_error_count";

	@RequestMapping(value = "/wechat_config/receiver_{token}.html")
	public void receiver(HttpServletRequest request,
			HttpServletResponse response, ModelMap modelMap,
			@PathVariable("token") String token, String echostr,
			String signature, String nonce, String timestamp)
			throws IOException {
		if (org.apache.commons.lang.StringUtils.isBlank(token)) {
			ResponseUtils.renderText(response, "非法请求");
			return;
		}
		Partner partner = partnerMng.getByToken(token);
		if (partner == null) {
			ResponseUtils.renderText(response, "非法请求");
			return;
		}
		if (!wechatConfigSvc.createWxMpService(partner.getAppId(),
				partner.getSecretKey(), partner.getToken()).checkSignature(
				timestamp, nonce, signature)) {// 消息签名不正确，说明不是公众平台发过来的消息
			ResponseUtils.renderText(response, "非法请求");
			return;
		}
		if (echostr != null && echostr.length() > 0) {// 说明是一个仅仅用来验证的请求，回显echostr
			ResponseUtils.renderText(response, echostr);
			return;
		}
		WxMpXmlMessage wxMpXmlMessage = WxMpXmlMessage.fromXml(request
				.getInputStream());
		wxMpXmlMessage.setToUserName(token);
		wechatConfigSvc.route(request, response, wxMpXmlMessage);
	}

	/**
	 * 页面调用例子： <script
	 * src="http://static.yoro.com/javascripts/wechat-1.0.js"></script> <script
	 * type="text/javascript"> var shareConfig = { title: '${shop.title}', desc:
	 * '淘遍全球美妆，在这里您将看到全球精品美妆爆款，所有精品均有品牌方直接授权，快来体验吧。', link: location.href,
	 * imgUrl: 'http://static.yoro.com/118175f0bd4f417eb004f7786bc1a83e.jpg',
	 * pId:"1" }; </script>
	 * 
	 * @param pId
	 *            合作伙伴编号
	 */
	@RequestMapping(value = "/wechat_config/config.html")
	public void config(HttpServletRequest request, ModelMap model, String url,
			Long pId) {
		WebErrors errors = vldExist(request, pId);
		if (!errors.hasErrors()) {
			try {
				Partner partner = partnerMng.get(pId);
				WxMpService wxMpService = wechatConfigSvc.createWxMpService(
						partner.getAppId(), partner.getSecretKey(),
						partner.getToken());
				wxMpService.getJsapiTicket();
				WxJsapiSignature signature = wxMpService
						.createJsapiSignature(url);
				model.addAttribute("url", url);
				model.addAttribute("nonceStr", signature.getNoncestr());
				model.addAttribute("timestamp", signature.getTimestamp());
				model.addAttribute("signature", signature.getSignature());
				model.addAttribute("appId", signature.getAppid());
			} catch (WxErrorException e) {
				errors.addErrorString(e.getMessage());
				log.error("wechat config :" + e.getMessage(), e);
			}
		}
		if (errors.hasErrors()) {
			errors.toModel(model);
		}
	}

	@RequestMapping(value = "/wechat_config/oauth2-{pId}-{scope}-{SESSION_NAME}.html")
	public void oauthSubmit(HttpServletRequest request,
			HttpServletResponse response, ModelMap model,
			@PathVariable("pId") Long pId, String URL,
			@PathVariable("scope") String scope,
			@PathVariable("SESSION_NAME") String SESSION_NAME) throws Exception {
		URL = WebUtils.getRequestURQ(request);
		log.info("oauth url:{}", URL);
		if (StringUtils.isBlank(URL)) {
			URL = "/";
		}
		Partner partner = partnerMng.get(pId);
		WxMpService wxMpService = wechatConfigSvc.createWxMpService(
				partner.getAppId(), partner.getSecretKey(), partner.getToken());
		ParamMap<String, Object> params = new ParamMap<String, Object>()
				.add("_URL", URL).add("_SESSION_NAME", SESSION_NAME)
				.add("_PID", pId);
		response.sendRedirect(wxMpService.oauth2buildAuthorizationUrl(
				"http://"
						+ partner.getRealm()
						+ "/login_wechat"
						+ CryptoDesUtils.encrypt(
								JsonUtils.renderJson(params.getPraamMap()),
								CryptoDesUtils.PASSWORD_CRYPT_KEY) + ".html",
				scope == null ? WxConsts.OAUTH2_SCOPE_BASE : scope,
				CryptoDesUtils.encrypt(JsonUtils
						.renderJson(new ParamMap<String, String>().add(
								"serviceName", WebUtils.getRealm(request))
								.getPraamMap()),
						CryptoDesUtils.PASSWORD_CRYPT_KEY)));
	}

	@RequestMapping(value = { "/login_wechat{params}.html" })
	public String redirectURI(HttpServletRequest request, ModelMap model, String code,
			@PathVariable("params") String params, String state) throws Exception {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		if (params != null && params.length() > 0) {
			paramMap = JsonUtils.toMap(CryptoDesUtils.decrypt(params, CryptoDesUtils.PASSWORD_CRYPT_KEY));
		}
		Long pId = ((Integer) paramMap.getOrDefault("_PID", 1)).longValue();
		String URI = (String) paramMap.getOrDefault("_URL", "/");
		String _SESSION_NAME = (String) paramMap.getOrDefault("_SESSION_NAME", WECHAT_OPEN_ID);
		WebErrors errors = WebErrors.create(request);
		errors.ifBlank(code, "CODE", 500);
		WxMpOAuth2AccessToken wxMpOAuth2AccessToken = (WxMpOAuth2AccessToken) session.getAttribute(request,
				_SESSION_NAME == null ? WECHAT_OPEN_ID : _SESSION_NAME);
		if ((wxMpOAuth2AccessToken != null || errors.hasErrors())
				&& ParamentersUtils.getQueryParams(URI, "refresh") == null) {
			return "redirect:" + (StringUtils.isBlank(URI) ? "/member/index.html" : URI);
		}
		StringBuffer redirectURI = new StringBuffer("redirect:");
		redirectURI.append(URI);
		Partner partner = partnerMng.get(pId);
		WxMpService wxMpService = wechatConfigSvc.createWxMpService(partner.getAppId(), partner.getSecretKey(),
				partner.getToken());
		try {
			log.info("根据CODE{} 获取 oauth2getAccessToken refererURL {}", code, WebUtils.getRefererURL());
			wxMpOAuth2AccessToken = wxMpService.oauth2getAccessToken(code);
		} catch (WxErrorException e) {
			addErrorCount(request);
		}
		if (wxMpOAuth2AccessToken != null) {
			session.setAttribute(request, null, _SESSION_NAME == null ? WECHAT_OPEN_ID : _SESSION_NAME,
					wxMpOAuth2AccessToken);
		}
		log.info("redirectURI url:{}", redirectURI.toString());
		return redirectURI.toString();
	}

	@RequestMapping(value = { "/wechat_config/redirectURI.html" })
	public String redirectURI(HttpServletRequest request, ModelMap model,
			String code, Long pId, String state) {
		WebErrors errors = WebErrors.create(request);
		errors.ifBlank(code, "CODE", 500);
		WxMpOAuth2AccessToken wxMpOAuth2AccessToken = (WxMpOAuth2AccessToken) session
				.getAttribute(request, WECHAT_OPEN_ID);
		if ((wxMpOAuth2AccessToken != null || errors.hasErrors())) {
			return "redirect:"
					+ (StringUtils.isBlank(state) ? "/member/index.html"
							: state);
		}
		StringBuffer redirectURI = new StringBuffer("redirect:");
		redirectURI.append(state);
		Partner partner = partnerMng.get(pId);
		WxMpService wxMpService = wechatConfigSvc.createWxMpService(
				partner.getAppId(), partner.getSecretKey(), partner.getToken());
		try {
			wxMpOAuth2AccessToken = wxMpService.oauth2getAccessToken(code);
		} catch (WxErrorException e) {
			log.error("redirectURI 获取微信 TOKEN 失败  code :" + code, e);
			addErrorCount(request);
		}
		if (wxMpOAuth2AccessToken != null) {
			session.setAttribute(request, null, WECHAT_OPEN_ID,
					wxMpOAuth2AccessToken);
		}
		log.info("redirectURI url:{}", redirectURI.toString());
		return redirectURI.toString();
	}

	protected Integer addErrorCount(HttpServletRequest request) {
		Integer errorCount = (Integer) session.getAttribute(request,
				WECHAT_ERROR_COUNT);
		if (errorCount == null) {
			errorCount = 1;
		} else {
			errorCount++;
		}
		session.setAttribute(request, null, WECHAT_ERROR_COUNT, errorCount);
		return errorCount;
	}

	protected WebErrors vldExist(HttpServletRequest request, Long pId) {
		WebErrors errors = WebErrors.create(request);
		if (errors.ifNull(pId, "合作伙伴编号")) {
			return errors;
		}
		if (partnerMng.get(pId) == null) {
			errors.addErrorString("合作伙伴不存在");
		}
		return errors;
	}

	@Autowired
	private SessionProvider session;
	@Autowired
	private PartnerMng partnerMng;
	@Autowired
	private WechatConfigSvc wechatConfigSvc;
}
