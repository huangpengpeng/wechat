package com.wechat.entity.base;

import java.util.Date;

import javax.persistence.Column;

import com.common.jdbc.Comment;
import com.common.jdbc.VersionEntity;

@javax.persistence.MappedSuperclass
public class BaseWeiXin extends VersionEntity {

	private static final long serialVersionUID = -565056849240817948L;
	
	public BaseWeiXin() {
	}

	public BaseWeiXin(Long userId, String openid, String unionid) {
		this.userId = userId;
		this.openid = openid;
		this.unionid = unionid;
	}

	@Comment(value="用户ID")
	@Column(unique=true)
	private Long userId;
	
	@Comment(value="微信用户openid")
	private String openid;
	
	@Comment(value="微信用户unionid")
	@Column(unique=true)
	private String unionid;
	
	@Comment(value="创建时间")
	private Date createTime;

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getOpenid() {
		return openid;
	}

	public void setOpenid(String openid) {
		this.openid = openid;
	}

	public String getUnionid() {
		return unionid;
	}

	public void setUnionid(String unionid) {
		this.unionid = unionid;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
}
