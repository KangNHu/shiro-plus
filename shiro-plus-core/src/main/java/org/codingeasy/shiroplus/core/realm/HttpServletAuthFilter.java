package org.codingeasy.shiroplus.core.realm;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.Assert;
import org.apache.shiro.web.filter.authz.AuthorizationFilter;
import org.apache.shiro.web.util.WebUtils;
import org.codingeasy.shiroplus.core.event.EventManager;
import org.codingeasy.shiroplus.core.interceptor.DynamicAuthorizationFilter;
import org.codingeasy.shiroplus.core.interceptor.WebInvoker;
import org.codingeasy.shiroplus.core.metadata.AuthMetadataManager;
import org.codingeasy.shiroplus.core.metadata.GlobalMetadata;
import org.codingeasy.shiroplus.core.realm.processor.AuthProcessor;
import org.codingeasy.shiroplus.core.utils.PathUtils;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static org.codingeasy.shiroplus.core.metadata.GlobalMetadata.EXTEND_ADMIN_ID_KEY;

/**
* 权限过滤器  
* @author : KangNing Hu
*/
public class HttpServletAuthFilter extends DynamicAuthorizationFilter {
	//是否开启动态授权
	private static boolean enableDynamicAuthorization = false;

	public static final String AUTH_FILTER_NAME = "auth2";

	public HttpServletAuthFilter(AuthMetadataManager authMetadataManager, EventManager eventManager , AuthProcessor<HttpServletRequest , HttpServletResponse> processor) {
		super(authMetadataManager, eventManager);
		setAuthProcessor(authProcessor);
	}

	public static void setEnableDynamicAuthorization(boolean enableDynamicAuthorization) {
		HttpServletAuthFilter.enableDynamicAuthorization = enableDynamicAuthorization;
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		if (request instanceof HttpServletRequest){
			HttpServletRequest httpServletRequest = (HttpServletRequest) request;
			HttpServletResponse httpServletResponse = (HttpServletResponse) response;
			try {
				//不开启放行
				if (!isEnableAuthentication(httpServletRequest)) {
					chain.doFilter(request, response);
					return;
				}
				String token = authProcessor.getToken(httpServletRequest);
				if (StringUtils.isEmpty(token)) {
					throw new AuthenticationException("Invalid certificate");
				}

				//获取主体
				Subject subject = SecurityUtils.getSubject();
				////创建token,执行登录
				subject.login(new RequestToken<>(httpServletRequest, token));
				//超级管理员校验
				Object principal = subject.getPrincipal();
				GlobalMetadata globalMetadata = getGlobalMetadata(httpServletRequest);
				if (globalMetadata != null ) {
					Object adminId = globalMetadata.get(EXTEND_ADMIN_ID_KEY);
					if (adminId != null&& principal != null && adminId.toString().equals(principal.toString())){
						chain.doFilter(request , response);
						return;
					}
				}
				//是否开启动态授权
				if (enableDynamicAuthorization) {
					super.doFilter(request, response, chain);
				}else {
					chain.doFilter(request, response);
				}
			}catch (AuthenticationException e){
				authProcessor.authenticationFailure(httpServletRequest , httpServletResponse , e);
			}
			return;
		}
		chain.doFilter(request , response);
	}



	/**
	 * 是否开启鉴权
	 * @param request 请求对象
	 * @return 如果开启返回true 否则为false
	 */
	private boolean isEnableAuthentication(HttpServletRequest request) {
		//获取全局元信息
		GlobalMetadata globalMetadata = getGlobalMetadata(request);
		if (globalMetadata == null){
			return true;
		}
		String pathWithinApplication = WebUtils.getPathWithinApplication(request);
		//对匿名访问的请求进行处理
		if (PathUtils.matches(globalMetadata.getAnons() , pathWithinApplication)) {
			return false;
		}
		//校验开关
		Boolean enableAuthentication = globalMetadata.getEnableAuthentication();
		return enableAuthentication == null || enableAuthentication;
	}

}
