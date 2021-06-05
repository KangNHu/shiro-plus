package org.codingeasy.shiro.authorize.interceptor;

import com.google.common.base.Charsets;
import com.google.common.net.MediaType;
import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.authz.UnauthenticatedException;
import org.codingeasy.shiro.authorize.handler.AuthExceptionHandler;
import org.codingeasy.shiro.authorize.metadata.AuthMetadataManager;
import org.codingeasy.shiro.authorize.metadata.GlobalMetadata;
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
 * @author : KangNing Hu
 */
public class DynamicAuthorizationFilter extends AbstractAuthorizationInterceptor implements Filter {

	private static final Logger logger = LoggerFactory.getLogger(DynamicAuthorizationFilter.class);


	public DynamicAuthorizationFilter(AuthMetadataManager authMetadataManager) {
		super(authMetadataManager, new DefaultAuthExceptionHandler());
	}


	public DynamicAuthorizationFilter(AuthMetadataManager authMetadataManager, AuthExceptionHandler authExceptionHandler) {
		super(authMetadataManager, authExceptionHandler);
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
		String tenantId = this.tenantIdGenerator.generate(invoker);
		GlobalMetadata globalMetadata = this.authMetadataManager.getGlobalMetadata(tenantId);
		return globalMetadata == null || globalMetadata.getEnableAuthorization() == null || globalMetadata.getEnableAuthorization();
	}

	/**
	 * 默认异常处理器
	 *
	 * @author hukangning
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
