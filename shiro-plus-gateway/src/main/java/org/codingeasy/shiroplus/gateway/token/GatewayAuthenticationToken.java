package org.codingeasy.shiroplus.gateway.token;

import org.apache.shiro.authc.AuthenticationToken;
import org.codingeasy.shiroplus.core.metadata.GlobalMetadata;
import org.springframework.http.server.reactive.ServerHttpRequest;

import javax.servlet.http.HttpServletRequest;

/**
*   网关鉴权token
* @author : KangNing Hu
*/
public interface GatewayAuthenticationToken extends AuthenticationToken {

	/**
	 * 获取元数据
	 * @return 返回全局元信息
	 */
	GlobalMetadata getMetadata();


	/**
	 * 获取网关的请求对象
	 * @return 返回请求对象
	 */
	ServerHttpRequest getRequest();
}
