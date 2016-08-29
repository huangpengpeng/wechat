package com.wechat.dao.impl;

import java.util.List;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Repository;

import com.common.aop_msg.New;
import com.common.jdbc.JdbcTemplateBaseDao;
import com.common.jdbc.SqlBuilder;
import com.wechat.dao.WechatUserDao;
import com.wechat.entity.WechatUser;

@Repository
public class WechatUserDaoImpl extends JdbcTemplateBaseDao implements WechatUserDao{

	@Caching(evict = { @CacheEvict(value = "com.wechat.entity.WechatUser_!ID", key = "#wechatUser.openId"),
			@CacheEvict(value = "com.wechat.entity.WechatUser_ID", key = "#result") })
	@Override
	public long add(WechatUser wechatUser) {
		return super.add(wechatUser);
	}

	@Override
	public WechatUser getByOpenId(Long partnerId, String openId) {
		SqlBuilder sqlBuilder = new SqlBuilder("select * from WechatUser where 1=1");
		if (sqlBuilder.ifNotNull(partnerId)) {
			sqlBuilder.andEqualTo("partnerId", partnerId);
		}
		if (sqlBuilder.ifNotNull(openId)) {
			sqlBuilder.andEqualTo("openId", openId);
		}
		return queryForObject(sqlBuilder);
	}
	
	
	@Override
	public WechatUser get(Long id) {
		return super.queryForObject(id);
	}

	@Override
	protected Class<?> getEntityClass() {
		return WechatUser.class;
	}

	public WechatUser update(Long id, Long externalNo) {
		SqlBuilder sqlBuilder = new SqlBuilder(
				"update WechatUser set gmtModify=current_timestamp()");
		if (sqlBuilder.ifNotNull(externalNo)) {
			sqlBuilder.set("externalNo", externalNo);
		}
		super.update(id, sqlBuilder);
		return New.get(WechatUserDao.class).get(id);
	}

	@Caching(evict = { @CacheEvict(value = "com.wechat.entity.WechatUser_ID", key = "#id"),
			@CacheEvict(value = "com.wechat.entity.WechatUser_!ID", key = "#result.openId") })
	@Override
	public WechatUser removeBinding(Long id) {
		SqlBuilder sqlBuilder = new SqlBuilder("update WechatUser set gmtModify=current_timestamp()");
		sqlBuilder.set("externalNo", null);
		super.update(id, sqlBuilder);
		return New.get(WechatUserDao.class).get(id);
	}

	@Override
	public List<WechatUser> getByExternalNo(Long externalNo) {
		SqlBuilder sqlBuilder=new SqlBuilder("select * from WechatUser where 1=1");
		if(sqlBuilder.ifNotNull(externalNo)){
			sqlBuilder.andEqualTo("externalNo", externalNo);
		}
		return query(sqlBuilder);
	}

	@Override
	public void delete(Long id) {
		super.delete(id);
	}
}
