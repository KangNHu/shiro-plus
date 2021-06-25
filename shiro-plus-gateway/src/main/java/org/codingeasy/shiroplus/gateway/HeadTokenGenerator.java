package org.codingeasy.shiroplus.gateway;

import org.apache.commons.collections.CollectionUtils;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.BearerToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;

import java.net.InetSocketAddress;
import java.util.List;

/**
* 基于请求头 token生成器
 * <p>基于请求头Authentication 获取token</p>
* @author : KangNing Hu
*/
public class HeadTokenGenerator implements TokenGenerator{

	private static final Logger log = LoggerFactory.getLogger(HeadTokenGenerator.class);

	private static final String AUTHENTICATION = "Authentication";

	@Override
	public AuthenticationToken generate(ServerHttpRequest request) {
		HttpHeaders headers = request.getHeaders();
		List<String> tokens = headers.get(AUTHENTICATION);
		InetSocketAddress remoteAddress = request.getRemoteAddress();
		if (CollectionUtils.isEmpty(tokens) || remoteAddress == null){
			log.warn("创建 authentication token 失败 {} ,{}" ,
					remoteAddress == null ? "" : remoteAddress.getHostString(),
					tokens);
			return null;
		}
		return new BearerToken( tokens.get(0) , remoteAddress.getHostString());
	}
}
