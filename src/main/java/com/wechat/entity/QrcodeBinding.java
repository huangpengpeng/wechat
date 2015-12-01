package com.wechat.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import com.wechat.entity.base.BaseQrcodeBinding;

@Entity
@Table(uniqueConstraints = { @UniqueConstraint(columnNames = { "partnerId", "wechatUserId" }) })
public class QrcodeBinding extends BaseQrcodeBinding {

	public QrcodeBinding() {
	}

	public QrcodeBinding(Long partnerId, Long qrcodeId, Long wechatUserId) {
		super(partnerId, qrcodeId, wechatUserId);
	}

	public void init() {
		if (getCreatetime() == null) {
			setCreatetime(new Date());
		}
	}

	private static final long serialVersionUID = 6125716893617619924L;
}
