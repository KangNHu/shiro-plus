package org.codingeasy.shiroplus.loader.admin.server.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Charsets;
import com.google.common.net.MediaType;
import org.apache.shiro.authz.UnauthenticatedException;
import org.codingeasy.shiroplus.core.interceptor.WebInvoker;
import org.codingeasy.shiroplus.loader.admin.server.models.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.mvc.condition.RequestConditionHolder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;

/**
*
 *   web 相关工具类
* @author : KangNing Hu
*/
public class WebUtils {

	private static final Logger logger = LoggerFactory.getLogger(WebUtils.class);

	private final static String HEAD_TOKEN = "x-token";
	private final static String HEAD_USER_AGENT = "User-Agent";
	private final static String HEAD_FORWARDED_FOR="x-forwarded-for";
	private final static String HEAD_PROXY_CLIENT_IP="Proxy-Client-IP";
	private final static String HEAD_WL_PROXY_CLIENT_IP="WL-Proxy-Client-IP";
	private final static String HEAD_VALUE_EMPTY = "unknown";
	/**
	 * 从请求中获取token
	 * @param request 请求对象
	 * @return 返回token 如果没有返回空
	 */
	public static String getToken(HttpServletRequest request){
		return request.getHeader(HEAD_TOKEN);
	}


	/**
	 * 获取请求用户代理者
	 * @param request 请求对象
	 * @return 返回代理者
	 */
	public static String getUserAgent(HttpServletRequest request){
		return request.getHeader(HEAD_USER_AGENT);
	}

	/**
	 * 获取客户端ip
	 * @param request 请求对象
	 * @return
	 */
	public static String getRemoteIp(HttpServletRequest request){
		String ip = request.getHeader(HEAD_FORWARDED_FOR);
		if (ip == null || ip.length() == 0 || HEAD_VALUE_EMPTY.equalsIgnoreCase(ip)) {
			ip = request.getHeader(HEAD_PROXY_CLIENT_IP);
		}
		if (ip == null || ip.length() == 0 || HEAD_VALUE_EMPTY.equalsIgnoreCase(ip)) {
			ip = request.getHeader(HEAD_WL_PROXY_CLIENT_IP);
		}
		if (ip == null || ip.length() == 0 || HEAD_VALUE_EMPTY.equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
		}
		return ip;
	}


	/**
	 * 获取当前请求对象
	 * @return 返回请求对象
	 */
	public static HttpServletRequest getRequest(){
		return ((ServletRequestAttributes)RequestContextHolder.currentRequestAttributes()).getRequest();
	}
}
