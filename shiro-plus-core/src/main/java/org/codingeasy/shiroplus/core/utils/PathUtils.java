package org.codingeasy.shiroplus.core.utils;


import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.AntPathMatcher;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
*   http 相关工具类
* @author : KangNing Hu
*/
public class PathUtils {


	private  static AntPathMatcher antPathMatcher = new AntPathMatcher();

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
	@Deprecated
	public static String getControlPath(HttpServletRequest request){
		// requestURI = /a/hello/world
		String requestURI = request.getRequestURI();
		// contextPath  = /a or "/" or ""
		String prefixPath = request.getContextPath();
		// 映射路径（子路径）
		return StringUtils.substringAfter(requestURI, StringUtils.replace(prefixPath, "//", "/"));
	}

	/**
	 * 匹配url
	 * @param patterns 规则列表
	 * @param url 请求url
	 * @return 如果规则列表有一个匹配上则返回true 如果一个都没有匹配上则返回false
	 */
	public static boolean matches(List<String> patterns , String url){
		if (CollectionUtils.isEmpty(patterns)){
			return false;
		}
		for (String anon : patterns) {
			if (antPathMatcher.match(anon, url)) {
				return true;
			}
		}
		return false;
	}
}
