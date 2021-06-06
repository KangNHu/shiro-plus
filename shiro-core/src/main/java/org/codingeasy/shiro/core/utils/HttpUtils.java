package org.codingeasy.shiro.core.utils;


import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletRequest;

/**
*   http 相关工具类
* @author : KangNing Hu
*/
public class HttpUtils {


	/**
	 * 获取control 方法对应的path 不包括根目录
	 * <p>
	 *     如 url:http://localhost:8080/root/user/get
	 *     其中 根目录为 /root
	 *     control path为 /user/get
	 * </p>
	 * @param request 请求对象
	 * @return 返回control path 如上例子中则返回 /user/get
	 */
	public static String getControlPath(HttpServletRequest request){
		// requestURI = /a/hello/world
		String requestURI = request.getRequestURI();
		// contextPath  = /a or "/" or ""
		String prefixPath = request.getContextPath();
		// 映射路径（子路径）
		return StringUtils.substringAfter(requestURI, StringUtils.replace(prefixPath, "//", "/"));
	}
}
