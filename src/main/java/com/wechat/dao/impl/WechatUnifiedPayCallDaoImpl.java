package com.wechat.dao.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.common.jdbc.JdbcTemplateBaseDao;
import com.common.jdbc.SqlBuilder;
import com.wechat.dao.WechatUnifiedPayCallDao;
import com.wechat.entity.WechatUnifiedPayCall;

@Repository
public class WechatUnifiedPayCallDaoImpl extends JdbcTemplateBaseDao implements WechatUnifiedPayCallDao{

	@Override
	public long add(WechatUnifiedPayCall unifiedCall) {
		return super.add(unifiedCall);
	}

	@Override
	public WechatUnifiedPayCall getByRequestNo(String requestNo) {
		SqlBuilder sqlBuilder=new SqlBuilder("select * from WechatUnifiedPayCall where 1=1");
		if(sqlBuilder.ifNotNull(requestNo)){
			sqlBuilder.andEqualTo("requestNo", requestNo);
		}
		return queryForObject(sqlBuilder);
	}

	@Override
	public List<WechatUnifiedPayCall> getByExternalNo(String externalNo) {
		SqlBuilder sqlBuilder=new SqlBuilder("select * from WechatUnifiedPayCall where 1=1");
		if(sqlBuilder.ifNotNull(externalNo)){
			sqlBuilder.andEqualTo("externalNo", externalNo);
		}
		return query(sqlBuilder);
	}

	@Override
	public void update(Long id, String responseText, String responseCode,
			String responseMsg) {
		SqlBuilder sqlBuilder = new SqlBuilder(
				"update WechatUnifiedPayCall set gmtModify=current_timestamp()");
		if (sqlBuilder.ifNotNull(responseText)) {
			sqlBuilder.set("responseText", responseText);
		}
		if (sqlBuilder.ifNotNull(responseCode)) {
			sqlBuilder.set("responseCode", responseCode);
		}
		if (sqlBuilder.ifNotNull(responseMsg)) {
			sqlBuilder.set("responseMsg", responseMsg);
		}
		super.update(id, sqlBuilder);
	}

	@Override
	protected Class<?> getEntityClass() {
		return WechatUnifiedPayCall.class;
	}
}
