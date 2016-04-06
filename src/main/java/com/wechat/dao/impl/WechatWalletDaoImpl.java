package com.wechat.dao.impl;

import org.springframework.stereotype.Repository;

import com.common.jdbc.JdbcTemplateBaseDao;
import com.common.jdbc.SqlBuilder;
import com.wechat.dao.WechatWalletDao;
import com.wechat.entity.WechatWallet;

@Repository
public class WechatWalletDaoImpl extends JdbcTemplateBaseDao implements WechatWalletDao{

	@Override
	public long add(WechatWallet wechatWallet) {
		return super.add(wechatWallet);
	}

	@Override
	public WechatWallet get(Long id) {
		return super.queryForObject(id);
	}

	@Override
	public void update(Long id, String username, String wechatPayNo,Boolean identificationFlag) {
		SqlBuilder sqlBuilder = new SqlBuilder(
				"update WechatWallet set gmtModify=current_timestamp()");
		if (sqlBuilder.ifNotNull(username)) {
			sqlBuilder.set("username", username);
		}		
		if (sqlBuilder.ifNotNull(wechatPayNo)) {
			sqlBuilder.set("wechatPayNo", wechatPayNo);
		}	
		
		sqlBuilder.set("identificationFlag", identificationFlag);
		
		super.update(id, sqlBuilder);
	}

	@Override
	protected Class<?> getEntityClass() {
		return WechatWallet.class;
	}

	@Override
	public WechatWallet getByexternalNo(String externalNo) {
		SqlBuilder sqlBuilder = new SqlBuilder("select * from WechatWallet where 1=1");
		if (sqlBuilder.ifNotNull(externalNo)) {
			sqlBuilder.andEqualTo("externalNo", externalNo);
		}
		return super.queryForObject(sqlBuilder);
	}
}
