package com.wechat.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.util.StringUtils;

public class ResponseConfig<T> {
	
	public final static Long DEFAULT_PARTENR=1L;

	/**
	 * email正则表达式
	 */
	public static final Pattern EMAIL_PATTERN = Pattern
			.compile("^\\w+(\\.\\w+)*@\\w+(\\.\\w+)+$");
	/**
	 * username正则表达式
	 */
	public static final Pattern USERNAME_PATTERN = Pattern
			.compile("^[0-9a-zA-Z\\u4e00-\\u9fa5\\.\\-@_]+$");

	public ResponseConfig<Map<?, Object>> createMap() {
		return new ResponseConfig<Map<?, Object>>();
	}

	public boolean ifNull(Object o, String field) {
		if (o == null) {
			addErrorCode("{filed}不能为空", field);
			return true;
		} else {
			return false;
		}
	}

	public boolean ifEmpty(Object[] o, String field) {
		if (o == null || o.length <= 0) {
			addErrorCode("{filed}不能为空", field);
			return true;
		} else {
			return false;
		}
	}

	public boolean ifBlank(String s, String field, int maxLength) {
		if (!StringUtils.hasText(s)) {
			addErrorCode("{filed}不能为空", field);
			return true;
		}
		if (ifMaxLength(s, field, maxLength)) {
			return true;
		}
		return false;
	}

	public boolean ifMaxLength(String s, String field, int maxLength) {
		if (s != null && s.length() > maxLength) {
			addErrorCode("{filed}长度错误", field);
			return true;
		}
		return false;
	}

	public boolean ifOutOfLength(String s, String field, int minLength,
			int maxLength) {
		if (s == null) {
			addErrorCode("{filed}不能为空", field);
			return true;
		}
		int len = s.length();
		if (len < minLength || len > maxLength) {
			addErrorCode("{filed}长度错误", field);
			return true;
		}
		return false;
	}

	public boolean ifNotEmail(String email, String field, int maxLength) {
		if (ifBlank(email, field, maxLength)) {
			return true;
		}
		Matcher m = EMAIL_PATTERN.matcher(email);
		if (!m.matches()) {
			addErrorCode("{filed}格式错误", field);
			return true;
		}
		return false;
	}

	public boolean ifNotUsername(String username, String field, int minLength,
			int maxLength) {
		if (ifOutOfLength(username, field, minLength, maxLength)) {
			return true;
		}
		Matcher m = USERNAME_PATTERN.matcher(username);
		if (!m.matches()) {
			addErrorCode("{filed}格式错误", field);
			return true;
		}
		return false;
	}

	/**
	 * 是否存在错误
	 * 
	 * @return
	 */
	public boolean hasErrors() {
		return errors != null && errors.size() > 0;
	}

	/**
	 * 添加错误字符串
	 * 
	 * @param error
	 */
	public void addErrorString(String error) {
		errors.add(error);
	}
	
	public String toErrorsString(){
		return errors.get(0);
	}

	public void addErrorCode(String code, String field) {
		errors.add(code.replace("{field}", field));
	}

	private List<String> errors = new ArrayList<String>();

	private T resultValue;

	public T getResultValue() {
		return resultValue;
	}

	public void setResultValue(T resultValue) {
		this.resultValue = resultValue;
	}

	public List<String> getErrors() {
		return errors;
	}
}
