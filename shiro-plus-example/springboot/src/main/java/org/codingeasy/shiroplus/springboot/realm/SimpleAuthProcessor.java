package org.codingeasy.shiroplus.springboot.realm;

import org.codingeasy.shiroplus.core.realm.RequestToken;
import org.codingeasy.shiroplus.core.realm.processor.HttpServletAuthProcessor;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.*;


/**
* 简单的权限处理器  
* @author : KangNing Hu
*/
@Component
public class SimpleAuthProcessor extends HttpServletAuthProcessor {

	private String userToken = "abc123456";

	private String userId = "123456";

	private static Map<String , Set<String>> map = new LinkedHashMap<>();


	static {
		map.put("123456:permission" , new HashSet<String>(Arrays.asList("test" ,"admin")));
		map.put("123456:role" , new HashSet<String>(Arrays.asList("add" ,"update" , "delete")));
	}




	@Override
	public String getToken(HttpServletRequest request) {
		return request.getHeader("token");
	}

	@Override
	public Object validate(RequestToken<HttpServletRequest> requestToken) {
		String token = (String) requestToken.getCredentials();
		if (userToken.equals(token)){
			return userId;
		}
		return null;
	}

	@Override
	public Set<String> getPermissions(Object primaryPrincipal) {
		return map.get(primaryPrincipal + ":permission");
	}
}
