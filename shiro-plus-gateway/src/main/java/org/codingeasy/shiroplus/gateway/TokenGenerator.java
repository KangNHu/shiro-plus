package org.codingeasy.shiroplus.gateway;

import org.apache.shiro.authc.AuthenticationToken;
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
	 * @return 返回token对象
	 */
	AuthenticationToken generate(ServerHttpRequest request);

}
