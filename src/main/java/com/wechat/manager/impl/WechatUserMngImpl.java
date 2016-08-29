package com.wechat.manager.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import com.common.aop_msg.Message;
import com.wechat.dao.WechatUserDao;
import com.wechat.entity.WechatUser;
import com.wechat.manager.WechatUserMng;

@Transactional(isolation = Isolation.REPEATABLE_READ)
@Service
public class WechatUserMngImpl implements WechatUserMng {

	@Override
	public WechatUser add(Long partnerId, Long externalNo, String openId) {
		WechatUser wechatUser = new WechatUser(partnerId, externalNo, openId);
		wechatUser.init();
		wechatUser.setId(dao.add(wechatUser));
		return wechatUser;
	}

	@Caching(cacheable = { @Cacheable(value = "com.wechat.entity.WechatUser_!ID", key = "#openId") })
	@Override
	public WechatUser getByOpenId(Long partnerId, String openId) {
		return dao.getByOpenId(partnerId, openId);
	}

	@Caching(evict = { @CacheEvict(value = "com.wechat.entity.WechatUser_ID", key = "#id"),
			@CacheEvict(value = "com.wechat.entity.WechatUser_!ID", key = "#result.openId",condition="#result.openId != null") })
	@Override
	public WechatUser update(Long id, Long externalNo) {
		WechatUser wechatUser=dao.get(id);
		dao.update(id, externalNo);
		return wechatUser;
	}

	@Caching(evict = { @CacheEvict(value = "com.wechat.entity.WechatUser_ID", key = "#id"),
			@CacheEvict(value = "com.wechat.entity.WechatUser_!ID", key = "#result.openId", condition = "#result.openId != null") })
	@Override
	public WechatUser removeBinding(Long id) {
		WechatUser wechatUser = dao.get(id);
		dao.removeBinding(id);
		return wechatUser;
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

	@Caching(evict = { @CacheEvict(value = "com.wechat.entity.WechatUser_ID", key = "#id"),
			@CacheEvict(value = "com.wechat.entity.WechatUser_!ID", key = "#result.openId") })
	@Override
	public WechatUser delete(Long id) {
		WechatUser wechatUser = dao.get(id);
		dao.delete(id);
		return wechatUser;
	}
	
	@Autowired
	private WechatUserDao dao;
}
