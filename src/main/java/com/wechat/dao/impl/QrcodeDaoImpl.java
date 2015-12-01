package com.wechat.dao.impl;

import org.springframework.stereotype.Repository;

import com.common.jdbc.JdbcTemplateBaseDao;
import com.common.jdbc.SqlBuilder;
import com.wechat.dao.QrcodeDao;
import com.wechat.entity.Qrcode;

@Repository
public class QrcodeDaoImpl extends JdbcTemplateBaseDao implements QrcodeDao{

	public long add(Qrcode qrcode) {
		return super.add(qrcode);
	}

	public Qrcode getBySign(Long partnerId, String sign) {
		SqlBuilder sqlBuilder=new SqlBuilder("select * from Qrcode where 1=1");
		if(sqlBuilder.ifNotNull(partnerId)){
			sqlBuilder.andEqualTo("partnerId", partnerId);
		}
		if(sqlBuilder.ifNotNull(sign)){
			sqlBuilder.andEqualTo("sign", sign);
		}
		return queryForObject(sqlBuilder);
	}

	@Override
	protected Class<?> getEntityClass() {
		return Qrcode.class;
	}

	public void update(Long id, String ticket) {
		SqlBuilder sqlBuilder = new SqlBuilder(
				"update Qrcode set gmtModify=current_timestamp()");
		if (sqlBuilder.ifNotNull(ticket)) {
			sqlBuilder.set("ticket", ticket);
		}
		super.update(id, sqlBuilder);
	}

	public Qrcode get(Long id) {
		return super.queryForObject(id);
	}
}
