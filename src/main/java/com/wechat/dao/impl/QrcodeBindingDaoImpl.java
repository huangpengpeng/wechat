package com.wechat.dao.impl;

import org.springframework.stereotype.Repository;

import com.common.jdbc.JdbcTemplateBaseDao;
import com.common.jdbc.SqlBuilder;
import com.wechat.dao.QrcodeBindingDao;
import com.wechat.entity.QrcodeBinding;

@Repository
public class QrcodeBindingDaoImpl extends JdbcTemplateBaseDao implements QrcodeBindingDao{

	public void add(QrcodeBinding qrcodeBinding) {
		super.add(qrcodeBinding);
	}

	public QrcodeBinding getByQrcode(Long partnerId,
			Long wechatUserId) {
		SqlBuilder sqlBuilder = new SqlBuilder(
				"select * from QrcodeBinding where 1=1");
		if (sqlBuilder.ifNotNull(partnerId)) {
			sqlBuilder.andEqualTo("partnerId", partnerId);
		}
		if (sqlBuilder.ifNotNull(wechatUserId)) {
			sqlBuilder.andEqualTo("wechatUserId", wechatUserId);
		}
		return queryForObject(sqlBuilder);
	}

	@Override
	protected Class<?> getEntityClass() {
		return QrcodeBinding.class;
	}

	@Override
	public void delete(Long id) {
		super.delete(id);
	}
}
