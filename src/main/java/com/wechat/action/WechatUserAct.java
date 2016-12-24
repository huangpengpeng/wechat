package com.wechat.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;

import com.common.web.WebErrors;
import com.wechat.entity.WechatUser;
import com.wechat.manager.PartnerMng;
import com.wechat.manager.WechatUserMng;

@Controller
public class WechatUserAct {


	@RequestMapping(value = "/wechat_user/logout.html")
	public void logout(HttpServletRequest request, HttpSession session, ModelMap model, Long pId,
			@CookieValue(value = "OPEN_ID", required = false) String openId) {
		WebErrors errors = validate(request, pId,openId);
		if (!errors.hasErrors()) {
			if (openId != null) {
				WechatUser wechatUser = manager.getByOpenId(pId, openId);
				manager.removeBinding(wechatUser.getId());
			}
		}
		if (errors.hasErrors()) {
			errors.toModel(model);
		}
	}

	private WebErrors validate(HttpServletRequest request, Long pId,String openId) {
		WebErrors errors = WebErrors.create(request);
		errors.ifNull(openId, "微信授权");
		errors.ifNull(pId, "合作伙伴编号");
		if (errors.hasErrors()) {
			return errors;
		}
		if (partnerMng.get(pId) == null) {
			errors.addErrorString("合作伙伴编号不存在");
			return errors;
		}
		if (manager.getByOpenId(pId, openId) == null) {
			errors.addErrorString("当前授权编号不存在");
		}
		return errors;
	}

	@Autowired
	private PartnerMng partnerMng;
	@Autowired
	private WechatUserMng manager;
}
