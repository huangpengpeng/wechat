package com.wechat.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import static com.wechat.action.WechatConfigAct.WECHAT_OPEN_ID;

import com.common.web.WebErrors;
import com.common.web.session.SessionProvider;
import com.wechat.entity.WechatUser;
import com.wechat.manager.PartnerMng;
import com.wechat.manager.WechatUserMng;

@Controller
public class WechatUserAct {

	@RequestMapping(value = "/wechat_user/logout.html")
	public void logout(HttpServletRequest request, ModelMap model, Long pId) {
		WebErrors errors = validate(request, pId);
		if (!errors.hasErrors()) {
			HttpSession session = request.getSession();
			String openId = (String) session.getAttribute(WECHAT_OPEN_ID);
			WechatUser wechatUser = manager.getByOpenId(pId, openId);
			manager.removeBinding(wechatUser.getId());
			session.invalidate();
		}
		if (errors.hasErrors()) {
			errors.toModel(model);
		}
	}
	
	private WebErrors validate(HttpServletRequest request, Long pId) {
		HttpSession session=request.getSession();
		WebErrors errors = WebErrors.create(request);
		String openId = (String) session.getAttribute( WECHAT_OPEN_ID);
		errors.ifBlank(openId, "授权编号", 2000);
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
