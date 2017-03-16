package com.wechat.dao.impl;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.common.jdbc.JdbcTemplateBaseDao;
import com.common.jdbc.SqlBuilder;
import com.wechat.dao.PartnerDao;
import com.wechat.entity.Partner;

@Repository
public class PartnerDaoImpl extends JdbcTemplateBaseDao implements PartnerDao {

	@Override
	public long add(Partner partner) {
		return super.add(partner);
	}

	@Override
	public void delete(Long id) {
		super.delete(id);
	}

	@Override
	public Partner get(Long id) {
		return super.queryForObject(id);
	}

	@Override
	public Partner getByToken(String token) {
		SqlBuilder sqlBuilder = new SqlBuilder("select * from Partner where 1=1");
		if (sqlBuilder.ifNotNull(token)) {
			sqlBuilder.andEqualTo("token", token);
		}
		return queryForObject(sqlBuilder);
	}

	@Override
	public void update(Long id, String name, String appId, String secretKey, String mchId, String signKey,
			String deviceInfo, String realm, String token, Date registerTime, String callUrl,Boolean ifPushFlg) {
		SqlBuilder sqlBuilder = new SqlBuilder("update Partner set gmtModify=current_timestamp() ");
		if (sqlBuilder.ifNotNull(name)) {
			sqlBuilder.set("name", name);
		}
		if (sqlBuilder.ifNotNull(appId)) {
			sqlBuilder.set("appId", appId);
		}
		if (sqlBuilder.ifNotNull(secretKey)) {
			sqlBuilder.set("secretKey", secretKey);
		}
		sqlBuilder.set("mchId", mchId);
		sqlBuilder.set("signKey", signKey);
		sqlBuilder.set("deviceInfo", deviceInfo);
		if (sqlBuilder.ifNotNull(realm)) {
			sqlBuilder.set("realm", realm);
		}
		if (sqlBuilder.ifNotNull(token)) {
			sqlBuilder.set("token", token);
		}
		sqlBuilder.set("registerTime", registerTime);
		sqlBuilder.set("callUrl", callUrl);
		if (sqlBuilder.ifNotNull(ifPushFlg)) {
			sqlBuilder.set("ifPushFlg",ifPushFlg);
		}
		super.update(id, sqlBuilder);
	}

	@Override
	protected Class<?> getEntityClass() {
		return Partner.class;
	}

	@Override
	public List<Partner> getList() {
		SqlBuilder sqlBuilder = new SqlBuilder("select * from Partner where 1=1");
		return super.query(sqlBuilder);
	}
}
