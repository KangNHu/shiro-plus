package org.codingeasy.shiroplus.core.interceptor;

import com.google.common.base.Charsets;
import com.google.common.net.MediaType;
import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.authz.UnauthenticatedException;
import org.apache.shiro.web.util.WebUtils;
import org.codingeasy.shiroplus.core.event.EventManager;
import org.codingeasy.shiroplus.core.handler.AuthExceptionHandler;
import org.codingeasy.shiroplus.core.metadata.AuthMetadataManager;
import org.codingeasy.shiroplus.core.metadata.GlobalMetadata;
import org.codingeasy.shiroplus.core.utils.PathUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * 动态授权过滤器
 *
 * @author : kangning <a>2035711178@qq.com</a>
 */
public class DynamicAuthorizationFilter extends AbstractAuthorizationInterceptor implements Filter {

	private static final Logger logger = LoggerFactory.getLogger(DynamicAuthorizationFilter.class);


	public DynamicAuthorizationFilter(AuthMetadataManager authMetadataManager ,  EventManager eventManager) {
		super(authMetadataManager, new DefaultAuthExceptionHandler() , eventManager);
	}


	public DynamicAuthorizationFilter(AuthMetadataManager authMetadataManager, AuthExceptionHandler authExceptionHandler , EventManager eventManager) {
		super(authMetadataManager, authExceptionHandler , eventManager);
	}

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {

	}


	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		if (request instanceof HttpServletRequest) {
			invoke(new WebInvoker((HttpServletRequest) request, (HttpServletResponse) response, chain));
		}
	}

	@Override
	public void destroy() {

	}


	@Override
	protected boolean isEnableAuthorization(Invoker invoker) {
		WebInvoker webInvoker = (WebInvoker) invoker;
		//获取全局配置
		String tenantId = this.tenantIdGenerator.generate(invoker);
		GlobalMetadata globalMetadata = this.authMetadataManager.getGlobalMetadata(tenantId);
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

	/**
	 * 默认异常处理器
	 *
	 * @author kangning <a>2035711178@qq.com</a>
	 */
	static class DefaultAuthExceptionHandler implements AuthExceptionHandler {


		@Override
		public void authorizationFailure(Invoker invoker , AuthorizationException e) {
			WebInvoker webInvoker = (WebInvoker) invoker;
			HttpServletResponse response = webInvoker.getResponse();
			response.setContentType(MediaType.ANY_TEXT_TYPE.toString());
			if (e instanceof UnauthenticatedException){
				response.setStatus(HttpServletResponse.SC_FORBIDDEN);
			}else {
				response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			}
			try {
				response.setCharacterEncoding(Charsets.UTF_8.name());
				PrintWriter writer = response.getWriter();
				writer.write(e.getMessage());
				writer.flush();
			} catch (Exception e1) {
				logger.warn("授权失败处理失败 ", e1);
			}
		}
	}


}
