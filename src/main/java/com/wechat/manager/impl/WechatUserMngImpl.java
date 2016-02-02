package com.wechat.manager.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import com.common.message.Message;
import com.wechat.dao.WechatUserDao;
import com.wechat.entity.WechatUser;
import com.wechat.manager.WechatUserMng;

@Transactional(isolation = Isolation.REPEATABLE_READ)
@Service
public class WechatUserMngImpl implements WechatUserMng {

	public WechatUser add(Long partnerId, Long externalNo, String openId) {
		WechatUser wechatUser = new WechatUser(partnerId, externalNo, openId);
		wechatUser.init();
		wechatUser.setId(dao.add(wechatUser));
		return wechatUser;
	}

	public WechatUser getByOpenId(Long partnerId, String openId) {
		return dao.getByOpenId(partnerId, openId);
	}

	public void update(Long id, Long externalNo) {
		dao.update(id, externalNo);
	}

	@Override
	public void removeBinding(Long id) {
		dao.removeBinding(id);
	}

	@Override
	public List<WechatUser> getByExternalNo(Long externalNo) {
		return dao.getByExternalNo(externalNo);
	}

	@Message(name = { WechatUserMng.MSG_WECHAT_USER_REGISTER_SUBMIT })
	@Override
	public Map<String, Object> register(Long partnerId, Long externalNo,
			String openId, Long pId) {
		WechatUser wechatUser = new WechatUser(partnerId, externalNo, openId);
		wechatUser.init();
		wechatUser.setId(dao.add(wechatUser));
		return new HashMap<String, Object>() {
			private static final long serialVersionUID = 8391427169725093996L;
			{
				this.put("WECHAT_USER", wechatUser);
				this.put("pId", pId);
			}
		};
	}

	@Override
	public void delete(Long id) {
		dao.delete(id);
	}
	
	@Autowired
	private WechatUserDao dao;
}
