package com.wechat.action;

import static com.wechat.action.WechatConfigAct.WECHAT_OPEN_ID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import me.chanjar.weixin.common.util.StringUtils;
import me.chanjar.weixin.mp.bean.result.WxMpOAuth2AccessToken;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.common.web.WebErrors;
import com.wechat.entity.WechatUser;
import com.wechat.manager.PartnerMng;
import com.wechat.manager.WechatUserMng;

@Controller
public class WechatUserAct {
	
	private Logger log=LoggerFactory.getLogger(WechatUserAct.class);

	@RequestMapping(value = "/wechat_user/logout.html")
	public void logout(HttpServletRequest request, ModelMap model, Long pId) {
		WebErrors errors = validate(request, pId);
		if (!errors.hasErrors()) {
			HttpSession session = request.getSession();
			WxMpOAuth2AccessToken auth2AccessToken = (WxMpOAuth2AccessToken) session
					.getAttribute(WECHAT_OPEN_ID);
			WechatUser wechatUser = manager.getByOpenId(pId,
					auth2AccessToken.getOpenId());
			manager.removeBinding(wechatUser.getId());
			session.invalidate();
		}
		if (errors.hasErrors()) {
			errors.toModel(model);
		}
	}
	
	@RequestMapping(value = "/wechat_user/remove.html")
	public void remove(HttpServletRequest request, ModelMap model, Long pId,
			String openId) {
		WebErrors errors = validateRemove(request, pId, openId);
		if (!errors.hasErrors()) {
			WechatUser user = manager.getByOpenId(pId, openId);
			manager.delete(user.getId());
		}
		if (errors.hasErrors()) {
			log.error(" 解除OPENID:[" + openId + "]");
			errors.toModel(model);
		}
	}
	
	
	private WebErrors validateRemove(HttpServletRequest request, Long pId,
			String openId) {
		WebErrors errors = WebErrors.create(request);
		HttpSession session = request.getSession();
		WxMpOAuth2AccessToken auth2AccessToken = (WxMpOAuth2AccessToken) session
				.getAttribute(WECHAT_OPEN_ID);
		if (StringUtils.isBlank(openId) && auth2AccessToken != null) {
			openId = auth2AccessToken.getOpenId();
		}
		errors.ifBlank(openId, "授权编号", 1000);
		errors.ifNull(pId, "合作伙伴编号");
		if (errors.hasErrors()) {
			return errors;
		}
		if (partnerMng.get(pId) == null) {
			errors.addErrorString("合作伙伴编号不存在");
			return errors;
		}
		WechatUser user=manager.getByOpenId(pId, openId) ;
		if (user == null) {
			errors.addErrorString("微信授权不存在");
			return errors;
		}
		if(user.getExternalNo()!=null){
			errors.addErrorString("您没有权限删除授权");
		}
		return errors;
	}
	
	private WebErrors validate(HttpServletRequest request, Long pId) {
		HttpSession session=request.getSession();
		WebErrors errors = WebErrors.create(request);
		WxMpOAuth2AccessToken auth2AccessToken=  (WxMpOAuth2AccessToken) session.getAttribute( WECHAT_OPEN_ID);
		errors.ifNull(auth2AccessToken, "微信授权");
		errors.ifNull(pId, "合作伙伴编号");
		if (errors.hasErrors()) {
			return errors;
		}
		if (partnerMng.get(pId) == null) {
			errors.addErrorString("合作伙伴编号不存在");
			return errors;
		}
		if (manager.getByOpenId(pId, auth2AccessToken.getOpenId()) == null) {
			errors.addErrorString("当前授权编号不存在");
		}
		return errors;
	}
	
	@Autowired
	private PartnerMng partnerMng;
	@Autowired
	private WechatUserMng manager;
}
