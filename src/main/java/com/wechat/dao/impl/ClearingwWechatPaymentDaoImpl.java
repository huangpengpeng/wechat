package com.wechat.dao.impl;

import org.springframework.stereotype.Repository;

import com.common.jdbc.JdbcTemplateBaseDao;
import com.common.jdbc.SqlBuilder;
import com.wechat.dao.ClearingwWechatPaymentDao;
import com.wechat.entity.ClearingwWechatPayment;

@Repository
public class ClearingwWechatPaymentDaoImpl extends JdbcTemplateBaseDao implements ClearingwWechatPaymentDao{

	@Override
	public long add(ClearingwWechatPayment clearingwWechatPayment) {
		return super.add(clearingwWechatPayment);
	}

	@Override
	public ClearingwWechatPayment get(Long id) {
		return super.queryForObject(id);
	}

	@Override
	public ClearingwWechatPayment getByExternalNo(String type, String externalNo) {
		SqlBuilder sqlBuilder = new SqlBuilder(
				"select * from ClearingwWechatPayment where 1=1");
		if (sqlBuilder.ifNotNull(type)) {
			sqlBuilder.andEqualTo("type", type);
		}
		if (sqlBuilder.ifNotNull(externalNo)) {
			sqlBuilder.andEqualTo("externalNo", externalNo);
		}
		return super.queryForObject(sqlBuilder);
	}

	@Override
	protected Class<?> getEntityClass() {
		return ClearingwWechatPayment.class;
	}
}
