package com.wechat.dao.impl;

import org.springframework.stereotype.Repository;

import com.common.jdbc.JdbcTemplateBaseDao;
import com.common.jdbc.SqlBuilder;
import com.wechat.dao.PartnerDao;
import com.wechat.entity.Partner;

@Repository
public class PartnerDaoImpl extends JdbcTemplateBaseDao implements PartnerDao {

	public Partner get(Long id) {
		return super.queryForObject(id);
	}

	@Override
	protected Class<?> getEntityClass() {
		return Partner.class;
	}

	public Partner getByToken(String token) {
		SqlBuilder sqlBuilder=new SqlBuilder("select * from Partner where 1=1");
		if(sqlBuilder.ifNotNull(token)){
			sqlBuilder.andEqualTo("token", token);
			}
		return queryForObject(sqlBuilder);
	}
}
