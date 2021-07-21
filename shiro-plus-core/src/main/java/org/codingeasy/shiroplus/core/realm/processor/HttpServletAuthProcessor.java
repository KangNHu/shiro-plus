package org.codingeasy.shiroplus.core.realm.processor;

import com.google.common.base.Charsets;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authz.AuthorizationException;
import org.codingeasy.shiroplus.core.metadata.GlobalMetadata;
import org.codingeasy.shiroplus.core.metadata.MetadataContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;

/**
* 基于servlet的权限处理器  
* @author : KangNing Hu
*/
public class HttpServletAuthProcessor extends DefaultAuthProcessor<HttpServletRequest  , HttpServletResponse> {

	private Logger logger = LoggerFactory.getLogger(HttpServletAuthProcessor.class);


	/**
	 * 写出响应
	 * @param request 请求对象
	 * @param response 响应对象
	 * @param e
	 */
	@Override
	public void authorizationFailure(HttpServletRequest request, HttpServletResponse response, AuthorizationException e) {
		logger.warn("租户 [{}]授权失败  [{}]" ,
				getTenantId(request),
				e.getMessage());
		writeAuthFailure(response, HttpServletResponse.SC_FORBIDDEN ,e.getMessage());
	}

	/**
	 * 写出响应
	 * @param request 请求对象
	 * @param response  响应对象
	 * @param e
	 */
	@Override
	public void authenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException e) {
		logger.warn("租户 [{}]授权失败  [{}]" ,
				getTenantId(request),
				e.getMessage());
		writeAuthFailure(response,HttpServletResponse.SC_UNAUTHORIZED , e.getMessage());
	}


	/**
	 * 写响应
	 * @param response 响应对象
	 * @param message 请求对象
	 */
	private void writeAuthFailure(HttpServletResponse response,int status , String message) {
		response.setContentType("text/*;charset=utf-8");
		response.setStatus(status);
		try {
			response.setCharacterEncoding(Charsets.UTF_8.name());
			PrintWriter writer = response.getWriter();
			writer.write(message);
			writer.flush();
		} catch (Exception e1) {
			logger.warn("写响应失败 ", e1);
		}
	}

}
