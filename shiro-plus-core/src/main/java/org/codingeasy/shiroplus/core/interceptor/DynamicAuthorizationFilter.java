package org.codingeasy.shiroplus.core.interceptor;


import org.apache.shiro.web.util.WebUtils;
import org.codingeasy.shiroplus.core.event.EventManager;
import org.codingeasy.shiroplus.core.metadata.AuthMetadataManager;
import org.codingeasy.shiroplus.core.metadata.GlobalMetadata;
import org.codingeasy.shiroplus.core.metadata.RequestMethod;
import org.codingeasy.shiroplus.core.realm.processor.DefaultAuthProcessor;
import org.codingeasy.shiroplus.core.utils.PathUtils;


import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 动态授权过滤器
 *
 * @author : kangning <a>2035711178@qq.com</a>
 */
public class DynamicAuthorizationFilter extends AbstractAuthorizationInterceptor<HttpServletRequest , HttpServletResponse> implements Filter  {



	public DynamicAuthorizationFilter(AuthMetadataManager authMetadataManager ,  EventManager eventManager) {
		super(authMetadataManager , eventManager);
		setAuthProcessor(new DefaultAuthProcessor<>());
	}

	/**
	 * 获取权限元数据key
	 * @return 返回key 返回http请求的 path + ":" + method
	 */
	@Override
	protected String getPermissionMetadataKey(HttpServletRequest request) {
		return WebUtils.getPathWithinApplication(request) + ":" + RequestMethod.form(request.getMethod());
	}


	@Override
	public void init(FilterConfig filterConfig) throws ServletException {

	}


	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		if (request instanceof HttpServletRequest) {
			invoke(new WebInvoker((HttpServletRequest) request, (HttpServletResponse) response, chain));
			return;
		}
		chain.doFilter(request , response);
	}

	@Override
	public void destroy() {
		System.out.println();
	}


	@Override
	protected boolean isEnableAuthorization(Invoker<HttpServletRequest , HttpServletResponse> invoker) {
		WebInvoker webInvoker = (WebInvoker) invoker;
		//获取全局配置
		GlobalMetadata globalMetadata = super.getGlobalMetadata(invoker.getRequest());;
		if (globalMetadata == null){
			return true;
		}
		//处理anon 列表（白名单列表）
		if (PathUtils.matches(globalMetadata.getAnons() , WebUtils.getPathWithinApplication(webInvoker.getRequest()))){
			return false;
		}
		//处理总开关
		return globalMetadata.getEnableAuthorization() == null || globalMetadata.getEnableAuthorization();
	}


}
