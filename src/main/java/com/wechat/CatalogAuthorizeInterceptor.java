package com.wechat;

import java.io.UnsupportedEncodingException;
import java.util.regex.Pattern;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.common.util.WebUtils;
import com.common.web.RequestUtils;

/**
 * 微信支付目录授权拦截器
 * @author huangpengpeng
 *
 */
public class CatalogAuthorizeInterceptor extends HandlerInterceptorAdapter{

	@Override
	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler) throws Exception {
		boolean ifcatalogFlag = true;
		String uri = getURI(request);
		String requestrealm = WebUtils.getRealm(request);

		if (exclude(uri)) {
			if (!StringUtils.equalsIgnoreCase(requestrealm, realm)) {
				String redirectURI = RequestUtils.getLocation(request);
				response.sendRedirect(redirectURI.replaceFirst(requestrealm,
						realm));
				ifcatalogFlag = false;
			}
		}

		return ifcatalogFlag;
	}
	
	
	private boolean exclude(String uri) {
		if (urls != null) {
			for (String exc : urls) {
				if (Pattern.matches(exc, uri)) {
					return true;
				}
			}
		}
		return false;
	}

	
	/**
	 * 获得第三个路径分隔符的位置
	 * 
	 * @param request
	 * @throws IllegalStateException
	 *             访问路径错误，没有三(四)个'/'
	 * @throws UnsupportedEncodingException
	 */
	private static String getURI(HttpServletRequest request)
			throws IllegalStateException, UnsupportedEncodingException {
		if (request.getAttribute(RequestDispatcher.INCLUDE_REQUEST_URI) != null) {
			String result = (String) request
					.getAttribute(RequestDispatcher.INCLUDE_PATH_INFO);
			if (result == null) {
				result = (String) request
						.getAttribute(RequestDispatcher.INCLUDE_SERVLET_PATH);
			} else {
				result = (String) request
						.getAttribute(RequestDispatcher.INCLUDE_SERVLET_PATH)
						+ result;
			}
			if ((result == null) || (result.equals(""))) {
				result = "/";
			}
			return (result);
		}

		// No, extract the desired path directly from the request
		String result = request.getPathInfo();
		if (result == null) {
			result = request.getServletPath();
		} else {
			result = request.getServletPath() + result;
		}
		if ((result == null) || (result.equals(""))) {
			result = "/";
		}
		return result;
	}

	
	/**
	 * 支付页面地址
	 */
	private String [] urls;
	/**
	 * 支付授权域名
	 */
	private  String realm;
	public String[] getUrls() {
		return urls;
	}


	public void setUrls(String[] urls) {
		this.urls = urls;
	}


	public String getRealm() {
		return realm;
	}


	public void setRealm(String realm) {
		this.realm = realm;
	}
}
