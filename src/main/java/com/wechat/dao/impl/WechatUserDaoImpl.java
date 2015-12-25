package com.wechat.dao.impl;

import org.springframework.stereotype.Repository;

import com.common.jdbc.JdbcTemplateBaseDao;
import com.common.jdbc.SqlBuilder;
import com.wechat.dao.WechatUserDao;
import com.wechat.entity.WechatUser;

@Repository
public class WechatUserDaoImpl extends JdbcTemplateBaseDao implements WechatUserDao{

	public long add(WechatUser wechatUser) {
		return super.add(wechatUser);
	}

	public WechatUser getByOpenId(Long partnerId, String openId) {
		SqlBuilder sqlBuilder=new SqlBuilder("select * from WechatUser where 1=1");
		if(sqlBuilder.ifNotNull(partnerId)){
			sqlBuilder.andEqualTo("partnerId", partnerId);
		}
		if(sqlBuilder.ifNotNull(openId)){
			sqlBuilder.andEqualTo("openId", openId);
		}
		return queryForObject(sqlBuilder);
	}

	@Override
	protected Class<?> getEntityClass() {
		return WechatUser.class;
	}

	public void update(Long id, Long externalNo) {
		SqlBuilder sqlBuilder = new SqlBuilder(
				"update WechatUser set gmtModify=current_timestamp()");
		if (sqlBuilder.ifNotNull(externalNo)) {
			sqlBuilder.set("externalNo", externalNo);
		}
		super.update(id, sqlBuilder);
	}

	@Override
	public void removeBinding(Long id) {
		SqlBuilder sqlBuilder = new SqlBuilder(
				"update WechatUser set gmtModify=current_timestamp()");
		sqlBuilder.set("externalNo", null);
		super.update(id, sqlBuilder);
	}
}
