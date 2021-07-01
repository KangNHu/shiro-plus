package org.codingeasy.shiroplus.gateway;

import org.apache.shiro.authc.AuthenticationToken;
import org.codingeasy.shiroplus.core.metadata.GlobalMetadata;
import org.codingeasy.shiroplus.gateway.token.GatewayAuthenticationToken;
import org.springframework.http.server.reactive.ServerHttpRequest;

import javax.servlet.http.HttpServletRequest;

/**
* token生成器  
* @author : KangNing Hu
*/
public interface TokenGenerator {

	/**
	 * 生成 鉴权token
	 * @param request 请求对象
	 * @param globalMetadata 全局元信息
	 * @return 返回token对象
	 */
	GatewayAuthenticationToken generate(ServerHttpRequest request , GlobalMetadata globalMetadata);

}
