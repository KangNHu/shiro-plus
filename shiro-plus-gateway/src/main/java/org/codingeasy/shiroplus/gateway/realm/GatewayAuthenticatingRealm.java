package org.codingeasy.shiroplus.gateway.realm;

import org.apache.shiro.authc.HostAuthenticationToken;
import org.apache.shiro.realm.AuthenticatingRealm;
import org.codingeasy.shiroplus.gateway.token.GatewayAuthenticationToken;

/**
*    用于网关鉴权 realm
 *  <p>用于适配 {@link org.codingeasy.shiroplus.gateway.factory.AuthGatewayFilterFactory}的默认token生成器</p>
 *  @see org.codingeasy.shiroplus.gateway.factory.AuthGatewayFilterFactory
 *  <p></p>
* @author : KangNing Hu
*/
public abstract class GatewayAuthenticatingRealm extends AuthenticatingRealm {

	@Override
	protected void onInit() {
		super.onInit();
		setAuthenticationTokenClass(GatewayAuthenticationToken.class);
	}
}
