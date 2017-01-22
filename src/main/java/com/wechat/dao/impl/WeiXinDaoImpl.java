package com.wechat.dao.impl;

import org.springframework.stereotype.Repository;

import com.common.jdbc.JdbcTemplateBaseDao;
import com.common.jdbc.SqlBuilder;
import com.wechat.dao.WeiXinDao;
import com.wechat.entity.WeiXin;

@Repository
public class WeiXinDaoImpl extends JdbcTemplateBaseDao implements WeiXinDao {

	@Override
	public Long add(WeiXin weiXin) {
		return super.add(weiXin);
	}

	@Override
	public WeiXin getByUnionid(String unionid) {
		SqlBuilder sqlBuilder = new SqlBuilder("select * from WeiXin where 1=1");
		if (sqlBuilder.ifNotNull(unionid)) {
			sqlBuilder.andEqualTo("unionid", unionid);
		}
		return super.queryForObject(sqlBuilder);
	}

	public WeiXin getByUser(Long userId) {
		SqlBuilder sqlBuilder = new SqlBuilder("select * from WeiXin where 1=1");
		if (sqlBuilder.ifNotNull(userId)) {
			sqlBuilder.andEqualTo("userId", userId);
		}
		return super.queryForObject(sqlBuilder);
	}

	@Override
	public WeiXin get(Long id) {
		return super.queryForObject(id);
	}

	@Override
	public void delete(Long id) {
		super.delete(id);
	}

	@Override
	protected Class<?> getEntityClass() {
		return WeiXin.class;
	}
}
