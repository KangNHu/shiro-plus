package org.codingeasy.shiroplus.gateway.config;

import org.apache.shiro.SecurityUtils;
import org.codingeasy.shiroplus.springboot.ShiroPlusSecurityManager;
import org.springframework.context.event.ContextRefreshedEvent;

/**
* 网关安全管理器
 * @see ShiroPlusSecurityManager
* @author : KangNing Hu
*/
public class ShiroPlusGatewaySecurityManager extends ShiroPlusSecurityManager {


	@Override
	public void onApplicationEvent(ContextRefreshedEvent event) {
		super.onApplicationEvent(event);
		//设置上下文
		SecurityUtils.setSecurityManager(this);
	}
}
