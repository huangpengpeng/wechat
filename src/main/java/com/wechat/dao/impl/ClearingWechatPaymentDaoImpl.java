package com.wechat.dao.impl;

import java.math.BigDecimal;
import java.util.Date;

import org.springframework.stereotype.Repository;

import com.common.jdbc.JdbcTemplateBaseDao;
import com.common.jdbc.SqlBuilder;
import com.common.jdbc.page.Pagination;
import com.wechat.dao.ClearingWechatPaymentDao;
import com.wechat.entity.ClearingWechatPayment;
import com.wechat.entity.ClearingWechatPayment.ClearingwWechatPaymentStatus;

@Repository
public class ClearingWechatPaymentDaoImpl extends JdbcTemplateBaseDao implements ClearingWechatPaymentDao{

	@Override
	public long add(ClearingWechatPayment clearingWechatPayment) {
		return super.add(clearingWechatPayment);
	}

	@Override
	public ClearingWechatPayment get(Long id) {
		return super.queryForObject(id);
	}

	@Override
	public ClearingWechatPayment getByExternalNo(String type, String externalNo) {
		SqlBuilder sqlBuilder = new SqlBuilder(
				"select * from ClearingWechatPayment where 1=1");
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
		return ClearingWechatPayment.class;
	}

	@Override
	public void clearing(Long id, String requestNo) {
		SqlBuilder sqlBuilder = new SqlBuilder(
				"update ClearingWechatPayment set gmtModify=current_timestamp() , clearingTime=now(),status=?,requestNo=?");
		sqlBuilder.setParam(ClearingwWechatPaymentStatus.已支付.toString());
		sqlBuilder.setParam(requestNo);
		super.update(id, sqlBuilder);
	}

	@Override
	public Pagination getPage(Long userId,String status,Date startCreateTime, Date endCreateTime,
			Integer pageNo) {
		SqlBuilder sqlBuilder = new SqlBuilder(
				"select * from ClearingWechatPayment where 1=1");
		if (sqlBuilder.ifNotNull(startCreateTime)) {
			sqlBuilder.andGreaterThanOrEqualTo("createTime", startCreateTime);
		}
		if (sqlBuilder.ifNotNull(endCreateTime)) {
			sqlBuilder.andLessThanOrEqualTo("createTime", endCreateTime);
		}
		if (sqlBuilder.ifNotNull(userId)) {
			sqlBuilder.andEqualTo("userId", userId);
		}
		if (sqlBuilder.ifNotNull(status)) {
			sqlBuilder.andEqualTo("status", status);
		}
		return super.getPage(sqlBuilder, pageNo == null ? 1 : pageNo, 10);
	}

	@Override
	public void repair(Long id, BigDecimal clearingFee) {
		SqlBuilder sqlBuilder = new SqlBuilder(
				"update ClearingWechatPayment set gmtModify=current_timestamp() ");
		sqlBuilder.set("clearingFee", clearingFee);
		super.update(id, sqlBuilder);
	}
}
