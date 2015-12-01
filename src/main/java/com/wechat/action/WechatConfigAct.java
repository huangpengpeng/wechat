package com.wechat.action;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import me.chanjar.weixin.common.api.WxConsts;
import me.chanjar.weixin.common.bean.WxJsapiSignature;
import me.chanjar.weixin.common.exception.WxErrorException;
import me.chanjar.weixin.common.util.StringUtils;
import me.chanjar.weixin.common.util.http.URIUtil;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.result.WxMpOAuth2AccessToken;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.common.web.ResponseUtils;
import com.common.web.WebErrors;
import com.common.web.session.SessionProvider;
import com.wechat.entity.Partner;
import com.wechat.manager.PartnerMng;
import com.wechat.plugins.WechatConfigSvc;

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
				WxJsapiSignature signature = null;
				signature = wxMpService.createJsapiSignature(url);
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

	@RequestMapping(value = "/wechat_config/oauth2_{pId}.html")
	public String oauthSubmit(HttpServletRequest request, ModelMap model,
			@PathVariable("pId") Long pId, String url, String event) {
		WebErrors errors = WebErrors.create(request);
		if (errors.hasErrors()) {
			return errors.showErrorPage(model,
					"redirect:/common/error_message.html");
		}
		log.info("oauth url:{}", url);
		if (StringUtils.isBlank(url)) {
			url = "/";
		}
		Partner partner = partnerMng.get(pId);
		WxMpService wxMpService = wechatConfigSvc.createWxMpService(
				partner.getAppId(), partner.getSecretKey(), partner.getToken());
		return "redirect:"
				+ wxMpService.oauth2buildAuthorizationUrl(
						"http://" + request.getServerName()
								+ "/common/wechat_config/redirectURI.html?pId="
								+ pId, WxConsts.OAUTH2_SCOPE_BASE,
						URIUtil.encodeURIComponent(url));
	}

	@RequestMapping(value = "/wechat_config/redirectURI.html")
	public String redirectURI(HttpServletRequest request, ModelMap model,
			Long pId, String code, String state) {
		WebErrors errors = WebErrors.create(request);
		if (errors.hasErrors()) {
			return errors.showErrorPage(model,
					"redirect:/common/error_message.html");
		}
		StringBuffer redirectURI = new StringBuffer("redirect:");
		redirectURI.append(state);
		Partner partner = partnerMng.get(pId);
		WxMpService wxMpService = wechatConfigSvc.createWxMpService(
				partner.getAppId(), partner.getSecretKey(), partner.getToken());
		WxMpOAuth2AccessToken wxMpOAuth2AccessToken = null;
		try {
			if (session.getAttribute(request, WECHAT_OPEN_ID) == null) {
				wxMpOAuth2AccessToken = wxMpService.oauth2getAccessToken(code);
			}
		} catch (WxErrorException e) {
			log.error("获取微信 TOKEN 失败  code :" + code, e);
			addErrorCount(request);
		}
		if (wxMpOAuth2AccessToken != null) {
			session.setAttribute(request, null, WECHAT_OPEN_ID,
					wxMpOAuth2AccessToken.getOpenId());
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
