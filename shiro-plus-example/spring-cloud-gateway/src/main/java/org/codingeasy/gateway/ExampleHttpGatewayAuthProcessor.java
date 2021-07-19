package org.codingeasy.gateway;

import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.codingeasy.shiroplus.core.realm.RequestToken;
import org.codingeasy.shiroplus.gateway.HttpGatewayAuthProcessor;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;

import java.util.*;

/**
*   
* @author : KangNing Hu
*/
@Component
public class ExampleHttpGatewayAuthProcessor extends HttpGatewayAuthProcessor {


	private static Map<Object , Set<String>> map = new HashMap<>();

	private String userToken = "abc123456";
	static {
		map.put("123456" + ":roles" , new HashSet<String>(Arrays.asList("test" ,"admin")));
		map.put("123456" + ":permi" , new HashSet<String>(Arrays.asList("add" ,"update" , "delete","get")));
	}

	@Override
	public String getToken(ServerHttpRequest request) {
		return request.getQueryParams().getFirst("token");
	}

	@Override
	public Object validate(RequestToken<ServerHttpRequest> requestToken) {
		String token = (String) requestToken.getPrincipal();
		if (userToken.equals(token)){
			return "123456";
		}
		return null;
	}


	@Override
	public Set<String> getPermissions(Object primaryPrincipal) {
		return map.get(primaryPrincipal + ":permi");
	}

	@Override
	public Set<String> getRoles(Object primaryPrincipal) {
		return map.get(primaryPrincipal + ":roles");
	}
}
